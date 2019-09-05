package com.legoproject.lego;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class HistoryActivity extends AppCompatActivity {

    private TextView history_data, history_clean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示左上角返回键

        history_data = findViewById(R.id.history_data);
        history_clean = findViewById(R.id.history_clean);

        MyListener m3 = new HistoryActivity.MyListener();
        history_clean.setOnClickListener(m3);

        readHistory();

    }

    private void readHistory() {
        FileInputStream fis = null;
        BufferedReader br=null;
        try {
            fis=openFileInput("record.history");
            br=new BufferedReader(new InputStreamReader(fis));
            String ln;
            try {
                while( (ln=br.readLine())!=null ){history_data.append("\n"+ln);}
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                if(br!=null)br.close();
                if(fis!=null)fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class MyListener implements View.OnClickListener {
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case (R.id.history_clean):
                    cleanHistory();
                    break;}
        }
    }

    private void cleanHistory() {
        File f=new File(getFilesDir(), "record.history");
        if(f.exists()) {
            f.delete();
            Toast.makeText(HistoryActivity.this, "历史记录已清空", Toast.LENGTH_SHORT).show();
            history_data.setText("");
        }
    }
}
