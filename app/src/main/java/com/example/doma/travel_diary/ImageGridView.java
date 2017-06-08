package com.example.doma.travel_diary;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageGridView extends BaseActivity {
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid_view);


        final Adapter_GridView adapter_gridView = new Adapter_GridView();
        adapter_gridView.loadArrayListPreferences("file", "key", this);

        gridView = (GridView) findViewById(R.id.ImageGrid);


        gridView.setAdapter(adapter_gridView);


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final CustomDialog customDialog = new CustomDialog(ImageGridView.this, adapter_gridView.GridItemList.get(position).getTitle(),adapter_gridView.GridItemList.get(position).getmContent(), Uri.parse(adapter_gridView.GridItemList.get(position).getImage_Uri()),adapter_gridView.GridItemList.get(position).getDate());

                customDialog.show();

                parent.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if(event.getAction()==MotionEvent.ACTION_UP){
                            customDialog.dismiss();
                        }

                        return false;
                    }
                });

                return true;
            }
        });


//        gridView.setAdapter();


    }
}
