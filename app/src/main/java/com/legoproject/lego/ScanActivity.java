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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

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

public class ScanActivity extends AppCompatActivity implements CvCameraViewListener2 {

    private JavaCameraView myCamera;
    private boolean a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scan);
        init();


        //----向日葵测试
//        boolean success = OpenCVLoader.initDebug();
//        Bitmap bitmap = BitmapFactory.decodeResource(
//                this.getResources(), R.drawable.test
//        );
//        Mat src = new Mat();
//        Mat dst = new Mat();
//        Utils.bitmapToMat(bitmap, src);
//        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
//        Utils.matToBitmap(dst, bitmap);
//        ImageView iv = this.findViewById(R.id.scan_img);
//        iv.setImageBitmap(bitmap);
//        src.release();
//        dst.release();
        //--------------------------------------
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
        myCamera = findViewById(R.id.main_camera);
        myCamera.setVisibility(SurfaceView.VISIBLE);
        myCamera.setCvCameraViewListener(this);
        myCamera.setCameraIndex(0); //使用后置摄像头
        if (myCamera != null) {
            myCamera.disableView();
        }
        myCamera.enableView();
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
            Core.rotate(frame, frame, Core.ROTATE_90_CLOCKWISE);
        }
//        for (int i=1;i<100;i++)
//            for (int j=1;j<100;j++)
//            {
//
//            }
//        frame = rectangularDetection(frame); //矩形检测
        return rectangularDetection(frame);
    }

    private Mat rectangularDetection(Mat frame) {
        Mat frameGray = new Mat();
        Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGRA2GRAY); //转为灰度图
        Imgproc.Canny(frameGray, frameGray, 200, 400, 3, false); //阈值可调

//        List<MatOfPoint> contours=new ArrayList<>();
//        Imgproc.findContours(frame,contours,new Mat(),Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_NONE);
//        double maxVal = 0;
//        int maxValIdx = 0;
//        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++)
//        {
//            double contourArea = Imgproc.contourArea(contours.get(contourIdx));
//            if (maxVal < contourArea)
//            {
//                maxVal = contourArea;
//                maxValIdx = contourIdx;
//            }
//        }
//        Mat mRgba=new Mat();
//        mRgba.create(frame.rows(), frame.cols(), CvType.CV_8UC3);
//        //绘制检测到的轮廓
//        Imgproc.drawContours(mRgba, contours, maxValIdx, new Scalar(0,255,0), 5);

        List<Point> rect = getCornersByContour(frameGray);
//        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_GRAY2BGR); //转为彩图
        if (rect != null) {
            Imgproc.line(frame, rect.get(0), rect.get(1), new Scalar(0, 255, 0), 5, 8, 0);
            Imgproc.line(frame, rect.get(1), rect.get(3), new Scalar(0, 255, 0), 5, 8, 0);
            Imgproc.line(frame, rect.get(2), rect.get(0), new Scalar(0, 255, 0), 5, 8, 0);
            Imgproc.line(frame, rect.get(3), rect.get(2), new Scalar(0, 255, 0), 5, 8, 0);

        }
        return frame;
    }

    public static List<Point> getCornersByContour(Mat imgsource) {
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

        double[] temp_double = approxCurve.get(0, 0);
        if (temp_double==null) return null;
        Point point1 = new Point(temp_double[0], temp_double[1]);

        temp_double = approxCurve.get(1, 0);
        if (temp_double==null) return null;
        Point point2 = new Point(temp_double[0], temp_double[1]);

        temp_double = approxCurve.get(2, 0);
        if (temp_double==null) return null;
        Point point3 = new Point(temp_double[0], temp_double[1]);

        temp_double = approxCurve.get(3, 0);
        if (temp_double==null) return null;
        Point point4 = new Point(temp_double[0], temp_double[1]);

        List<Point> source = new ArrayList<>();
        source.add(point1);
        source.add(point2);
        source.add(point3);
        source.add(point4);
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
        return source;
    }


}