package com.legoproject.lego;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;

public class ScanActivity extends AppCompatActivity implements CvCameraViewListener2 {

    final static int RED = 0;
    final static int ORANGE = 1;
    final static int YELLOW = 2;
    final static int GREEN = 3;
    final static int BLUEGREEN = 4;
    final static int BLUE = 5;
    final static int PURPLE = 6;
    private JavaCameraView myCamera;
    private String colorText;
    private int color = -1;
    private Button button;
    List<Point> source = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scan);
        init();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (color) {
                    case RED:
                        colorText="red";
                        break;
                    case ORANGE:
                        colorText="orange";
                        break;
                    case YELLOW:
                        colorText="yellow";
                        break;
                    case GREEN:
                        colorText="green";
                        break;
                    case BLUEGREEN:
                        colorText="blue-green";
                        break;
                    case BLUE:
                        colorText="blue";
                        break;
                    case PURPLE:
                        colorText="purple";
                        break;}
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error 确保opencv可用
        }

        //相机动态授权
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        //相机初始化
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //设置为竖屏模式
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myCamera = findViewById(R.id.scan_camera);
        myCamera.setVisibility(SurfaceView.VISIBLE);
        myCamera.setCvCameraViewListener(this);
        myCamera.setCameraIndex(0); //使用后置摄像头
        if (myCamera != null) {
            myCamera.disableView();
        }
        myCamera.enableView();
        button = findViewById(R.id.scan_button);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Mat frame = inputFrame.rgba();
        if (this.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            Core.rotate(frame, frame, Core.ROTATE_90_CLOCKWISE); //旋转90度
        }
        frame = rectangularDetection(frame); //矩形检测

        return frame;
    }

    private void colorDetection(Point point, Mat frame) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(frame, hsv, COLOR_BGR2HSV);//BGR转换为HSV
        int x = (int) point.x;
        int y = (int) point.y;

        double[] clone = hsv.get(x, y).clone();
        double hun = clone[0];// HSV hun
        double s = clone[1];
        double v = clone[2];
        if ((s > 43 && s < 255) || (v < 255 && v > 46)) {
            if ((hun >= 0 && hun < 10) || (hun > 160 && hun < 200)) {
                Log.i("Color", "red");
                color = RED;
            } else if (hun >= 11 && hun < 25) {
                Log.i("Color", "ORANGE");
                color = ORANGE;
            } else if (hun >= 26 && hun < 34) {
                Log.i("Color", "YELLOW");
                color = YELLOW;
            } else if (hun >= 35 && hun < 77) {
                Log.i("Color", "GREEN");
                color = GREEN;
            } else if (hun >= 78 && hun < 99) {
                Log.i("Color", "BLUEGREEN");
                color = BLUEGREEN;
            } else if (hun >= 110 && hun < 124) {
                Log.i("Color", "BLUE");
                color = BLUE;
            } else if (hun >= 125 && hun < 155) {
                Log.i("Color", "PURPLE");
                color = PURPLE;
            }
        } else {
            color = -1;

        }


    }


    private Mat rectangularDetection(Mat frame) {
        Mat frameGray = new Mat();
        Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGRA2GRAY); //转为灰度图
        Imgproc.Canny(frameGray, frameGray, 200, 400, 3, false); //阈值可调

        List<Point> rect = getCornersByContour(frameGray);
//        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_GRAY2BGR); //转为彩图
        if (rect != null) {
            Imgproc.line(frame, rect.get(0), rect.get(1), new Scalar(0, 255, 0), 5, 8, 0);
            Imgproc.line(frame, rect.get(1), rect.get(3), new Scalar(0, 255, 0), 5, 8, 0);
            Imgproc.line(frame, rect.get(2), rect.get(0), new Scalar(0, 255, 0), 5, 8, 0);
            Imgproc.line(frame, rect.get(3), rect.get(2), new Scalar(0, 255, 0), 5, 8, 0);
            colorDetection(rect.get(4), frame);

        }
        return frame;
    }

    public List<Point> getCornersByContour(Mat imgsource) {
        List<MatOfPoint> contours = new ArrayList<>();
        //轮廓检测
        Imgproc.findContours(imgsource, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        double maxArea = -1;
        int maxAreaIdx = -1;
        MatOfPoint temp_contour;//假设最大的轮廓在index=0处
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        if (contours.size() == 0) return null;
        for (int idx = 0; idx < contours.size(); idx++) {
            temp_contour = contours.get(idx);
            double contourarea = Imgproc.contourArea(temp_contour);
            //当前轮廓面积比最大的区域面积大就检测是否为四边形
            if (contourarea > maxArea) {
                //检测contour是否是四边形
                MatOfPoint2f new_mat = new MatOfPoint2f(temp_contour.toArray());
                int contourSize = (int) temp_contour.total();
                MatOfPoint2f approxCurve_temp = new MatOfPoint2f();
                //对图像轮廓点进行多边形拟合
                Imgproc.approxPolyDP(new_mat, approxCurve_temp, contourSize * 0.05, true);
                if (approxCurve_temp.total() == 4) {
                    maxArea = contourarea;
                    maxAreaIdx = idx;
                    approxCurve = approxCurve_temp;
                }
            }
        }

        double[] temp_double;

        for (int i = 0; i < 4; i++) {
            temp_double = approxCurve.get(i, 0);
            if (temp_double == null) return null;
            source.add(new Point(temp_double[0], temp_double[1]));
        }
        //对4个点进行排序
        Point centerPoint = new Point(0, 0);//质心
        for (Point corner : source) {
            centerPoint.x += corner.x;
            centerPoint.y += corner.y;
        }
        centerPoint.x = centerPoint.x / source.size();
        centerPoint.y = centerPoint.y / source.size();

        Point lefttop = new Point();
        Point righttop = new Point();
        Point leftbottom = new Point();
        Point rightbottom = new Point();
        for (int i = 0; i < source.size(); i++) {
            if (source.get(i).x < centerPoint.x && source.get(i).y < centerPoint.y) {
                lefttop = source.get(i);
            } else if (source.get(i).x > centerPoint.x && source.get(i).y < centerPoint.y) {
                righttop = source.get(i);
            } else if (source.get(i).x < centerPoint.x && source.get(i).y > centerPoint.y) {
                leftbottom = source.get(i);
            } else if (source.get(i).x > centerPoint.x && source.get(i).y > centerPoint.y) {
                rightbottom = source.get(i);
            }
        }
        source.clear();
        source.add(lefttop);
        source.add(righttop);
        source.add(leftbottom);
        source.add(rightbottom);
        source.add(centerPoint);
        return source;
    }


}