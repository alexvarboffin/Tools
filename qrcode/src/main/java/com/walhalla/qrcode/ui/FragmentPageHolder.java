//package com.walhalla.qrcode.ui;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.walhalla.qrcode.R;
//import com.walhalla.qrcode.databinding.FragmentViewpagerHolderBinding;
//import com.walhalla.qrcode.ui.adapter.SectionsPagerAdapter;
//import com.walhalla.qrcode.ui.generate.GenerateFragment;
//import com.walhalla.qrcode.ui.history.HistoryFragment;
//import com.walhalla.qrcode.ui.scan.ScanFragment;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import androidx.fragment.app.Fragment;
//import androidx.viewpager.widget.ViewPager;
//
//public class FragmentPageHolder extends Fragment implements BottomNavigationView.OnItemSelectedListener {
//
//
//    private FragmentViewpagerHolderBinding mBinding;
//    private MenuItem prevMenuItem;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mBinding = FragmentViewpagerHolderBinding.inflate(inflater, container, false);
//        return mBinding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mBinding.bottomNavigation.setOnItemSelectedListener(this);
//        setupViewPager(mBinding.viewpager);
//        mBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//                if (prevMenuItem != null) prevMenuItem.setChecked(false);
//                else mBinding.bottomNavigation.getMenu().getItem(0).setChecked(false);
//                mBinding.bottomNavigation.getMenu().getItem(position).setChecked(true);
//                prevMenuItem = mBinding.bottomNavigation.getMenu().getItem(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }
//
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        int id = menuItem.getItemId();
//        if (id == R.id.action_scan) {
//            mBinding.viewpager.setCurrentItem(0);
//        } else if (id == R.id.action_history) {
//            mBinding.viewpager.setCurrentItem(1);
//        } else if (id == R.id.action_generate) {
//            mBinding.viewpager.setCurrentItem(2);
//        }
//        return false;
//    }
//
//    private void setupViewPager(ViewPager o) {
//        SectionsPagerAdapter mPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
//        mPagerAdapter.addFragment(ScanFragment.newInstance(), getString(R.string.action_scan));
//        mPagerAdapter.addFragment(HistoryFragment.newInstance(), getString(R.string.action_history));
//        mPagerAdapter.addFragment(GenerateFragment.newInstance(), getString(R.string.action_generate));
//        o.setAdapter(mPagerAdapter);
//    }
//}
