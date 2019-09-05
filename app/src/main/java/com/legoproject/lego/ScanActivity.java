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
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import static java.lang.Math.sqrt;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;

public class ScanActivity extends AppCompatActivity implements CvCameraViewListener2 {

//    final static int RED = 0;
//    final static int ORANGE = 1;
//    final static int YELLOW = 2;
//    final static int GREEN = 3;
//    final static int BLUEGREEN = 4;
//    final static int BLUE = 5;
//    final static int PURPLE = 6;
    private JavaCameraView myCamera;
//    private TextView colorText, lengthText, widthText;
    private int color=-1;
    private Button button;
    private double length, width;
    List<Point> source = new ArrayList<>();

    private LegoAdapter adapter;
    private List<Lego> legoList = new ArrayList<>();
    private int[] sheet = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scan);
        init();
        initLegos();
        adapter = new LegoAdapter(ScanActivity.this,
                R.layout.lego_item, legoList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
//                switch (color) {
//                    case RED:
//                        colorText.setText("red");
//                        break;
//                    case ORANGE:
//                        colorText.setText("orange");
//                        break;
//                    case YELLOW:
//                        colorText.setText("yellow");
//                        break;
//                    case GREEN:
//                        colorText.setText("green");
//                        break;
//                    case BLUEGREEN:
//                        colorText.setText("blue-green");
//                        break;
//                    case BLUE:
//                        colorText.setText("blue");
//                        break;
//                    case PURPLE:
//                        colorText.setText("purple");
//                        break;
//                }
//                lengthText.setText(String.valueOf(length));
//                widthText.setText(String.valueOf(width));
            }
        });


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

    private void initLegos() {
        if(sheet[0]>0){
            Lego red12 = new Lego(0, 1, 2, sheet[0]);
            legoList.add(red12);}
        if(sheet[1]>0){
            Lego orange12 = new Lego(1, 1, 2, sheet[1]);
            legoList.add(orange12);}
        if(sheet[2]>0){
            Lego yellow12 = new Lego(2, 1, 2, sheet[2]);
            legoList.add(yellow12);}
        if(sheet[3]>0){
            Lego green12 = new Lego(3, 1, 2, sheet[3]);
            legoList.add(green12);}
        if(sheet[4]>0){
            Lego bluegreen12 = new Lego(4, 1, 2, sheet[4]);
            legoList.add(bluegreen12);}
        if(sheet[5]>0){
            Lego blue12 = new Lego(5, 1, 2, sheet[5]);
            legoList.add(blue12);}
        if(sheet[6]>0){
            Lego purple12 = new Lego(6, 1, 2, sheet[6]);
            legoList.add(purple12);}
        if(sheet[7]>0){
            Lego red14 = new Lego(0, 1, 4, sheet[7]);
            legoList.add(red14);}
        if(sheet[8]>0){
            Lego orange14 = new Lego(1, 1, 4, sheet[8]);
            legoList.add(orange14);}
        if(sheet[9]>0){
            Lego yellow14 = new Lego(2, 1, 4, sheet[9]);
            legoList.add(yellow14);}
        if(sheet[10]>0){
            Lego green14 = new Lego(3, 1, 4, sheet[10]);
            legoList.add(green14);}
        if(sheet[11]>0){
            Lego bluegreen14 = new Lego(4, 1, 4, sheet[11]);
            legoList.add(bluegreen14);}
        if(sheet[12]>0){
            Lego blue14 = new Lego(5, 1, 4, sheet[12]);
            legoList.add(blue14);}
        if(sheet[13]>0){
            Lego purple14 = new Lego(6, 1, 4, sheet[13]);
            legoList.add(purple14);}
        if(sheet[14]>0){
            Lego red22 = new Lego(0, 2, 2, sheet[14]);
            legoList.add(red22);}
        if(sheet[15]>0){
            Lego orange22 = new Lego(1, 2, 2, sheet[15]);
            legoList.add(orange22);}
        if(sheet[16]>0){
            Lego yellow22 = new Lego(2, 2, 2, sheet[16]);
            legoList.add(yellow22);}
        if(sheet[17]>0){
            Lego green22 = new Lego(3, 2, 2, sheet[17]);
            legoList.add(green22);}
        if(sheet[18]>0){
            Lego bluegreen22 = new Lego(4, 2, 2, sheet[18]);
            legoList.add(bluegreen22);}
        if(sheet[19]>0){
            Lego blue22 = new Lego(5, 2, 2, sheet[19]);
            legoList.add(blue22);}
        if(sheet[20]>0){
            Lego purple22 = new Lego(6, 2, 2, sheet[20]);
            legoList.add(purple22);}
        if(sheet[21]>0){
            Lego red23 = new Lego(0, 2, 3, sheet[21]);
            legoList.add(red23);}
        if(sheet[22]>0){
            Lego orange23 = new Lego(1, 2, 3, sheet[22]);
            legoList.add(orange23);}
        if(sheet[23]>0){
            Lego yellow23 = new Lego(2, 2, 3, sheet[23]);
            legoList.add(yellow23);}
        if(sheet[24]>0){
            Lego green23 = new Lego(3, 2, 3, sheet[24]);
            legoList.add(green23);}
        if(sheet[25]>0){
            Lego bluegreen23 = new Lego(4, 2, 3, sheet[25]);
            legoList.add(bluegreen23);}
        if(sheet[26]>0){
            Lego blue23 = new Lego(5, 2, 3, sheet[26]);
            legoList.add(blue23);}
        if(sheet[27]>0){
            Lego purple23 = new Lego(6, 2, 3, sheet[27]);
            legoList.add(purple23);}
        if(sheet[28]>0){
            Lego red24 = new Lego(0, 2, 4, sheet[28]);
            legoList.add(red24);}
        if(sheet[29]>0){
            Lego orange24 = new Lego(1, 2, 4, sheet[29]);
            legoList.add(orange24);}
        if(sheet[30]>0){
            Lego yellow24 = new Lego(2, 2, 4, sheet[30]);
            legoList.add(yellow24);}
        if(sheet[31]>0){
            Lego green24 = new Lego(3, 2, 4, sheet[31]);
            legoList.add(green24);}
        if(sheet[32]>0){
            Lego bluegreen24 = new Lego(4, 2, 4, sheet[32]);
            legoList.add(bluegreen24);}
        if(sheet[33]>0){
            Lego blue24 = new Lego(5, 2, 4, sheet[33]);
            legoList.add(blue24);}
        if(sheet[34]>0){
            Lego purple24 = new Lego(6, 2, 4, sheet[34]);
            legoList.add(purple24);}
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
//        colorText = findViewById(R.id.color_text);
//        lengthText = findViewById(R.id.length_text);
//        widthText = findViewById(R.id.widith_text);
        button = findViewById(R.id.det_but);
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

//    private void colorDetection(Point point, Mat frame) {
//        Mat hsv = new Mat();
//        Imgproc.cvtColor(frame, hsv, COLOR_BGR2HSV);//BGR转换为HSV
//        int x = (int) point.x;
//        int y = (int) point.y;
//
//        double[] clone = hsv.get(x, y).clone();
//        double hun = clone[0];// HSV hun
//        double s = clone[1];
//        double v = clone[2];
//        if ((s > 43 && s < 255) || (v < 255 && v > 46)) {
//            if ((hun >= 0 && hun < 10) || (hun > 160 && hun < 200)) {
//                Log.i("Color", "red");
//                color = RED;
//            } else if (hun >= 11 && hun < 25) {
//                Log.i("Color", "ORANGE");
//                color = ORANGE;
//            } else if (hun >= 26 && hun < 34) {
//                Log.i("Color", "YELLOW");
//                color = YELLOW;
//            } else if (hun >= 35 && hun < 77) {
//                Log.i("Color", "GREEN");
//                color = GREEN;
//            } else if (hun >= 78 && hun < 99) {
//                Log.i("Color", "BLUEGREEN");
//                color = BLUEGREEN;
//            } else if (hun >= 110 && hun < 124) {
//                Log.i("Color", "BLUE");
//                color = BLUE;
//            } else if (hun >= 125 && hun < 155) {
//                Log.i("Color", "PURPLE");
//                color = PURPLE;
//            }
//        } else {
//            color = -1;
//
//        }
//
//
//    }


    private Mat rectangularDetection(Mat frame) {
//        Mat frameGray = new Mat();
//        Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGRA2GRAY); //转为灰度图
//        Imgproc.GaussianBlur(frameGray, frameGray, new Size(3, 3), 2, 2); //高斯滤波降噪
//        Imgproc.Canny(frameGray, frameGray, 200, 400, 3, false); //canny边缘检测，阈值可调
//        Imgproc.dilate(frameGray, frameGray, new Mat(), new Point(-1, -1), 3, 1, new Scalar(1)); //膨胀
//        if (true) return frameGray;
//        List<Point> rect = getCornersByContour(frameGray);

//        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_GRAY2BGR); //转为彩图
//        if (rect != null) {
//            colorDetection(rect.get(4), frame);
////            frame = frameGray;
//            Imgproc.line(frame, rect.get(0), rect.get(1), new Scalar(0, 255, 0), 5, 8, 0);
//            Imgproc.line(frame, rect.get(1), rect.get(3), new Scalar(0, 255, 0), 5, 8, 0);
//            Imgproc.line(frame, rect.get(2), rect.get(0), new Scalar(0, 255, 0), 5, 8, 0);
//            Imgproc.line(frame, rect.get(3), rect.get(2), new Scalar(0, 255, 0), 5, 8, 0);
//            length = sqrt((rect.get(0).x - rect.get(1).x) * (rect.get(0).x - rect.get(1).x) +
//                    (rect.get(0).y - rect.get(1).y) * (rect.get(0).y - rect.get(1).y));
//            width = sqrt((rect.get(0).x - rect.get(2).x) * (rect.get(0).x - rect.get(2).x) +
//                    (rect.get(0).y - rect.get(2).y) * (rect.get(0).y - rect.get(2).y));
//            Log.i("length", String.valueOf(length));
//            Log.i("width", String.valueOf(width));

//        }
//        return frame;
        return getCornersByContour(frame);
    }

    public Mat getCornersByContour(Mat imgsource) {
        Mat frameGray = new Mat();
        Imgproc.cvtColor(imgsource, frameGray, Imgproc.COLOR_BGRA2GRAY); //转为灰度图
        Imgproc.GaussianBlur(frameGray, frameGray, new Size(3, 3), 2, 2); //高斯滤波降噪
        Imgproc.Canny(frameGray, frameGray, 200, 400, 3, false); //canny边缘检测，阈值可调
//        Imgproc.dilate(frameGray, frameGray, new Mat(), new Point(-1, -1), 3, 1, new Scalar(1)); //膨胀
//        if (true) return frameGray;

        List<MatOfPoint> contours = new ArrayList<>();
        //轮廓检测
        Imgproc.findContours(frameGray, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//        Imgproc.drawContours(imgsource,contours,-1,new Scalar(0,255,0),2);
//        if (true) return imgsource;
        double maxArea = -1;
        int maxAreaIdx = -1;
        MatOfPoint temp_contour;//假设最大的轮廓在index=0处
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        if (contours.size() == 0) return null;
        for (int idx = 0; idx < contours.size(); idx++) {
            temp_contour = contours.get(idx);

            double contourarea = Imgproc.contourArea(temp_contour);
            //当前轮廓面积比最大的区域面积大就检测是否为四边形
//            if (contourarea > 1800) { //900为1X1乐高积木的面积大小
            //检测contour是否是四边形
            MatOfPoint2f new_mat = new MatOfPoint2f(temp_contour.toArray());
            RotatedRect rect = Imgproc.minAreaRect(new_mat);
            Point vertices[] = new Point[4];
            rect.points(vertices);
            for (int i = 0; i < 4; i++)
                Imgproc.line(imgsource, vertices[i], vertices[(i + 1) % 4], new Scalar(0, 255, 0));
            Point centerPoint = new Point(0, 0);//质心
            for (int i = 0; i < 4; i++) {
                centerPoint.x += vertices[i].x;
                centerPoint.y += vertices[i].y;
            }
            centerPoint.x = centerPoint.x / 4;
            centerPoint.y = centerPoint.y / 4;
            List<Point> source = new ArrayList<>();
            for (int i=0;i<4;i++)
                source.add(vertices[i]);
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
            int color = colorDetection(source, imgsource);
            double length = sqrt((source.get(0).x - source.get(1).x) * (source.get(0).x - source.get(1).x) +
                    (source.get(0).y - source.get(1).y) * (source.get(0).y - source.get(1).y));
            double width = sqrt((source.get(0).x - source.get(2).x) * (source.get(0).x - source.get(2).x) +
                    (source.get(0).y - source.get(2).y) * (source.get(0).y - source.get(2).y));
            int lengthInt = (int) (length / 32.5);
            int widthInt = (int) (width / 32.5);
            if (lengthInt < widthInt) {
                int temp = lengthInt;
                lengthInt = widthInt;
                widthInt = temp;
            }

            /*****09040130试图修改*****/
            int legosize = -1;
            if(widthInt==1 && lengthInt==2){legosize = 0;}
            if(widthInt==1 && lengthInt==4){legosize = 1;}
            if(widthInt==2 && lengthInt==2){legosize = 2;}
            if(widthInt==2 && lengthInt==3){legosize = 3;}
            if(widthInt==2 && lengthInt==4){legosize = 4;}
            if (legosize!=-1) {
                color = 3;
                int num = color+legosize*7;
                sheet[color+legosize*7] = sheet[color+legosize*7] + 1;
                legoList.get(color+legosize*7).setLegoNumber(sheet[color+legosize*7]);
            }

//            if (true) return imgsource;
//            int contourSize = (int) temp_contour.total();
//            MatOfPoint2f approxCurve_temp = new MatOfPoint2f();
            //对图像轮廓点进行多边形拟合
//            Imgproc.approxPolyDP(new_mat, approxCurve_temp, contourSize * 0.05, true);
//            approxCurve_temp = new_mat;
//            if (approxCurve_temp.total() == 4||true) {
//                if (isRectangle(approxCurve_temp)) {
//                    approxCurve = approxCurve_temp;
//                    double[] temp_double;
//
//                    for (int i = 0; i < 4; i++) {
//                        temp_double = approxCurve.get(i, 0);
//                        if (temp_double == null) return imgsource;
//                        source.add(new Point(temp_double[0], temp_double[1]));
//                    }
//                    //对4个点进行排序
//                    Point centerPoint = new Point(0, 0);//质心
//                    for (Point corner : source) {
//                        centerPoint.x += corner.x;
//                        centerPoint.y += corner.y;
//                    }
//                    centerPoint.x = centerPoint.x / source.size();
//                    centerPoint.y = centerPoint.y / source.size();
//
//                    Point lefttop = new Point();
//                    Point righttop = new Point();
//                    Point leftbottom = new Point();
//                    Point rightbottom = new Point();
//                    for (int i = 0; i < source.size(); i++) {
//                        if (source.get(i).x < centerPoint.x && source.get(i).y < centerPoint.y) {
//                            lefttop = source.get(i);
//                        } else if (source.get(i).x > centerPoint.x && source.get(i).y < centerPoint.y) {
//                            righttop = source.get(i);
//                        } else if (source.get(i).x < centerPoint.x && source.get(i).y > centerPoint.y) {
//                            leftbottom = source.get(i);
//                        } else if (source.get(i).x > centerPoint.x && source.get(i).y > centerPoint.y) {
//                            rightbottom = source.get(i);
//                        }
//                    }
//                    source.clear();
//                    source.add(lefttop);
//                    source.add(righttop);
//                    source.add(leftbottom);
//                    source.add(rightbottom);
//                    source.add(centerPoint);
//                    colorDetection(source.get(4), imgsource);
////            frame = frameGray;
//                    Imgproc.line(imgsource, source.get(0), source.get(1), new Scalar(0, 255, 0), 5, 8, 0);
//                    Imgproc.line(imgsource, source.get(1), source.get(3), new Scalar(0, 255, 0), 5, 8, 0);
//                    Imgproc.line(imgsource, source.get(2), source.get(0), new Scalar(0, 255, 0), 5, 8, 0);
//                    Imgproc.line(imgsource, source.get(3), source.get(2), new Scalar(0, 255, 0), 5, 8, 0);
////                        length = sqrt((rect.get(0).x - rect.get(1).x) * (rect.get(0).x - rect.get(1).x) +
////                                (rect.get(0).y - rect.get(1).y) * (rect.get(0).y - rect.get(1).y));
////                        width = sqrt((rect.get(0).x - rect.get(2).x) * (rect.get(0).x - rect.get(2).x) +
////                                (rect.get(0).y - rect.get(2).y) * (rect.get(0).y - rect.get(2).y));
////                        Log.i("length", String.valueOf(length));
////                        Log.i("width", String.valueOf(width));
//                }
//            }
////                if (approxCurve_temp.total() == 4 ) {
////                    maxArea = contourarea;
////                    maxAreaIdx = idx;
////                    approxCurve = approxCurve_temp;
////                }
////            }
        }

//        double[] temp_double;
//
//        for (int i = 0; i < 4; i++) {
//            temp_double = approxCurve.get(i, 0);
//            if (temp_double == null) return null;
//            source.add(new Point(temp_double[0], temp_double[1]));
//        }
//        //对4个点进行排序
//        Point centerPoint = new Point(0, 0);//质心
//        for (Point corner : source) {
//            centerPoint.x += corner.x;
//            centerPoint.y += corner.y;
//        }
//        centerPoint.x = centerPoint.x / source.size();
//        centerPoint.y = centerPoint.y / source.size();
//
//        Point lefttop = new Point();
//        Point righttop = new Point();
//        Point leftbottom = new Point();
//        Point rightbottom = new Point();
//        for (int i = 0; i < source.size(); i++) {
//            if (source.get(i).x < centerPoint.x && source.get(i).y < centerPoint.y) {
//                lefttop = source.get(i);
//            } else if (source.get(i).x > centerPoint.x && source.get(i).y < centerPoint.y) {
//                righttop = source.get(i);
//            } else if (source.get(i).x < centerPoint.x && source.get(i).y > centerPoint.y) {
//                leftbottom = source.get(i);
//            } else if (source.get(i).x > centerPoint.x && source.get(i).y > centerPoint.y) {
//                rightbottom = source.get(i);
//            }
//        }
//        source.clear();
//        source.add(lefttop);
//        source.add(righttop);
//        source.add(leftbottom);
//        source.add(rightbottom);
//        source.add(centerPoint);
//        return source;
        return imgsource;
    }

    private int colorDetection(List<Point> source, Mat imgsource) {
        return -1;
    }

    private boolean isRectangle(MatOfPoint2f approxCurve_temp) {
        Point p0 = new Point(approxCurve_temp.get(0, 0)[0], approxCurve_temp.get(0, 0)[1]);
        Point p1 = new Point(approxCurve_temp.get(1, 0)[0], approxCurve_temp.get(1, 0)[1]);
        Point p2 = new Point(approxCurve_temp.get(2, 0)[0], approxCurve_temp.get(2, 0)[1]);
        Point p3 = new Point(approxCurve_temp.get(3, 0)[0], approxCurve_temp.get(3, 0)[1]);
//        double angle1 = getAngle(p1, p2, p0);
//        double angle2 = getAngle(p0, p3, p1);
//        double angle3 = getAngle(p1, p2, p3);
//        double angle4 = getAngle(p0, p3, p2);
//        Log.i("angle",String.valueOf(angle1));
//        Log.i("angle",String.valueOf(angle2));
//        Log.i("angle",String.valueOf(angle3));
//        Log.i("angle",String.valueOf(angle4));
        if (getAngle(p1, p2, p0) > 0.65 && getAngle(p0, p3, p1) > 0.65 && getAngle(p1, p2, p3) > 0.65 && getAngle(p0, p3, p2) > 0.65)
            return true;
        return false;
    }

    private double getAngle(Point pt1, Point pt2, Point pt0) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
    }


}