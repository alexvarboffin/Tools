//package com.walhalla.smsregclient.screens;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.google.android.material.tabs.TabLayout;
//
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.lifecycle.Lifecycle;
//import androidx.viewpager2.adapter.FragmentStateAdapter;
//
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.material.tabs.TabLayoutMediator;
//import com.walhalla.smsregclient.R;
//import com.walhalla.smsregclient.core.BaseFragment;
//import com.walhalla.smsregclient.databinding.FragmentOperationsBinding;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class ViewPagerHolder extends BaseFragment {
//
//
//    private FragmentOperationsBinding mBinding;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_operations, container, false);
//        return mBinding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //- setSupportActionBar(toolbar);
//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), null);
//
//        mBinding.viewPager.setAdapter(mSectionsPagerAdapter);
//        if (getActivity() != null) {
//            TabLayout tabLayout = (getActivity()).findViewById(R.id.tabs);
//            if (tabLayout != null) {
//                //old version --> tabLayout.setupWithViewPager(mBinding.viewPager);
//                new TabLayoutMediator(tabLayout, mBinding.viewPager,
//                        (tab, position) -> tab.setText("titles.get(position)")).attach();
//            }
//        }
//
//       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });*/
//    }
//
//    /*@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
//        return true;
//    }*/
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_exit) {
////            System.exit(0);
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            //View rootView = inflater.inflate(R.layout.fragment_tabbed, nav_container, false);
//            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return null;//rootView;
//        }
//    }
//
//    public static class SectionsPagerAdapter extends FragmentStateAdapter {//FragmentPagerAdapter {
//
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public SectionsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
//            super(fragmentManager, lifecycle);
//        }
//
//
////    public SectionsPagerAdapter(FragmentManager manager) {
////        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
////    }
//
//
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
////    @Override
////    public CharSequence getPageTitle(int position) {
////        return mFragmentTitleList.get(position);
////    }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            return mFragmentList.get(position);
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
//        }
//
//        @Override
//        public int getItemCount() {
//            return mFragmentList.size();
//        }
//    }
//}
