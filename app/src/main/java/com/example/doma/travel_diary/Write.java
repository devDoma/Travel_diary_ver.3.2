package com.example.doma.travel_diary;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Write extends BaseActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;


    private Uri mImageCaptureUri;
    private Uri mImageUri;
    private String Uri_string;

    EditText mMemoEdit = null;
    EditText mTitleEdit = null;
    int index_modify = -1;

    // 이전에 기록에서부터 불러와야하는데 어떻게 하지...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        mImageUri = null;

        Intent intent = getIntent();
        final MyAdapter adapter = new MyAdapter();
        adapter.loadArrayListPreferences("file","key", this);

        boolean modify = false;

        final ArrayList<Data_Log> itemList = adapter.getItemList();

        mMemoEdit = (EditText) findViewById(R.id.editText_Write);
        if (getIntent().getIntExtra("index", -1) != -1) {
            modify = true;
        }

        mTitleEdit = (EditText) findViewById(R.id.editText_title);


        if (modify) {//수정했을 시 자료를 받아와서 write 액티비티에서 보여주는 작업.

            index_modify = intent.getIntExtra("index", -1);

            mImageUri = Uri.parse(itemList.get(index_modify).getImage_Uri());


            ImageView imageView = (ImageView) findViewById(R.id.imageView_photo);
            imageView.setImageURI(mImageUri);

            String content = itemList.get(index_modify).getmContent();
            mMemoEdit.setText(content, TextView.BufferType.EDITABLE);

            String title = itemList.get(index_modify).getTitle();
            mTitleEdit.setText(title, TextView.BufferType.EDITABLE);
        }


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        LinearLayout ll = (LinearLayout) findViewById(R.id.Layout_choosePhoto);
        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doTakePhotoAction();
                            }
                        };

                        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                doTakeAlbumAction();

                            }
                        };

                        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        };

                        alertDialog.setTitle("업로드할 이미지 선택");
                        alertDialog.setPositiveButton("사진촬영", cameraListener);
                        alertDialog.setNeutralButton("앨범선택", albumListener);
                        alertDialog.setNegativeButton("취소", cancelListener);
                        alertDialog.show();

                        break;
                    }


                    default:
                        break;
                }
                return false;
            }


        });

        ImageButton btSave = (ImageButton) findViewById(R.id.button_Save);
        final boolean finalModify = modify;
        btSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String mMemo = mMemoEdit.getText().toString();
                String mTitle = mTitleEdit.getText().toString();


                if (Uri_string == null) {
                    Toast.makeText(Write.this, "사진을 선택해주세요", Toast.LENGTH_LONG).show();
                } else {

                    String currentDateString = DateFormat.getDateInstance().format(new Date());
                    String currnetTimeString = DateFormat.getTimeInstance().format(new Date());

                    if (finalModify) {
                        if (Uri_string == null) {
                            Uri_string = mImageUri.toString();
                        }

                        adapter.modifyItem(Uri_string, mTitle, mMemo, index_modify);
                        adapter.saveArrayListPreferences("file", "key", Write.this);
                        adapter.notifyDataSetChanged();
                    } else {
//                        int i =0;
//                        while(true){
//                        adapter.addItem(Uri_string, mTitle, mMemo+String.valueOf(i), currentDateString, currnetTimeString);
//                        if(i >100){
//                            break;
//                        }
//                            i++;
//                        }//임의로 데이터를 늘리기 위한 과정
                        adapter.addItem(Uri_string, mTitle, mMemo, currentDateString, currnetTimeString);
                        adapter.saveArrayListPreferences("file", "key", Write.this);
                        adapter.notifyDataSetChanged();
                    }

                    Intent intent = new Intent(Write.this, Log.class);
                    intent.putExtra("adapterFromWrite", adapter);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    Toast.makeText(Write.this, "저장완료", Toast.LENGTH_LONG).show();
                }
            }

        });


    }


    public void doTakePhotoAction() {//사진찍어 이미지 가져오기

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = "tmp_" + String.valueOf(System.currentTimeMillis() + ".jpg");//현재 시간을 가져와서 "tmp_현재시간.jpg"라는 임시  URL  생성
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void doTakeAlbumAction() {//앨범 불러오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA: {
                // 이미지를 받아와서 crop된 이미지를 새로 저장.

                final Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ImageView image = (ImageView) findViewById(R.id.imageView_photo);
                    image.setImageBitmap(photo);
                }

//                // 임시 파일 삭제
//                File f = new File(mImageCaptureUri.getPath());
//                if(f.exists())
//                {
//                    f.delete();
//                }
                Uri_string = String.valueOf(mImageCaptureUri);

                break;
//
//                final Bundle extras = data.getExtras();
//                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/" + System.currentTimeMillis() + ".jpg";
//
//
//                if (extras != null) {
//                    Bitmap photo = extras.getParcelable("data");//crop 된 bitmap 파일을 저장.
//
//
//                    ImageView image = (ImageView) findViewById(R.id.imageView_photo);
//                    image.setImageURI(Uri.parse(Uri_string));
//
//
//                }
//
//                // 임시 파일 삭제
//                File f = new File(mImageCaptureUri.getPath());
//                if (f.exists()) {
//                    f.delete();
//                }
//
//                Uri_string = filePath;
//
//                break;
            }

            case PICK_FROM_ALBUM: {
                try {

                    //이미지 데이터를 비트맵으로 받아온다.

                    mImageUri = data.getData();
                    Uri_string = mImageUri.toString();

                    //배치해놓은 ImageView에 set
                    final ImageView image = (ImageView) findViewById(R.id.imageView_photo);
                    image.setImageURI(mImageUri);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case PICK_FROM_CAMERA: {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출.

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }
        }
    }
//    private void saveObjects(String file_name, String key, Data_Log item){
//        SharedPreferences pref = getSharedPreferences(file_name, MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        String wow = item.getTime() + ", " + item.getTitle() + ", " + item.getImage_Uri() + ", " + item.getmContent() + ", " + item.getDate();
//        editor.putString(key, wow);
//        editor.commit();
//    }

//    private void removePrefences(String file_name, String key) {//값(Key Data) 삭제하기
//        SharedPreferences pref = getSharedPreferences(file_name, MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.remove(key);
//        editor.commit();
//    }
//
//    private void removeAllPreferences(String file_pref) {
//        SharedPreferences pref = getSharedPreferences(file_pref, MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.clear();
//        editor.commit();
//    }


}
