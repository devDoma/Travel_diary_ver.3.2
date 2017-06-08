package com.example.doma.travel_diary;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.IMediaControllerCallback;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by seungyunk on 2017-02-22.
 */

public class CustomDialog extends Dialog {

    private Context mContext;
    private TextView mTitleView;
    private TextView mContentView;
    private TextView mDateView;
    private ImageView mImageView;
    private String mTitle;
    private String mContents;
    private Uri mUri;
    private String mDate;

    public CustomDialog(Context context, String title, String content, Uri uri, String date) {
        super(context);
        mTitle = title;
        mContents = content;
        mUri = uri;
        mContext = context;
        mDate = date;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cutom_dialog_view);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;//흐릿한 정도

        //다이얼로그 크기 설정

        lpWindow.copyFrom(getWindow().getAttributes());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        lpWindow.width = (int) Math.round(width*0.9);
        lpWindow.height = (int) Math.round( height*0.7);
        getWindow().setAttributes(lpWindow);



        mTitleView = (TextView) findViewById(R.id.textView_DialogTitle);
        mContentView = (TextView) findViewById(R.id.textView_DialogContent);
        mImageView = (ImageView) findViewById(R.id.imageView_Dialog);
        mDateView = (TextView) findViewById(R.id.textView_DialogDate);

        mTitleView.setText(mTitle);
        mContentView.setText(mContents);
        mImageView.setImageURI(mUri);
        mDateView.setText(mDate);

        mContentView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/byulbitcha.ttf"));
        mTitleView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/byulbitcha.ttf"));
        mDateView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/byulbitcha.ttf"));


    }
}
