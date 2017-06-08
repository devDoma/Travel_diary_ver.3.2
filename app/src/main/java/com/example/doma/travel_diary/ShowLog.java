package com.example.doma.travel_diary;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShowLog extends BaseActivity{

    ImageView imageView;
    TextView textView;
    TextView textView_title;
    ImageButton imageButton;
    ArrayList<Data_Log> itemList;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log);

        textView_title = (TextView) findViewById(R.id.textView_title);
        textView =(TextView) findViewById(R.id.textView_content);
        imageButton = (ImageButton) findViewById(R.id.imageButton_screen);
        imageView = (ImageView) findViewById(R.id.imageView_screen);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                MyAdapter adapter = (MyAdapter) intent.getSerializableExtra("adapter");
                itemList = adapter.getItemList_Calender();

                BackThread thread = new BackThread();//메인 스레드의 핸들러 객체를 외부 클래스에 넣어줌.
                thread.setDaemon(true);
                thread.start();

            }
        });

    }


    class BackThread extends Thread {


        @Override
        public void run() {
            int i = 0;
            flag = true;
            while (flag) {
                if (i > itemList.size()-1) {
                    break;
                }
                final String title = itemList.get(i).getTitle();
                final String uri = itemList.get(i).getImage_Uri();
                final String content = itemList.get(i).getmContent();


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageButton.setVisibility(View.INVISIBLE);
                        Glide.with(ShowLog.this).load(Uri.parse(uri)).into(imageView);
//                      imageView.setImageURI(Uri.parse(uri));
                        textView.setText(content);
                        textView_title.setText(title);
                    }
                });

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageButton.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    Handler handler = new Handler();

    @Override
    protected void onPause() {
        super.onPause();
        flag = false;
    }
}


