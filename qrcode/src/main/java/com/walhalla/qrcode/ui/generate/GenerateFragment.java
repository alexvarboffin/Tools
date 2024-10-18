package com.walhalla.qrcode.ui.generate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.walhalla.qrcode.R;
import com.walhalla.qrcode.databinding.FragmentGenerateBinding;
import com.walhalla.qrcode.helpers.constant.IntentKey;
import com.walhalla.qrcode.helpers.model.Code;
import com.walhalla.qrcode.ui.activity.GeneratedCodeActivity;

import org.jetbrains.annotations.NotNull;

public class GenerateFragment extends androidx.fragment.app.Fragment implements View.OnClickListener {

    private FragmentGenerateBinding mBinding;
    private Context mContext;


    public GenerateFragment() {

    }

    public static GenerateFragment newInstance() {
        return new GenerateFragment();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentGenerateBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();

        //initializeCodeTypesSpinner
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.code_types, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.item_spinner);
        mBinding.spinnerTypes.setAdapter(arrayAdapter);


//        com.walhalla.qrcode.AdManager m = AdManager.getInstance();
//        m.showAds(getContext(), null);

    }


    private void setListeners() {
        mBinding.spinnerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View view1 = parent.getSelectedView();
                if (view1 instanceof TextView) {
                    ((TextView) view1).setTextColor(ContextCompat.getColor(mContext,
                            position == 0 ? R.color.text_hint : R.color.text_regular));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.textViewGenerate.setOnClickListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (mContext == null) {
            return;
        }
        if (view.getId() == R.id.text_view_generate) {
            generateCode();
        }
    }

    private void generateCode() {
        Intent intent = new Intent(mContext, GeneratedCodeActivity.class);
        if (mBinding.editTextContent.getText() != null) {
            String content = mBinding.editTextContent.getText().toString().trim();
            int type = mBinding.spinnerTypes.getSelectedItemPosition();

            if (!TextUtils.isEmpty(content) && type != 0) {

                boolean isValid = true;

                switch (type) {
                    case Code.BAR_CODE:
                        if (content.length() > 80) {
                            makeSnack(mContext, R.string.error_barcode_content_limit);
                            isValid = false;
                        }
                        break;

                    case Code.QR_CODE:
                        if (content.length() > 1000) {
                            makeSnack(mContext, R.string.error_qrcode_content_limit);
                            isValid = false;
                        }
                        break;

                    default:
                        isValid = false;
                        break;
                }

                if (isValid) {
                    Code code = new Code(content, type);
                    intent.putExtra(IntentKey.MODEL, code);
                    startActivity(intent);
                }
            } else {
                makeSnack(mContext, R.string.error_provide_proper_content_and_type);
            }
        }
    }

    //javax.swing.Popup
    private void makeSnack(Context mContext, int res0) {
        //Toast.makeText(this, res0, Toast.LENGTH_LONG).show();
        //View view = findViewById(R.id.cLayout);
        View view = getActivity().findViewById(android.R.id.content);
        if (view == null) {
            Toast.makeText(getActivity(), res0, Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(view, res0, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
}
