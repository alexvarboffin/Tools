package com.walhalla.smsregclient.core;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.view.View;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.google.android.material.tabs.TabLayout;
import com.walhalla.smsregclient.BuildConfig;
import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.m1;
import com.walhalla.ui.DLog;

public abstract class BaseFragment extends MvpAppCompatFragment {
    protected IOnFragmentInteractionListener mListener;

    public BaseFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IOnFragmentInteractionListener) {
            mListener = (IOnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnFragmentInteractionListener");
        }
    }

    //[2]
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setRetainInstance(true);//saved fragment state
//        //      Log.d("");
//    }

    //[3]
    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup nav_container, Bundle savedInstanceState) {

    //View v = inflater.inflate(R.layout.screen_categories_, nav_container, false);
    //     Log.d("");
    //     return null;
    // }

    //[4]
    //@Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    //    super.onActivityCreated(savedInstanceState);
    //mListener.setBurger(toolbar);//<-- show toolbar
    //     Log.d("");
    //}
//    @Override
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

    //[5]
//    @Override
//    public void onStart() {
//        super.onStart();
//        //    Log.d("");
//    }

    //FRAGMENT IS ACTIVE....

    //[6]
    @Override
    public void onResume() {
        super.onResume();
        //deltaTime = System.currentTimeMillis() - time;
        //Log.e(TAG, "[ #### onResume: delta - " + deltaTime + " #### ]");
        if (BuildConfig.DEBUG) {
            DLog.d(this.getClass().getSimpleName());
        }
    }


    //FRAGMENT_IS_ACTIVE
    //FRAGMENT IS ACTIVE....
    //7
//    @Override
//    public void onPause() {
//        super.onPause();
//        //    Log.d("");
//    }

    //8
//    @Override
//    public void onStop() {
//        super.onStop();
//        //    Log.d("");
//    }

    //9
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        //   Log.d("");
//    }

    //10
    @Override
    public void onDestroy() {
        super.onDestroy();
        //  Log.d("");
    }

    //11...
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    public IOnFragmentInteractionListener getListener() {
        return mListener;
    }


    protected void modifyContainer(String title, boolean visible) {
        if (getActivity() != null) {
            m1 activity = ((m1) getActivity());
            ActionBar actionBar = activity.getSupportActionBar();
            TabLayout tabLayout = getActivity().findViewById(R.id.tabs);

            if (actionBar != null) {
                actionBar.setSubtitle(title);
                tabLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }
    }
}
