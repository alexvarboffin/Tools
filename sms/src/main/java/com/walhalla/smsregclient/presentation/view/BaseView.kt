
package com.walhalla.smsregclient.presentation.view

import com.arellomobile.mvp.MvpView

/**
 * Created by combo on 11/16/2017.
 *
 */
interface BaseView : MvpView {
    fun showError(errorMsg: String?)

    fun showError(err: Int)
}
