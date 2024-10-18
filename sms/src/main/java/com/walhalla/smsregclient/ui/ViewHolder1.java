package com.walhalla.smsregclient.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.Status;
import com.walhalla.smsregclient.databinding.ItemOperationBinding;
import com.walhalla.smsregclient.network.beans.OperationBean;
import com.walhalla.smsregclient.ui.adapter.ComplexRecyclerViewAdapter;
import com.walhalla.ui.DLog;

import static com.walhalla.smsregclient.network.Constants.hm;

public class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ItemOperationBinding binding;
    private ComplexRecyclerViewAdapter.ChildItemClickListener listener;
    private ImageView imageView;

    // = view.findViewById(R.id.iv_service)
    //AppCompatImageView service;

    public ViewHolder1(ItemOperationBinding binding, ComplexRecyclerViewAdapter.ChildItemClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.getRoot().setOnClickListener(this);
        this.imageView = binding.getRoot().findViewById(R.id.iv_circle);
        this.listener = listener;
        //Log.d("@hash=" + this.hashCode());
    }


    @Override
    public void onClick(View v) {
        this.listener.onClick(v, getAdapterPosition());
    }

    public void onBind(Context context, String message_handler, OperationBean operation) {
        if (operation != null) {
            String tmp = (operation.tzid == null) ? "" : operation.tzid;
            binding.tzid.setText(tmp);
            binding.service.setText(operation.getService());

            String s = "";
            if (operation.getPhone() != null) {
                s = String.format("+%1$s", operation.getPhone());
            }
            binding.phone.setText(s);
            if (DLog.nonNull(operation.getMsg())) {
                binding.msg.setVisibility(View.GONE);
            } else {
                binding.msg.setText(String.format(message_handler, operation.getMsg()));
            }

            final String tmp0 = operation.getStatus();
            try {
                Status ee = Status.valueOf(tmp0);
                binding.status.setText(hm.get(ee));
            } catch (Exception e) {
                DLog.handleException(e);
            }
            if (operation.getStatus().equals(Status.TZ_OVER_OK.value)) {
                DrawableCompat.setTint(imageView.getDrawable(), ContextCompat.getColor(context, R.color.btn_green_pressed));
            } else if (operation.getStatus().equals(Status.TZ_OVER_EMPTY.value)) {
                DrawableCompat.setTint(imageView.getDrawable(), ContextCompat.getColor(context, R.color.bg_circle_red));
            }
        }
    }
}
