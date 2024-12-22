
package com.walhalla.smsregclient.presentation.view

import com.walhalla.smsregclient.network.beans.GetStateModel

interface OperationStateView : BaseView {
    fun showData(data: GetStateModel?)

    fun getNumError(index: Int)

    fun showSuccess(request_successful: Int)
}
