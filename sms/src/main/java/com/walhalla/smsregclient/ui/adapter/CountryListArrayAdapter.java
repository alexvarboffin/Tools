package com.walhalla.smsregclient.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.ui.TObject;
import com.walhalla.smsregclient.ui.ViewHolder;

import java.util.List;


public class CountryListArrayAdapter extends ArrayAdapter<TObject> {

    private final List<TObject> list;
    private final Activity context;

    public CountryListArrayAdapter(Activity activity, List<TObject> list) {
        super(activity, R.layout.spinner_activity_countrycode, list);
        this.context = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.spinner_activity_countrycode, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(android.R.id.text1);
            viewHolder.flag = view.findViewById(R.id.icon1);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).name);
        holder.flag.setImageDrawable(list.get(position).flag);
        return view;
    }

}