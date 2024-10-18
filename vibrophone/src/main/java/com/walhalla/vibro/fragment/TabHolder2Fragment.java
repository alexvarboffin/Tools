package com.walhalla.vibro.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.walhalla.ui.DLog;

import com.walhalla.vibro.Constants;
import com.walhalla.vibro.Helpers;
import com.walhalla.vibro.PlayerManager;
import com.walhalla.vibro.R;
import com.walhalla.vibro.adapter.ViewPagerAdapter;
import com.walhalla.vibro.helpers.VibrationModeSwitcher;

public class TabHolder2Fragment extends Fragment {

    private int mSelected;
    private ViewPager.OnPageChangeListener mmmm = new ViewPager.OnPageChangeListener() {
        /**
         * OnPageChangeListener
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //@@@ invalidateFragmentMenus(position);
            mSelected = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private VibrationModeSwitcher callback;

    public TabHolder2Fragment() {}

    public static TabHolder2Fragment newInstance(String param1, String param2) {
        return new TabHolder2Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_holder_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager2 mViewPager = view.findViewById(R.id.viewpager);
        mViewPager.setUserInputEnabled(false);

        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        //tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.viewpager));
        //mBinding.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (callback != null) {
                    callback.changeMode(tab.getPosition());
                }

//DLog.d("" + tab.getText() + "::" + titles.get(tab.getPosition()));
//                ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
//                if (ab != null) {
//                    ab.setSubtitle(titles.get(tab.getPosition()));
//                }

                //@@@ invalidateFragmentMenus(tab.getPosition()); //api v2

                if (getActivity() != null) {
                    Helpers.hideKeyboardFrom(getActivity(),
                            //getActivity().findViewById(R.id.et_user_input)
                            getActivity().getWindow().getDecorView()
                    );
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                try {
                    VibrationModeFragment raw = (VibrationModeFragment) getChildFragmentManager()
                            .getFragments().get(tab.getPosition());
                    if (raw != null) {
                        if (PlayerManager.getInstance(getActivity()).isVibrating) {
                            raw.switchSm(VibrationModeFragment.State.STOP);
                        }
                        raw.onTabUnselected();
                        //DLog.d("TAB_UNSELECTED::" + tab.getPosition() + " " + raw.getClass().getSimpleName());
                    }
                } catch (Exception e) {
                    DLog.handleException(e);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setupViewPager(mViewPager);
        //tabLayout.setupWithViewPager(mBinding.viewpager);
        new TabLayoutMediator(
                tabLayout, mViewPager,
                (tab, position) -> {
                    tab.setText(Constants.titles[position]);
                    DLog.d("@@" + position);
                }
        ).attach();

        //@@@mBinding.viewpager.addOnPageChangeListener(mmmm);

        //int[] icons = getResources().getIntArray(R.array.tab_icons);
        //TypedArray icons = getResources().obtainTypedArray(R.array.tab_icons);

//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            tabLayout.getTabAt(i).setIcon(icons.getResourceId(i, -1));
//        }
//        icons.recycle();
        mViewPager.setOffscreenPageLimit(
                (tabLayout.getTabCount() > 0) ? tabLayout.getTabCount() : ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        );
    }

//    private void invalidateFragmentMenus(int position) {
////        v1
////        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
////            mPagerAdapter.getItem(i).setHasOptionsMenu(i == position);
////        }
////        if (getActivity() != null) {
////            getActivity().invalidateOptionsMenu(); //or respectively its support method.
////        }
//        for (int i = 0; i < mPagerAdapter.getItemCount(); i++) {
//            //int item = mBinding.viewpager.getCurrentItem();
//            mPagerAdapter.getItem(i).setHasOptionsMenu(/*i == item && */i == position);
//            DLog.d("000000 " + i + " " + position);
//        }
//        if (getActivity() != null) {
//            getActivity().invalidateOptionsMenu(); //or respectively its support method.
//        }
//    }

    private void setupViewPager(ViewPager2 viewPager) {
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(this);//getChildFragmentManager(), 0

        //mPagerAdapter.addFragment(, getString(titles[0]));
        //mPagerAdapter.addFragment(, getString(titles[1]));
        //mPagerAdapter.addFragment(, getString(titles[2]));
        viewPager.setAdapter(mPagerAdapter);
    }

    public boolean onBackPressed() {
//        Fragment baseFragment = getChildFragmentManager().getFragments().get(mSelected);
        return false;
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof VibrationModeSwitcher) {
            callback = (VibrationModeSwitcher) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }


}
