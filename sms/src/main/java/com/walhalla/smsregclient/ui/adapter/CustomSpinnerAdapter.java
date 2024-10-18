package com.walhalla.smsregclient.ui.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.walhalla.smsregclient.R;

import java.util.List;

/**
 * Created by combo on 12.04.2017.
 */

public class CustomSpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    private final Context context;
    private final int resource;
    private final List<SpinnerItem> data;

    public CustomSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                                List<SpinnerItem> data) {
        super(context, resource, data);
        this.resource = resource;
        this.context = context;
        this.data = data;
    }

    @Nullable
    @Override
    public SpinnerItem getItem(int position) {
        return data.get(position);
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
        View row = inflater.inflate(resource, parent, false);

        TextView label = row.findViewById(android.R.id.text1);
        try{
            label.setText(data.get(position).getValue());
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(data.toString());
        }

        ImageView icon = row.findViewById(R.id.icon1);

        if (position == 0) {
            //icon.setImageResource(android.R.drawable.ic_btn_speak_now);
            //return getNothingSelectedView(parent);
        } else {
            //icon.setImageResource(R.drawable.ic_sms);
        }

        return row;
    }

}
