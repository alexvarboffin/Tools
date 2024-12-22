package com.walhalla.smsregclient.repository

import com.walhalla.smsregclient.Application
import com.walhalla.smsregclient.helper.PreferencesHelper
import com.walhalla.smsregclient.network.APIService
import com.walhalla.smsregclient.network.badbackend.BaseResponse
import com.walhalla.smsregclient.network.beans.APIError
import com.walhalla.smsregclient.network.response.Balance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by combo on 11/15/2017.
 *
 */
class SmsRepository : Repository {
    @Inject
    var service: APIService? = null

    @Inject
    var preferencesHelper: PreferencesHelper? = null

    init {
        Application.getComponents().inject(this)
    }

    override fun getBalance(callback: Repository.Callback<String?>?) {
        val task = service!!.getBalance(
            preferencesHelper!!.apiKey
        )
        task.enqueue(object : Callback<Balance?> {
            override fun onResponse(call: Call<Balance?>, response: Response<Balance?>) {
                if (response.isSuccessful) {
                    val bm: BaseResponse? = response.body()
                    if (bm is APIError) {
                        val errorMsg = bm.error
                        callback?.falseResponse(errorMsg)
                    } else if (bm != null) {
                        val data = response.body()
                        //tvResponse.setText(data.getBalance());
                        if (data != null) {
                            val balance = if ((data.balance == null)) "0" else data.balance
                            //tvBalance.setText(result);//1 - success
                            callback?.successResponse(balance)
                        }
                    }
                } else {
                    //code 200
                    //APIError error = ErrorUtils.parseError(response, retrofit);
                    callback?.falseResponse(response.message())
                    //[{"response":"0","error_msg":"No active tranzactions"}]
                }
            }

            override fun onFailure(call: Call<Balance?>, t: Throwable) {
                callback?.falseResponse(t.localizedMessage)
            }
        })
    }


}
