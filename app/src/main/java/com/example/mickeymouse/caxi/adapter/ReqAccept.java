package com.example.mickeymouse.caxi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mickeymouse.caxi.R;
import com.example.mickeymouse.caxi.activity.AcceptRequest;
import com.example.mickeymouse.caxi.activity.ShareRequest;
import com.example.mickeymouse.caxi.model.FilterData;

import java.util.List;

/**
 * Created by MickeyMouse on 11-Apr-17.
 */

public class ReqAccept extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FilterData> filtedataList;
    private static final String TAG = CustomListAdapter.class.getSimpleName();


    public ReqAccept(Activity activity, List<FilterData> filtedataList) {
        this.activity = activity;
        this.filtedataList = filtedataList;
    }

    @Override
    public int getCount() {
        return filtedataList.size();
    }

    @Override
    public Object getItem(int location) {
        return filtedataList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)

            convertView = inflater.inflate(R.layout.row_item_layout, null);

        TextView userName = (TextView) convertView.findViewById(R.id.username);
        TextView carType = (TextView) convertView.findViewById(R.id.carType);
        TextView time = (TextView) convertView.findViewById(R.id.Filtertime);

        // getting movie data for the row
        final FilterData filterData = filtedataList.get(position);

        // username
        userName.setText(filterData.getUserName());

        // cartype
        carType.setText(filterData.getCarType());


        // time
        time.setText(filterData.getTime());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(parent.getContext(), AcceptRequest.class);

                intent.putExtra("bookid", filterData.getBookid().toString());
                intent.putExtra("name", filterData.getUserName().toString());

                view.getContext().startActivity(intent);


            }
        });

        return convertView;
    }
}
