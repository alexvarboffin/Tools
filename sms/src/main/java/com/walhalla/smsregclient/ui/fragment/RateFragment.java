

package com.walhalla.smsregclient.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.walhalla.smsregclient.core.BaseFragment;
import com.walhalla.smsregclient.presentation.view.RateView;
import com.walhalla.smsregclient.presentation.presenter.RatePresenter;

import com.walhalla.smsregclient.R;

import com.arellomobile.mvp.presenter.InjectPresenter;


public class RateFragment extends BaseFragment implements RateView, View.OnClickListener {

    @InjectPresenter
    RatePresenter mRatePresenter;


    private Button btnSetRate;
    TextView tvResponse;
    TextView tvRate;
    EditText rate;

    public static RateFragment newInstance() {
        RateFragment fragment = new RateFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSetRate = view.findViewById(R.id.btn_set_rate);
        tvResponse = view.findViewById(R.id.response);
        tvRate = view.findViewById(R.id.tv_rate);
        rate = view.findViewById(R.id.rate);
        btnSetRate.setOnClickListener(this);
        this.modifyContainer(getString(R.string.title_set_rate), false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_set_rate) {

            String rate = this.rate.getText().toString().trim();
            if (rate.isEmpty()) {
                Toast.makeText(getContext(), "Установите значение", Toast.LENGTH_LONG).show();
            } else {
                mRatePresenter.setRate(rate);
            }

        }
    }

    @Override
    public void successRateResponse(float rate) {

        String message = Float.toString(rate);
        showRate(rate);
        Toast.makeText(
                getContext(), String.format("Установлено значение: %1$s", message),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void showError(String err) {
        Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRate(float rate) {
        String message = Float.toString(rate);
        this.rate.setText(message);
    }
}
