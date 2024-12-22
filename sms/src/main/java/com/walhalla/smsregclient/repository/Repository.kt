
package com.walhalla.smsregclient.repository

/**
 * Created by combo on 11/15/2017.
 *
 */
interface Repository {
    interface Callback<T> {
        fun successResponse(data: T)

        fun falseResponse(message: String?)
    }

    fun getBalance(callback: Callback<String?>?)
}
