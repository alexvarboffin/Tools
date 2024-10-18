package com.walhalla.smsregclient.ui.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import com.walhalla.smsregclient.BuildConfig;
import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.core.BaseFragment;
import com.walhalla.smsregclient.databinding.FragmentOperationsBinding;
import com.walhalla.smsregclient.network.Opstate;
import com.walhalla.smsregclient.m1;

import java.util.ArrayList;
import java.util.List;


public class OperationTabbedFragment extends BaseFragment {

    private FragmentOperationsBinding mBind;

    public OperationTabbedFragment() {
    }

    public static OperationTabbedFragment newInstance() {
        return new OperationTabbedFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Integer> titles = new ArrayList<>();
        titles.add(R.string.tab_active);
        titles.add(R.string.tab_completed);
        titles.add(R.string.tab_ip);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager(), getLifecycle());
        adapter.addFragment(OperationTabFragment.newInstance(Opstate.ACTIVE));
        adapter.addFragment(OperationTabFragment.newInstance(Opstate.COMPLETED));
        if (BuildConfig.DEBUG) {
            adapter.addFragment(TabIpFragment.newInstance());
        }
        mBind.viewPager.setAdapter(adapter);

        if (getActivity() != null) {
            m1 activity = ((m1) getActivity());
            ActionBar actionBar = activity.getSupportActionBar();
            TabLayout tabLayout = getActivity().findViewById(R.id.tabs);

            if (actionBar != null) {
                actionBar.setSubtitle(getString(R.string.title_operations));
            }
            tabLayout.setVisibility(View.VISIBLE);

            //((MainActivity)getActivity()).getSupportActionBar()
            // .setDisplayHomeAsUpEnabled(true);
            //old version --> tabLayout.setupWithViewPager(mBinding.viewPager);

            //tabLayout.setVisibility(View.INVISIBLE);
            // new version
            new TabLayoutMediator(tabLayout, mBind.viewPager,
                    (tab, position) -> tab.setText(titles.get(position))).attach();

            TypedArray icons = getResources().obtainTypedArray(R.array.tab_icons);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                tabLayout.getTabAt(i).setIcon(icons.getResourceId(i, -1));
            }
            icons.recycle();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBind = FragmentOperationsBinding.inflate(inflater, container, false);
        return mBind.getRoot();
    }

    public static class SectionsPagerAdapter extends FragmentStateAdapter {//FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public SectionsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }


//    public SectionsPagerAdapter(FragmentManager manager) {
//        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//    }


        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mFragmentTitleList.get(position);
//    }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);

            /*switch (position) {
                case 0:
                    return ScreenOperationTab1.newInstance("active");
                case 1:
                    return ScreenOperationTab2.newInstance("completed");
            }

            return null;
            */
        }

        @Override
        public int getItemCount() {
            return mFragmentList.size();
        }
    }
}
