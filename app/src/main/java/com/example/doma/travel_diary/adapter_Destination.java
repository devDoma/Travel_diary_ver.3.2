package com.example.doma.travel_diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by seungyunk on 2017-02-23.
 */

public class adapter_Destination extends BaseAdapter {
    private ArrayList<String> destinationList = new ArrayList<>();
    TextView textView;

    @Override
    public int getCount() {
        return destinationList.size();
    }

    @Override
    public Object getItem(int position) {
        return destinationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dialog_destination_item, parent, false);
        }
        textView = (TextView) convertView.findViewById(R.id.textView_destlist_item);
        textView.setText(destinationList.get(position));

        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.imageButton_dialogdestcancel);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                destinationList.remove(position);
//                notifyDataSetChanged();
//            }
//        });

        return textView;
    }

    public ArrayList<String> getDestinationList() {
        return destinationList;
    }

    public void setDestinationList(ArrayList<String> destinationList) {
        this.destinationList = destinationList;
    }
}
