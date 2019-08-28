package com.legoproject.lego;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        boolean success = OpenCVLoader.initDebug();
        Bitmap bitmap = BitmapFactory.decodeResource(
                this.getResources(), R.drawable.test
        );
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,dst,Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dst,bitmap);
        ImageView iv = this.findViewById(R.id.scan_img);
        iv.setImageBitmap(bitmap);
        src.release();
        dst.release();
    }
}
