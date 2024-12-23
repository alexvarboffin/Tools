//package com.walhalla.qrcode.ui.adapter;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentStatePagerAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SectionsPagerAdapter extends FragmentStatePagerAdapter {//FragmentPagerAdapter {
//
//    private final List<Fragment> mFragmentList = new ArrayList<>();
//    private final List<String> mFragmentTitleList = new ArrayList<>();
//
//    public SectionsPagerAdapter(FragmentManager manager) {
//        super(manager);
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return mFragmentList.get(position);
//
//            /*switch (position) {
//                case 0:
//                    return ScreenOperationTab1.newInstance("active");
//                case 1:
//                    return ScreenOperationTab2.newInstance("completed");
//            }
//
//            return null;
//            */
//    }
//
//    @Override
//    public int getCount() {
//        return mFragmentList.size();
//    }
//
//    public void addFragment(Fragment fragment, String title) {
//        mFragmentList.add(fragment);
//        mFragmentTitleList.add(title);
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mFragmentTitleList.get(position);
//    }
//
//}
