package com.walhalla.qrcode.ui.history;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.target.Target;
import com.walhalla.qrcode.GlideApp;
import com.walhalla.qrcode.R;
import com.walhalla.qrcode.databinding.ItemHistoryBinding;
import com.walhalla.qrcode.helpers.constant.AppConstants;
import com.walhalla.qrcode.helpers.itemtouch.ItemTouchHelperAdapter;
import com.walhalla.qrcode.helpers.model.Code;
import com.walhalla.qrcode.helpers.util.TimeUtil;
import com.walhalla.qrcode.helpers.util.database.DatabaseUtil;
import com.walhalla.qrcode.ui.base.ItemClickListener;
import com.walhalla.ui.DLog;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> implements ItemTouchHelperAdapter {
    /**
     * Fields
     */
    private List<Code> mItemList;
    private final ItemClickListener<Code> mItemClickListener;

    public HistoryAdapter(ItemClickListener<Code> itemClickListener) {
        mItemList = new ArrayList<>();
        mItemClickListener = itemClickListener;
    }

    private boolean isEqual(Code left, Code right) {
        return /*left.equals(right)*/false;
    }

    public void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    public void setItemList(List<Code> itemList) {
        mItemList = itemList;
    }

    public List<Code> getItems() {
        return mItemList;
    }

    public void removeItem(Code item) {
        int index = getItemPosition(item);
        if (index < 0 || index >= mItemList.size()) return;
        mItemList.remove(index);
        notifyItemRemoved(index);
    }

    public Code getItem(int position) {
        if (position < 0 || position >= mItemList.size()) return null;
        return mItemList.get(position);
    }

    public int getItemPosition(Code item) {
        return mItemList.indexOf(item);
    }

    public int addItem(Code item) {
        Code oldItem = findItem(item);

        if (oldItem == null) {
            mItemList.add(item);
            notifyItemInserted(mItemList.size() - 1);
            return mItemList.size() - 1;
        }

        return updateItem(item, item);
    }

    public void addItem(List<Code> items) {
        for (Code item : items) {
            addItem(item);
        }
    }

    public void addItemToPosition(Code item, int position) {
        mItemList.add(position, item);
        notifyItemInserted(position);
    }

    public void addItemToPosition(List<Code> item, int position) {
        mItemList.addAll(position, item);
        notifyItemRangeChanged(position, item.size());
    }

    public Code findItem(Code item) {
        for (Code currentItem : mItemList) {
            if (isEqual(item, currentItem)) {
                return currentItem;
            }
        }
        return null;
    }

    public int updateItem(Code oldItem, Code newItem) {
        int oldItemIndex = getItemPosition(oldItem);
        mItemList.set(oldItemIndex, newItem);
        notifyItemChanged(oldItemIndex);
        return oldItemIndex;
    }

    public int updateItem(Code newItem, int position) {
        mItemList.set(position, newItem);
        notifyItemChanged(position);
        return position;
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @NonNull ItemHistoryBinding i0 = ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new HistoryViewHolder(i0);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Code item = getItem(position);

        if (item != null) holder.bindCode(item);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        AsyncTask.execute(() -> {
            DatabaseUtil.on().deleteEntity(getItem(position));
            mItemList.remove(position);
        });

    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemHistoryBinding mBinding;

        HistoryViewHolder(@NonNull ItemHistoryBinding itemBinding) {
            super(itemBinding.getRoot());
            mBinding = itemBinding;
        }

        void bindCode(Code item) {
            Context context = mBinding.getRoot().getContext();

            if (context != null) {
                try {
                    GlideApp.with(context)
                            .asBitmap()
                            .apply(new RequestOptions()
                                    .skipMemoryCache(false)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                            .error(R.drawable.cancel)
                            .listener(new RequestListener() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target target, boolean isFirstResource) {
                                    if (e != null) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(@NonNull Object resource, @NonNull Object model, Target target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }

                            })
                            .load(item.getCodeImageUri()).into(mBinding.imageViewCode);

                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                String scanType = String.format(Locale.ENGLISH, context.getString(R.string.code_scan), context.getResources().getStringArray(R.array.code_types)[item.getType()]);

                mBinding.textViewCodeType.setText(scanType);

                mBinding.textViewTime.setText(TimeUtil.getFormattedDateString(item.getTimeStamp(), AppConstants.APP_HISTORY_DATE_FORMAT));
            }

            mBinding.constraintLayoutContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getItem(getAdapterPosition()), getAdapterPosition());
            }
        }
    }
}
