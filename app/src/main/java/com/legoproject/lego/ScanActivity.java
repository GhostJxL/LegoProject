package com.legoproject.lego;

import android.Manifest;
import android.content.Context;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import static java.lang.Math.sqrt;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.COLOR_BGRA2BGR;

public class ScanActivity extends AppCompatActivity implements CvCameraViewListener2 {


    private JavaCameraView myCamera;
    private Button button;

    private List<Lego> legoList = new ArrayList<>();
    private int[] sheet = new int[31];
    private int legosize;
    private String[] colorToText = {"white", "orange", "light-green", "dark-green", "light-blue", "dark-blue"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示左上角返回键
        init();
//        initLegos();
        final LegoAdapter adapter = new LegoAdapter(ScanActivity.this,
                R.layout.lego_item, legoList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLegos();

                adapter.notifyDataSetChanged();


            }
        });

    }

    private void initLegos() {

        legoList.clear();
        boolean isBlank = true;
        for (int i = 0; i < 31; i++) {
            if (sheet[i] != 0) isBlank = false;
        }
        if (isBlank) return;
        FileOutputStream fos = null;
//        PrintWriter pw=null;
        try {
            fos = openFileOutput("record.history", Context.MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fos);
        pw.println(DateUtil.getDateString(new Date()) + "\n");
        pw.flush();
//        int sheet[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31}; //测试用假数据
        for (int i = 0; i < 30; i++) {
            if (sheet[i] > 0) {
                int color = i % 6;
                int width = 0;
                if (i < 12) width = 1;
                else width = 2;
                int length = 0;
                if ((0 <= i && i <= 5) || (12 <= i && i <= 17)) length = 2;
                else if (18 <= i && i <= 23) length = 3;
                else if ((6 <= i && i <= 11) || (24 <= i && i <= 29)) length = 4;
                Lego lego = new Lego(color, width, length, sheet[i], false);
                legoList.add(lego);
                pw = new PrintWriter(fos);
                pw.println(colorToText[color] + "  " + String.valueOf(width) + " X "
                        + String.valueOf(length) + " Lego Board" + "  X" + String.valueOf(sheet[i]) + "\n");
                pw.flush();
            }
        }
        if (sheet[30] > 0) {
            Lego damage = new Lego(0, 0, 0, sheet[30], true);
            legoList.add(damage);
            pw = new PrintWriter(fos);
            if (sheet[30] == 1) {
                pw.println("Damaged Board" + "  X" + String.valueOf(sheet[30]) + "\n");
            }
            if (sheet[30] > 1) {
                pw.println("Damaged Boards" + "  X" + String.valueOf(sheet[30]) + "\n");
            }
            pw.flush();
        }
        try {
            fos.close();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        sheet = new int[31];
        return detection(frame); //检测
    }

    public Mat detection(Mat imgsource) {
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
        MatOfPoint temp_contour;
        if (contours.size() == 0) return imgsource;
        for (int idx = 0; idx < contours.size(); idx++) {
            temp_contour = contours.get(idx);

            double contourarea = Imgproc.contourArea(temp_contour); //计算轮廓面积

            if (contourarea > 1000 && contourarea < 9000) { //1000为1X1乐高积木的面积大小

                RotatedRect rect = Imgproc.minAreaRect(new MatOfPoint2f(temp_contour.toArray())); //最小旋转矩形拟合
                Point vertices[] = new Point[4];//得到矩形的四个顶点
                rect.points(vertices);
                if (vertices[0] == new Point(0, 0) ||
                        vertices[1] == new Point(0, 0) ||
                        vertices[2] == new Point(0, 0) ||
                        vertices[3] == new Point(0, 0))
                    continue;

                for (int i = 0; i < 4; i++)
                    Imgproc.line(imgsource, vertices[i], vertices[(i + 1) % 4], new Scalar(255, 0, 0), 2); //绘制边框，红色边框标定


                MatOfPoint minRect = new MatOfPoint(vertices);
                double minrectarea = Imgproc.contourArea(minRect);
                if (contourarea / minrectarea < 0.90) {//判断是否破损
                    sheet[30]++;
                    continue;
                }

                Point centerPoint = new Point(0, 0);//计算质心坐标
                for (int i = 0; i < 4; i++) {
                    centerPoint.x += vertices[i].x;
                    centerPoint.y += vertices[i].y;
                }
                centerPoint.x = centerPoint.x / 4;
                centerPoint.y = centerPoint.y / 4;

                //顶点排序
                Point lefttop = new Point();
                Point righttop = new Point();
                Point leftbottom = new Point();
                Point rightbottom = new Point();
                for (int i = 0; i < 4; i++) {
                    if (vertices[i].x < centerPoint.x && vertices[i].y < centerPoint.y) {
                        lefttop = vertices[i];
                    } else if (vertices[i].x > centerPoint.x && vertices[i].y < centerPoint.y) {
                        righttop = vertices[i];
                    } else if (vertices[i].x < centerPoint.x && vertices[i].y > centerPoint.y) {
                        leftbottom = vertices[i];
                    } else if (vertices[i].x > centerPoint.x && vertices[i].y > centerPoint.y) {
                        rightbottom = vertices[i];
                    }
                }
                List<Point> source = new ArrayList<>();
                source.add(lefttop);
                source.add(righttop);
                source.add(leftbottom);
                source.add(rightbottom);
                source.add(centerPoint);

                int color = -1;
                color = colorDetection(source, imgsource); //颜色检测

                //尺寸计算
                double length = sqrt((source.get(0).x - source.get(1).x) * (source.get(0).x - source.get(1).x) +
                        (source.get(0).y - source.get(1).y) * (source.get(0).y - source.get(1).y));
                double width = sqrt((source.get(0).x - source.get(2).x) * (source.get(0).x - source.get(2).x) +
                        (source.get(0).y - source.get(2).y) * (source.get(0).y - source.get(2).y));
                int lengthInt = (int) (length / 31);
                int widthInt = (int) (width / 31);
                Log.i("length", String.valueOf(lengthInt));
                Log.i("width", String.valueOf(widthInt));
                if (lengthInt < widthInt) {
                    int temp = lengthInt;
                    lengthInt = widthInt;
                    widthInt = temp;
                }
//                color = 3; //测试用
                int legosize = -1;
                if (widthInt == 1 && lengthInt == 2) {
                    legosize = 0;
                }
                if (widthInt == 1 && lengthInt == 4) {
                    legosize = 1;
                }
                if (widthInt == 2 && lengthInt == 2) {
                    legosize = 2;
                }
                if (widthInt == 2 && lengthInt == 3) {
                    legosize = 3;
                }
                if (widthInt == 2 && lengthInt == 4) {
                    legosize = 4;
                }
                if (color != -1 && legosize != -1) {
                    sheet[color + 6 * legosize]++;
//                    for (int i = 0; i < 4; i++)
//                        Imgproc.line(imgsource, vertices[i], vertices[(i + 1) % 4], new Scalar(247, 217, 76), 2); //绘制边框，黄色边框标定

                }

            }
        }
        return imgsource;
    }

    public static double RGBtoHSV(double r, double g, double b) {

        double h, s, v;

        double min, max, delta;

        min = Math.min(Math.min(r, g), b);
        max = Math.max(Math.max(r, g), b);

        // V
        v = max;

        delta = max - min;

        // S
        if (max != 0)
            s = delta / max;
        else {
            s = 0;
            h = -1;
            return h;
        }

        // H
        if (r == max)
            h = (g - b) / delta; // between yellow & magenta
        else if (g == max)
            h = 2 + (b - r) / delta; // between cyan & yellow
        else
            h = 4 + (r - g) / delta; // between magenta & cyan

        h *= 60;    // degrees

        if (h < 0)
            h += 360;

        return h;
    }


    private int colorDetection(List<Point> source, Mat imgsource) {
        Imgproc.GaussianBlur(imgsource, imgsource, new Size(3, 3), 2, 2); //高斯滤波降噪
        //Mat hsv = new Mat(imgsource.size(), CvType.CV_8UC3);
        Imgproc.cvtColor(imgsource, imgsource, COLOR_BGRA2BGR);
        // Imgproc.cvtColor(hsv, hsv, COLOR_BGR2HSV);

        Point[] points = new Point[5];
        for (int i = 0; i < 5; i++) {
            points[i] = source.get(i);
        }
        points[0].x = points[0].x + 10;
        points[0].y = points[0].y + 10;
        points[1].x = points[1].x - 10;
        points[1].y = points[1].y + 10;
        points[2].x = points[2].x + 10;
        points[2].y = points[2].y - 10;
        points[3].x = points[3].x - 10;
        points[3].y = points[3].y - 10;

        double[] temp;
        double h = 0, r = 0, g = 0, b = 0;
        for (int i = 0; i < 5; i++) {


            temp = imgsource.get((int) points[i].y, (int) points[i].x).clone();
            r = temp[0] / 5 + r;
            g = temp[1] / 5 + g;
            b = temp[2] / 5 + b;
            h = RGBtoHSV(temp[0], temp[1], temp[2]) / 5 + h;
            // temp = hsv.get((int) points[i].y, (int) points[i].x).clone();
            // temp = imgsource.get((int) points[i].y, (int) points[i].x).clone();
            //  r = temp[0] / 5 + r;
            //  g = temp[1] / 5 + g;
            // b = temp[2] / 5 + b;
        }

        if (r > 200 && g > 200 && b > 200) return 0;
        if (0 < h && h < 50) return 1;
        else if (50 < h && h < 130) return 2;
        else if (132 < h && h < 150) return 3;
        else if (170 < h && h < 200) return 4;
        else if (205 < h && h < 220) return 5;
        return -1;


    }

}