package com.walhalla.smsregclient.ui.dialog;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.ui.TObject;
import com.walhalla.smsregclient.ui.adapter.ServiceListArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServiceCodeActivity extends ListActivity {

    public static String RESULT_SERVICE_CODE = "service_code";
    public String[] countrycodes;
    private TypedArray imgs;
    private List<TObject> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateServiceList();

        ArrayAdapter<TObject> adapter = new ServiceListArrayAdapter(this, list);

        setListAdapter(adapter);
        getListView().setOnItemClickListener((parent, view, position, id) -> {
            TObject c = list.get(position);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(RESULT_SERVICE_CODE, c.code);
            setResult(RESULT_OK, returnIntent);
            imgs.recycle(); //recycle images
            finish();
        });
    }

    private void populateServiceList() {
        String handler = getString(R.string.service_handler);
        list = new ArrayList<>();
        final String[] array = getResources().getStringArray(R.array.service_names);
        countrycodes = getResources().getStringArray(R.array.service_codes);
        imgs = getResources().obtainTypedArray(R.array.service_flags);
        for (int i = 0; i < countrycodes.length; i++) {
            list.add(new TObject(String.format(handler, array[i]), countrycodes[i], imgs.getDrawable(i)));
        }
    }
}
