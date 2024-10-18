package com.walhalla.vibro.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.walhalla.vibro.activity.VibrationMode;
import com.walhalla.vibro.fragment.VibrationModeFragment;


import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter
        //FragmentStatePagerAdapter
        //RecyclerView.Adapter<ViewPagerAdapter.PagerVH>
{//  FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull Fragment fragmentActivity) {
        super(fragmentActivity);
    }


//    @NonNull
//    @Override
//    public PagerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new PagerVH(LinearLayout.inflate(parent.getContext(), R.layout.about, null));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PagerVH holder, int position) {
//
//    }
//
    @Override
    public int getItemCount() {
        return 3;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
//            case 0:
//                return new ViewMain();

            case 0:
                return VibrationModeFragment.newInstance(VibrationMode.RANDOM);
            case 1:
                return VibrationModeFragment.newInstance(VibrationMode.SIMPLE);
            case 2:
                return VibrationModeFragment.newInstance(VibrationMode.EXTENDED);
            default:
                return new Fragment();
        }
    }

//    public class PagerVH extends RecyclerView.ViewHolder {
//        public PagerVH(@NonNull View itemView) {
//            super(itemView);
//        }
//    }

}
