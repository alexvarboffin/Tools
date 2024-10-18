package com.walhalla.smsregclient.ui.dialog;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.ui.TObject;
import com.walhalla.smsregclient.ui.adapter.CountryListArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class CountrycodeActivity extends ListActivity {

    public static String RESULT_COUNTRY_CODE = String.valueOf((char) 222);
    private TypedArray typedArray;
    private List<TObject> countryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateCountryList();
        ArrayAdapter<TObject> adapter = new CountryListArrayAdapter(this, countryList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener((parent, view, position, id) -> {
            TObject c = countryList.get(position);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(RESULT_COUNTRY_CODE, c.code);
            setResult(RESULT_OK, returnIntent);
            typedArray.recycle(); //recycle images
            finish();
        });
    }

    private void populateCountryList() {
        countryList = new ArrayList<>();
        final String[] countrynames = getResources().getStringArray(R.array.country_names);
        final String[] countrycodes = getResources().getStringArray(R.array.country_codes);
        typedArray = getResources().obtainTypedArray(R.array.country_flags);
        for (int i = 0; i < countrycodes.length; i++) {
            countryList.add(new TObject(countrynames[i], countrycodes[i], typedArray.getDrawable(i)));
        }
    }


}

