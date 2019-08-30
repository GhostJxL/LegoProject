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
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

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
        frame = rectangularDetection(frame); //矩形检测
        return frame;
    }

    private Mat rectangularDetection(Mat frame) {
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGRA2GRAY); //转为灰度图
        Imgproc.Canny(frame,frame,200,400,3,false); //阈值可调
        Mat hierachy = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//        Imgproc.findContours(frame,contours,hierachy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_NONE);
        
        return frame;
    }

}