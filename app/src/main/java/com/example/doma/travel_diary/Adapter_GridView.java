package com.example.doma.travel_diary;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by seungyunk on 2017-02-20.
 */

public class Adapter_GridView extends BaseAdapter {
    ArrayList<Data_Log> GridItemList = new ArrayList<>();
    ImageView imageView;

    @Override
    public int getCount() {
        return GridItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return GridItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int pos = position;
        final Context context = parent.getContext();



        if (convertView == null) {
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView;
        }
//        imageView.setPadding(10, 50, 10, 50);

//        imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT));
//        imageView.setScaleType(ImageView.ScaleType.MATRIX);
//

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //디바이스의 액정 크기 구하기


        int RowHeight = (width -20)/3;

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, RowHeight));

        Glide.with(context).load(Uri.parse(GridItemList.get(pos).getImage_Uri())).into(imageView);

        return imageView;
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
        GridItemList = itemList;

    }

}
