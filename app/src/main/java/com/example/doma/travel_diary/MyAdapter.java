package com.example.doma.travel_diary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.provider.ChildLoadProvider;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Doma on 2017-01-24.
 */


public class MyAdapter extends BaseAdapter implements Serializable {

    private ArrayList<Data_Log> listViewItemList = new ArrayList<>();
    private ArrayList<Data_Log> ItemList_Calender = new ArrayList<>();
    ArrayList<Integer> WhereRUfrom = new ArrayList<>();

    MyAdapter madapter;


    @Override
    public int getCount() {
        return ItemList_Calender.size();
    }

    @Override
    public Object getItem(int position) {
        return ItemList_Calender.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = getCount() - position - 1;
        final Context context = parent.getContext();
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            //뷰홀더로 세팅하는 부분.
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.imageView_list_item);
            viewHolder.content = (TextView) convertView.findViewById(R.id.textView_list_item);
            viewHolder.title = (TextView) convertView.findViewById(R.id.textView_list_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//      글라이드를 이용한 이미지 로딩... 너무 사기라서 일단 패쓰..
        Glide.with(context).load(Uri.parse(ItemList_Calender.get(pos).getImage_Uri())).bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).into(viewHolder.icon);
//        Glide.with(context).load(Uri.parse(listViewItemList.get(pos).getImage_Uri())).into(viewHolder.icon);

//        viewHolder.position = WhereRUfrom.get(pos);
//        new ThumnailTask(WhereRUfrom.get(pos), viewHolder, context, Uri.parse(listViewItemList.get(WhereRUfrom.get(pos)).getImage_Uri())).execute(null, null);

//        viewHolder.icon.setImageBitmap(imageManager.decodeSampleBitmapFromURI(context,Uri.parse(listViewItemList.get(pos).getImage_Uri() ),100,100));
        viewHolder.content.setText(ItemList_Calender.get(pos).getmContent());
        viewHolder.title.setText(ItemList_Calender.get(pos).getTitle());
        viewHolder.content.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/byulbitcha.ttf"));
        viewHolder.title.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/byulbitcha.ttf"));


        ImageButton imageButton_del = (ImageButton) convertView.findViewById(R.id.imageButton_delete);
        imageButton_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder
                        .setTitle("삭제")
                        .setMessage("정말 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int Position = WhereRUfrom.get(pos);
                                ItemList_Calender.remove(pos);
                                listViewItemList.remove(Position);
                                removePrefences("file", "key" + Position, context);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        ImageButton imageButton_mod = (ImageButton) convertView.findViewById(R.id.imageButton_modify);
        imageButton_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Write.class);
                int Position = WhereRUfrom.get(pos);
                intent.putExtra("index", Position);
                intent.putExtra("adapter", madapter);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        });

        ImageButton imageButton_add_De = (ImageButton) convertView.findViewById(R.id.imageButton_setDes);
        imageButton_add_De.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog_Destination customDialog_destination = new CustomDialog_Destination(context);
                customDialog_destination.show();
            }
        });



        return convertView;
    }

    public void addItem(String image, String title, String mContent, String date, String time) {
        Data_Log item = new Data_Log();// 이러면 계속 새로운 데이터 로그 객체를 생성하는데 그건 괜차늠? ㅇㅇ 임시라서 괜차늠
        item.setImage_Uri(image);
        item.setmContent(mContent);
        item.setDate(date);
        item.setTime(time);
        item.setTitle(title);
        listViewItemList.add(item);
    }

//    public void arragngeList(int year, int month, int day){
//        for(int i = 0; i< getCount(); i++){
//            Data_Log item = listViewItemList.get(i);
//            item.getDate()
//        }
//    }

    public void getAdapter(MyAdapter adapter) {
        madapter = adapter;
    }


    public void modifyItem(String mImage, String title, String mContent, int index) {
        Data_Log item = new Data_Log();
        item.setImage_Uri(mImage);
        item.setmContent(mContent);
        item.setDate(listViewItemList.get(index).getDate());
        item.setTime(listViewItemList.get(index).getTime());
        item.setTitle(title);
        listViewItemList.set(index, item);
    }

    public ArrayList<Data_Log> getItemList() {
        return listViewItemList;
    }

    public void saveArrayListPreferences(String file_name, String key, Context context) {//내부저장 장치에 어레이리스트를 각 행마다 key+index로 저장하는 메소드.


        SharedPreferences pref = context.getSharedPreferences(file_name, MODE_PRIVATE);//"pref" 는 파일 이름

        SharedPreferences.Editor editor = pref.edit();

        for (int i = 0; i < listViewItemList.size(); i++) {
            Data_Log item = listViewItemList.get(i);

            String Image_Uri;
            String Time;
            String Title;
            String Content;
            String Date;

            if (item.getImage_Uri() == null) {
                Image_Uri = " ";
            } else {
                Image_Uri = item.getImage_Uri();
            }
            if (item.getTime() == null) {
                Time = " ";
            } else {
                Time = item.getTime();
            }
            if (item.getTitle() == null) {
                Title = " ";
            } else {
                Title = item.getTitle();
            }
            if (item.getmContent() == null) {
                Content = " ";
            } else {
                Content = item.getmContent();
            }
            if (item.getDate() == null) {
                Date = " ";
            } else {
                Date = item.getDate();
            }


            String wow = Time + "|" + Title + "|" + Image_Uri + "|" + Content + "|" + Date;
            String index = String.valueOf(i);
            String newKey = key + index;
            editor.putString(newKey, wow);
            editor.commit();
        }

    }

    public void loadArrayListPreferences(String file_name, String key, Context context) {
        SharedPreferences pref = context.getSharedPreferences(file_name, MODE_PRIVATE);//"pref" 는 파일 이름

        ArrayList<Data_Log> itemList = new ArrayList<>();
        int i = 0;
        while (true) {
            String index = String.valueOf(i);
            String newKey = key + index;
            String stringLine = pref.getString(newKey, null);

            if (stringLine == null) {
                break;
            }

            StringTokenizer st = new StringTokenizer(stringLine, "|");


            String[] a = new String[st.countTokens()];

            int j = 0;
            while (st.hasMoreTokens()) {


                a[j] = st.nextToken();
                if (j == 2) {
                    a[j] = a[j].replaceAll(" ", "");
                }
                j++;
            }
            if (j == 5) {
                Data_Log data_log = new Data_Log();
                data_log.setTime(a[0]);
                data_log.setTitle(a[1]);
                data_log.setImage_Uri(a[2]);
                data_log.setmContent(a[3]);
                data_log.setDate(a[4]);
                itemList.add(data_log);
            }

            i++;
        }
        listViewItemList = itemList;

    }

    private void removeAllPreferences(String file_pref, Context context) {
        SharedPreferences pref = context.getSharedPreferences(file_pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public ArrayList<Data_Log> getItemList_Calender() {
        return ItemList_Calender;
    }

    private void removePrefences(String file_name, String key, Context context) {//값(Key Data) 삭제하기
        SharedPreferences pref = context.getSharedPreferences(file_name, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    public void reorderArrayList(int year, int month, int day) {
        ItemList_Calender.clear();
        WhereRUfrom.clear();

        for (int i = 0; i < getItemList().size(); i++) {

            String Date_string = getItemList().get(i).getDate();

            StringTokenizer st = new StringTokenizer(Date_string, ".");


            String[] a = new String[st.countTokens()];

            int j = 0;
            while (st.hasMoreTokens()) {
                a[j] = st.nextToken();
                a[j] = a[j].replaceAll(" ", "");
                j++;
            }

            if ((Integer.valueOf(a[0]) == year) && (Integer.valueOf(a[1]) == month) && (Integer.valueOf(a[2]) == day)) {
                ItemList_Calender.add(getItemList().get(i));
                WhereRUfrom.add(i);

            }

        }
    }

    private static class ThumnailTask extends AsyncTask<Object, Void, Bitmap> {
        private int mPosition;
        private ViewHolder mHolder;
        private Context mContext;
        private Uri mUri;

        public ThumnailTask(int position, ViewHolder holder, Context context, Uri uri) {
            mPosition = position;
            mHolder = holder;
            mContext = context;
            mUri = uri;
        }

        @Override
        protected Bitmap doInBackground(Object[] params) {
            imageManager imageManager = new imageManager();
            return imageManager.decodeSampleBitmapFromURI(mContext, mUri, 100, 100);
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            if (mHolder.position == mPosition) {
                mHolder.icon.setImageBitmap(result);
            }
        }
    }


    public class CustomBitmapPool implements BitmapPool {
        @Override
        public int getMaxSize() {
            return 0;
        }

        @Override
        public void setSizeMultiplier(float sizeMultiplier) {

        }

        @Override
        public boolean put(Bitmap bitmap) {
            return false;
        }

        @Override
        public Bitmap get(int width, int height, Bitmap.Config config) {
            return null;
        }

        @Override
        public Bitmap getDirty(int width, int height, Bitmap.Config config) {
            return null;
        }

        @Override
        public void clearMemory() {

        }

        @Override
        public void trimMemory(int level) {

        }
    }

}

