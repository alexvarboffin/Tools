package com.walhalla.smsregclient.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.databinding.ItemOperationBinding;
import com.walhalla.smsregclient.network.beans.APIError;
import com.walhalla.smsregclient.network.beans.OperationBean;
import com.walhalla.smsregclient.ui.ViewHolder1;

import java.util.ArrayList;
import java.util.List;

public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String message_handler;

    public interface ChildItemClickListener {
        void onClick(View v, int position);
    }

    private final Context context;

    private final int OPERATION = 0, ERROR = 1;
    //private final View.OnClickListener listener;
    private ChildItemClickListener childItemClickListener;
    // The items to display in your RecyclerView
    private List<Object> items;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComplexRecyclerViewAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
        //this.listener = listener;
        this.message_handler = context.getString(R.string.handler_message);
    }

    public ComplexRecyclerViewAdapter(Context context) {
        items = new ArrayList<>();
        this.context = context;
        this.message_handler = context.getString(R.string.handler_message);
    }

    public void setChildItemClickListener(ChildItemClickListener listener) {
        childItemClickListener = listener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof OperationBean) {
            return OPERATION;
        } else if (items.get(position) instanceof APIError) {
            return ERROR;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case OPERATION:
                final ItemOperationBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_operation, parent, false);
                viewHolder = new ViewHolder1(binding, childItemClickListener);
                break;
            case ERROR:
                View v2 = inflater.inflate(R.layout.row_item_error, parent, false);
                viewHolder = new ViewHolder2(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case OPERATION:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                OperationBean operation = (OperationBean) items.get(position);
                vh1.onBind(context, message_handler, operation);
                break;
            case ERROR:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break;
        }
    }


    private void configureDefaultViewHolder(RecyclerViewSimpleTextViewHolder vh, int position) {
        //vh.getLabel().setText((CharSequence) items.get(position));
    }

    private void configureViewHolder2(ViewHolder2 vh2, int positon) {
        //vh2.getImageView().setImageResource(R.drawable.sample_golden_gate);
        APIError error = (APIError) items.get(positon);
        if (error != null) {
            vh2.error_msg.setText(error.error);
        }
    }

    public void swap(List<Object> data) {
        items.clear();
        items.addAll(data);

//            if (BuildConfig.DEBUG && data.get(0) instanceof OperationBean) {
//                items.clear();
//
//                for (int i = 0; i < data.size(); i++) {
//
//                    OperationBean raw = ((OperationBean) data.get(i));
//                    items.add(new OperationBean(
//                            "0000000" + i, "d2d.loc", "7770000000" + i, raw.getStatus(),
//                            raw.getMsg())
//                    );
//                }
//            }
        this.notifyDataSetChanged();
    }

    public void swap(Object data) {
        items.clear();
        items.add(data);
        notifyDataSetChanged();
    }
    //========================================================================================
    // ERR_ROW
    //========================================================================================

    private static class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder {
        private final TextView text1;

        private RecyclerViewSimpleTextViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
        }
    }

    private static class ViewHolder2 extends RecyclerView.ViewHolder {

        private final TextView response;
        private final TextView error_msg;

        ViewHolder2(View v2) {
            super(v2);
            response = v2.findViewById(R.id.response);
            error_msg = v2.findViewById(R.id.tv_error_msg);
        }
    }
}

