package com.example.doma.travel_diary;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by seungyunk on 2017-02-23.
 */

public class CustomDialog_Destination extends Dialog {

    private Context mContext;


    public CustomDialog_Destination(Context context) {

        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final adapter_Destination adapter_destination = new adapter_Destination();

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.custom_dialog_destination);


        ListView listView = (ListView) findViewById(R.id.listview_custiomDialog);
        listView.setAdapter(adapter_destination);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;//흐릿한 정도

        //다이얼로그 크기 설정

        lpWindow.copyFrom(getWindow().getAttributes());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = getWindow().getContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        lpWindow.width = (int) Math.round(width * 0.9);
        lpWindow.height = (int) Math.round(height * 0.7);
        getWindow().setAttributes(lpWindow);

        ImageButton imageButton_plus = (ImageButton) findViewById(R.id.imageButton_plus_dialog_d);
        imageButton_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText_dest);
                String destination = String.valueOf(editText.getText());

                adapter_destination.getDestinationList().add(destination);
                adapter_destination.notifyDataSetChanged();
            }
        });


    }
}
