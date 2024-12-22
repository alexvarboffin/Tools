/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.walhalla.smsregclient.presentation.view;

import com.arellomobile.mvp.MvpView;

/**
 * Created by combo on 11/16/2017.
 *
 */

public interface BaseView extends MvpView {
    void showError(String errorMsg);

    void showError(int err);
}
