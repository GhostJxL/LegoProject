package com.legoproject.lego;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView tv01,tv02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv01 = findViewById(R.id.main_camera);
        tv02 = findViewById(R.id.main_photo);

        //添加监听器
        MyListener ml = new MyListener();
        tv01.setOnClickListener(ml);
        tv02.setOnClickListener(ml);

    }

    class MyListener implements View.OnClickListener {
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case (R.id.main_camera):
                    Toast.makeText(MainActivity.this, "successful", Toast.LENGTH_SHORT).show();
                    scan();
                    break;
                case (R.id.main_photo):
                    photobook();
                    break;
            }
        }
    }

    public void scan(){
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        startActivity(intent);//跳转
        finish();
    }

    public void photobook(){

    }


}
