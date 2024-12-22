

package com.walhalla.smsregclient.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.walhalla.smsregclient.Status;
import com.walhalla.smsregclient.Utils;
import com.walhalla.smsregclient.core.BaseFragment;
import com.walhalla.smsregclient.databinding.FragmentOperationStateBinding;
import com.walhalla.smsregclient.network.beans.GetStateModel;
import com.walhalla.smsregclient.presentation.view.OperationStateView;
import com.walhalla.smsregclient.presentation.presenter.OperationStatePresenter;


import com.walhalla.smsregclient.R;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.walhalla.ui.DLog;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import static com.walhalla.smsregclient.Const.ARG_TZID;
import static com.walhalla.smsregclient.network.Constants.hm;

public class OperationStateFragment extends BaseFragment implements OperationStateView, View.OnClickListener {


    private FragmentOperationStateBinding mBinding;

    @Override
    public void getNumError(int index) {
        String[] arr = getResources().getStringArray(R.array.err_get_num_repeat);
        Toast.makeText(getContext(), arr[index], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(int successful) {
        Toast.makeText(getContext(), getString(successful), Toast.LENGTH_SHORT).show();
    }


    private Timer timer = new Timer();


    @InjectPresenter
    OperationStatePresenter mOperationStatePresenter;


    @Inject
    FirebaseAnalytics mFirebaseAnalytics;

    public static OperationStateFragment newInstance(String tzid) {
        OperationStateFragment fragment = new OperationStateFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TZID, tzid);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_operation_state, container, false);
        return mBinding.getRoot();
    }

    private void doSomethingRepeatedly() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    mOperationStatePresenter.getState();
                    // Stop refresh animation
                    mBinding.swipeRefreshLayout.setRefreshing(false);
                } catch (Exception ignored) {
                }

            }
        }, 0, 3000);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mBinding.btnActionGetNumRepeat.setOnClickListener(this);
        mBinding.btnActionSetReady.setOnClickListener(this);
        mBinding.number.setOnClickListener(this);
        mBinding.btnActionSetOperationOver.setOnClickListener(this);
        mBinding.btnActionSetOperationUsed.setOnClickListener(this);
        mBinding.btnActionSetOperationRevise.setOnClickListener(this);

        mBinding.btnActionSetOperationOk.setOnClickListener(this);
        mBinding.msg.setOnClickListener(this);


        String tzid = "";
        if (getArguments() != null) {
            tzid = getArguments().getString(ARG_TZID);
        }

        //mOperationStatePresenter.getState();
        mBinding.swipeRefreshLayout.setOnRefreshListener(this::doSomethingRepeatedly);
        mOperationStatePresenter.setTzid(tzid);
        this.modifyContainer(getString(R.string.tzid_numbel_holder, tzid), false);

        doSomethingRepeatedly();
        //mOperationStatePresenter.getState();
    }


    private void switchState(Status state) {
        switch (state) {

            case TZ_INPOOL:
                enableBtn(new Button[]{
                        mBinding.btnActionSetReady,
                        mBinding.btnActionSetOperationRevise,
                        mBinding.btnActionGetNumRepeat,
                        mBinding.btnActionSetOperationOver,
                        mBinding.btnActionSetOperationUsed,
                        mBinding.btnActionSetOperationOk
                }, false);
                //all false
                break;

            /**
             * 1
             * We get number, and server wait until we say setReady
             */
            case TZ_NUM_PREPARE:
                enableBtn(new Button[]{
                        //mBinding.btnActionSetReady,
                        mBinding.btnActionSetOperationRevise,
                        mBinding.btnActionGetNumRepeat,
                        mBinding.btnActionSetOperationOver,
                        mBinding.btnActionSetOperationUsed,
                        mBinding.btnActionSetOperationOk
                }, false);

                enableBtn(new Button[]{
                        mBinding.btnActionSetOperationUsed,
                        mBinding.btnActionSetReady
                }, true);
                break;

            case TZ_NUM_WAIT:

                //Сервис ждет смс
                enableBtn(new Button[]{
                        mBinding.btnActionSetReady,
                        mBinding.btnActionSetOperationRevise,
                        mBinding.btnActionGetNumRepeat,
                        mBinding.btnActionSetOperationOver,
                        mBinding.btnActionSetOperationUsed,
                        mBinding.btnActionSetOperationOk
                }, false);

                //Ожидаем смс
                enableBtn(new Button[]{
                        mBinding.btnActionSetOperationUsed,
                        mBinding.btnActionSetReady
                }, true);
                break;

            case TZ_NUM_ANSWER:
                enableBtn(new Button[]{
                        mBinding.btnActionSetReady
                }, false);

                enableBtn(new Button[]{
                        //mBinding.btnActionSetReady,
                        mBinding.btnActionSetOperationRevise,
                        mBinding.btnActionGetNumRepeat,
                        mBinding.btnActionSetOperationOver,
                        mBinding.btnActionSetOperationUsed,
                        mBinding.btnActionSetOperationOk
                }, true);

                //Отвечаем сервису родошел ли код
                break;


            //timeout
            case TZ_OVER_NR:
            case TZ_OVER_EMPTY:


                enableBtn(new Button[]{
                        mBinding.btnActionSetReady,
                        mBinding.btnActionSetOperationRevise,
                        mBinding.btnActionGetNumRepeat,
                        mBinding.btnActionSetOperationOver,
                        mBinding.btnActionSetOperationUsed,
                        mBinding.btnActionSetOperationOk
                }, false);
                //Отвечаем сервису додошел ли код
                break;


            case TZ_OVER_OK:
                enableBtn(new Button[]{
                        mBinding.btnActionSetReady,
                        mBinding.btnActionSetOperationRevise,
                        //mBinding.btnActionGetNumRepeat,
                        mBinding.btnActionSetOperationOver,
                        mBinding.btnActionSetOperationUsed,
                        mBinding.btnActionSetOperationOk
                }, false);//=======================================================
                enableBtn(new Button[]{mBinding.btnActionGetNumRepeat}, true);
                break;


        }
        mBinding.swipeRefreshLayout.setRefreshing(false);
    }

    private void enableBtn(Button[] buttons, boolean b) {
        for (Button button : buttons) {
            button.setEnabled(b);
            button.setVisibility((b) ? View.VISIBLE : View.GONE);
        }
    }

    private void sendAnalytics(int event) {
        SparseArrayCompat<String> map = new SparseArrayCompat<>();

        map.append(R.id.btn_action_get_num_repeat, "action_get_num_repeat");
        map.append(R.id.btn_action_set_ready, "action_set_ready");
        map.append(R.id.btn_action_set_operation_over, "action_set_operation_over");
        map.append(R.id.btn_action_set_operation_used, "action_set_operation_used");
        map.append(R.id.btn_action_set_operation_revise, "action_set_operation_revise");
        map.append(R.id.btn_action_set_operation_ok, "action_set_operation_ok");


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        String label = map.get(event);
        if (label != null && !label.isEmpty()) {
            Bundle params = new Bundle();
            params.putString("image_name", "lolo");
            params.putString("full_text", label);
            mFirebaseAnalytics.logEvent("debug_" + label, params);
            mFirebaseAnalytics.setCurrentScreen(getActivity(), null, null);
        }
    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(int err) {
        Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showData(GetStateModel data) {

        if (data != null) {
            String aa = (data.response == null) ? "" : data.response;
            try {
                Status rr = Status.valueOf(aa.trim());
                if (rr != null) {
                    Integer aaa = hm.get(rr);
                    if (aaa != null) {
                        mBinding.response.setText(aaa);
                    }
                }
            } catch (IllegalArgumentException e) {
                DLog.handleException(e);
            }
            mBinding.service.setText(getString(R.string.service_handler_2, data.getService()));
        }

        //data.setResponse(TZ_NUM_ANSWER);
        //data.setNumber("7952257513");
        //data.setMsg("09878");
        if (data != null) {
            if (data.getNumber() == null) {
                mBinding.numberTitle.setVisibility(View.GONE);
                mBinding.number.setVisibility(View.GONE);
            } else {
                mBinding.numberTitle.setVisibility(View.VISIBLE);
                mBinding.number.setVisibility(View.VISIBLE);
                mBinding.number.setText(data.getNumber());//"Выделенный номер: "
            }

            if (data.getMsg() == null) {
                mBinding.msgTitle.setVisibility(View.GONE);
                mBinding.msg.setVisibility(View.GONE);
            } else {
                mBinding.msgTitle.setVisibility(View.VISIBLE);
                mBinding.msg.setVisibility(View.VISIBLE);
                mBinding.msg.setText(data.getMsg());//"Выделенный номер: "
            }

            String tmp = data.response;
            try {
                Status rr = Status.valueOf(tmp.trim());
                switchState(rr);
            } catch (IllegalArgumentException e) {
                DLog.handleException(e);
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btn_action_get_num_repeat) {
            mOperationStatePresenter.getNumRepeat();
        } else if (id == R.id.number) {
            Utils.copyToClipboard(view.getContext(), mBinding.number.getText().toString());
        } else if (id == R.id.msg) {
            Utils.copyToClipboard(view.getContext(), mBinding.msg.getText().toString());

            //[*]
        } else if (id == R.id.btn_action_set_ready) {
            mOperationStatePresenter.setReady();

            //[*]
        } else if (id == R.id.btn_action_set_operation_over) {
            mOperationStatePresenter.setOperationOver();
            //[*]
        } else if (id == R.id.btn_action_set_operation_used) {
            mOperationStatePresenter.setOperationUsed();

            //[*]
        } else if (id == R.id.btn_action_set_operation_revise) {
            mOperationStatePresenter.setOperationRevise();

            //[*]
        } else if (id == R.id.btn_action_set_operation_ok) {
            mOperationStatePresenter.setOperationOk();
        }
        sendAnalytics(view.getId());
    }
}
