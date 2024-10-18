package com.walhalla.smsregclient.ui.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.ui.TObject;
import com.walhalla.smsregclient.ui.ViewHolder;

import java.util.List;

public class ServiceListArrayAdapter extends ArrayAdapter<TObject> {

    private final List<TObject> data;
    private final Activity context;

    public ServiceListArrayAdapter(Activity context, List<TObject> data) {
        super(context, R.layout.spinner_activity_countrycode, data);
        this.context = context;
        this.data = data;
    }

    @NonNull
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

        final TObject obj = data.get(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(obj.name);
        holder.flag.setImageDrawable(obj.flag);
        return view;
    }
}