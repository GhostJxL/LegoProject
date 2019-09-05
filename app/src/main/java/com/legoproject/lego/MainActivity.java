package com.legoproject.lego;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {



    private TextView tv02,tv03;

    private RelativeLayout tv01;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        tv01 = findViewById(R.id.main_cameralayout);

        tv02 = findViewById(R.id.main_photo);

        tv03 = findViewById(R.id.main_history);



        //添加监听器

        MyListener ml = new MyListener();

        tv01.setOnClickListener(ml);

        tv02.setOnClickListener(ml);

        tv03.setOnClickListener(ml);



    }





    class MyListener implements View.OnClickListener {

        public void onClick(View arg0) {

            switch (arg0.getId()) {

                case (R.id.main_cameralayout):

                    scan();

                    break;

                case (R.id.main_photo):

                    photobook();

                    break;

                case (R.id.main_history):

                    showhistory();

                    break;

            }

        }

    }



    public void scan(){

        Intent intent = new Intent(MainActivity.this, ScanActivity.class);

        startActivity(intent);//跳转至扫描界面

//        finish();

    }



    public void photobook(){



    }



    public void showhistory(){

        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);

        startActivity(intent);//跳转至识别历史记录界面

//        finish();

    }





}

