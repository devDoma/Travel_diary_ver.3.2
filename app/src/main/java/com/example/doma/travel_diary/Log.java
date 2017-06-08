package com.example.doma.travel_diary;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class Log extends BaseActivity {

    ListView listView;
    MyAdapter adapter = new MyAdapter();
    String dateToday = null;
    Boolean visiblity_button = false;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private boolean isFragmentChange = true;
    Fragment fragment_calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);//액티비티 를 보여줌

        final Animation anima_showing= AnimationUtils.loadAnimation(this, R.anim.showing);
        final Animation anim_disappearing = AnimationUtils.loadAnimation(this, R.anim.disappearing);
        final Animation anim_rotate = AnimationUtils.loadAnimation(this, R.anim.click_rotate_zooming);


        adapter.loadArrayListPreferences("file", "key", this);

        GregorianCalendar today = new GregorianCalendar ( );
        int year = today.get ( today.YEAR );
        int month = today.get ( today.MONTH ) + 1;
        int yoil = today.get ( today.DAY_OF_MONTH );

        adapter.reorderArrayList(year,month,yoil);//왜 처음부터 나오지 않지?


        listView = (ListView) findViewById(R.id.list_log);
        listView.setAdapter(adapter);// 리스트뷰와 어댑터 연결.

//        BackThread thread = new BackThread();
//        thread.setDaemon(true);
//        thread.start();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment_calendar = fm.findFragmentById(R.id.fragment);
        ft.hide(fm.findFragmentById(R.id.fragment));
        ft.commit();

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView2);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String newMonth;
                if (month + 1 < 10) {
                    newMonth = "0" + String.valueOf(month + 1);
                } else {
                    newMonth = String.valueOf(month + 1);
                }
                adapter.reorderArrayList(year,month+1,dayOfMonth);

                String newDay;
                if (dayOfMonth < 10) {
                    newDay = "0" + String.valueOf(dayOfMonth);
                } else {
                    newDay = String.valueOf(dayOfMonth);
                }

                dateToday = String.valueOf(year) + "." + newMonth + "." + newDay;

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(fragmentManager.findFragmentById(R.id.fragment));
                fragmentTransaction.commit();
                Button button = (Button) findViewById(R.id.fraghide);
                button.setText("▼  " + dateToday);


            }
        });

        addShowHideListener(R.id.fraghide, fragment_calendar);

        Button button_Frag = (Button) findViewById(R.id.fraghide);
        button_Frag.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switchFragment();
                return false;
            }
        });


        // SharedPreferences pref = getSharedPreferences("file", MODE_PRIVATE);


//        if (getIntent().getSerializableExtra("adapterFromWrite") != null) {
//            Intent intent = getIntent();
//            adapter = (MyAdapter) intent.getSerializableExtra("adapterFromWrite");
//        }


        final ImageButton btAdd = (ImageButton) findViewById(R.id.button_add);
        btAdd.setVisibility(View.INVISIBLE);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Log.this, Write.class);
                intent.putExtra("adapter", adapter);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


        final ImageButton btPlay = (ImageButton) findViewById(R.id.imageButton_play);
        btPlay.setVisibility(View.INVISIBLE);
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Log.this, ShowLog.class);
                intent.putExtra("adapter", adapter);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });



        final ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton_grid);
        imageButton.setVisibility(View.INVISIBLE);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Log.this, ImageGridView.class);
                startActivity(intent);
            }
        });

        final ImageButton btCentre = (ImageButton) findViewById(R.id.imageButton_center);//// 우측 하단 버튼 동작 정의..
        btCentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visiblity_button = !(visiblity_button);
                if (visiblity_button) {//버튼을 보이고자 할때
                    btCentre.setImageResource(R.drawable.button1);
                    btCentre.startAnimation(anim_rotate);

                    imageButton.startAnimation(anima_showing);
                    btAdd.startAnimation(anima_showing);
                    btPlay.startAnimation(anima_showing);
                    imageButton.setVisibility(View.VISIBLE);
                    btAdd.setVisibility(View.VISIBLE);
                    btPlay.setVisibility(View.VISIBLE);
                } else {
                    btCentre.setImageResource(R.drawable.button);
                    btCentre.startAnimation(anim_rotate);

                    imageButton.startAnimation(anim_disappearing);
                    btAdd.startAnimation(anim_disappearing);
                    btPlay.startAnimation(anim_disappearing);

                    imageButton.setVisibility(View.INVISIBLE);
                    btAdd.setVisibility(View.INVISIBLE);
                    btPlay.setVisibility(View.INVISIBLE);
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        new CounterTask().execute(0);
    }

    //
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Toast.makeText(this, "1번탭 onStart()", Toast.LENGTH_SHORT).show();
//    }
//
    @Override
    protected void onPause() {
        super.onPause();
        adapter.saveArrayListPreferences("file", "key", Log.this);
//        Intent intent =  new Intent(this, Write.class);
//

    }

    void addShowHideListener(int buttonId, final Fragment fragment) {
        final Button button = (Button) findViewById(buttonId);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        Date currentTime = new Date();
        final String dTime = formatter.format(currentTime);

        button.setText("▼  " + dTime);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (dateToday == null) {
                    dateToday = dTime;
                }

                if (fragment.isHidden()) {
                    ft.show(fragment); // Fragment 표시
                    button.setText("▲  " + dateToday);
                } else {
                    ft.hide(fragment); // Fragment 감추기
                    button.setText("▼  " + dateToday);
                }
                ft.commit();
            }
        });
    }

//    class BackThread extends Thread {
//        @Override
//        public void run() {
//            while (true) {
//                handler.sendEmptyMessage(0);
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } // end while
//        } // end run()
//    } // end class BackThread

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 0) {//message ID가 0이면
//                adapter.notifyDataSetChanged();
//                adapter.getAdapter(adapter);
//            }
//        }
//    };
    class CounterTask extends AsyncTask<Integer, Integer, Integer> {
        protected void onPreExecute(){
            mProgress = (ProgressBar) findViewById(R.id.progressBar);
            mProgress.setVisibility(View.VISIBLE);

        }
        protected Integer doInBackground(Integer... value){
//            while (mProgressStatus < 100){
//                try {	Thread.sleep(50);
//                } catch( InterruptedException e){
//                }
//                mProgressStatus++;
//                publishProgress(mProgressStatus);
//            }
            adapter.loadArrayListPreferences("file","key", Log.this);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mProgressStatus;
        }
        protected void onPostExecute(Integer result){
            mProgress.setVisibility(View.INVISIBLE);
        }
    }

    public void switchFragment(){
        Fragment fragment;
        if(isFragmentChange){
            fragment = new ListFragment_Destination();
        }else{
                    fragment = fragment_calendar;
        }

        isFragmentChange = (isFragmentChange)? false : true;

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentTransaction.commit();
    }

}


