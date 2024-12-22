package com.walhalla.smsregclient.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.walhalla.smsregclient.Application
import com.walhalla.smsregclient.Const
import com.walhalla.smsregclient.R
import com.walhalla.smsregclient.core.BaseFragment
import com.walhalla.smsregclient.databinding.FragmentOrderAddBinding
import com.walhalla.smsregclient.helper.PreferencesHelper
import com.walhalla.smsregclient.network.APIService
import com.walhalla.smsregclient.network.badbackend.BaseResponse
import com.walhalla.smsregclient.network.beans.APIError
import com.walhalla.smsregclient.network.response.OrderAddModel
import com.walhalla.ui.DLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ScreenOrderAdd : BaseFragment(), CompoundButton.OnCheckedChangeListener {
    private var mBinding: FragmentOrderAddBinding? = null

    @Inject
    var preferencesHelper: PreferencesHelper? = null

    private val service: APIService? = null
    private var options: MutableMap<String, String>? = null
    private val genderListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val current = parent.selectedItem as String
                //Log.d(current.getKey());
                //Toast.makeText(getContext(), "TObject ID: " + current.getKey()
                //        + ",  TObject Name : " + current.getValue(), Toast.LENGTH_SHORT).show();
                options!!["gender"] = current
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    private val addOrderCallback: Callback<OrderAddModel> = object : Callback<OrderAddModel?> {
        override fun onResponse(call: Call<OrderAddModel?>, response: Response<OrderAddModel?>) {
            if (response.isSuccessful) {
                //{"response":"ERROR","error_msg":"Wrong count value"}


                val bm: BaseResponse? = response.body()

                if (bm is APIError) {
                    Toast.makeText(context, bm.error, Toast.LENGTH_SHORT).show()
                    DLog.d("Error: $bm")
                } else if (bm != null) {
                    val bean = bm as OrderAddModel
                    DLog.d("Success: $bean")

                    if (bean.response == Const.RESPONSE_SUCCESS) {
                        val id = bean.order_id //id заказа
                    }
                }
            }
        }

        override fun onFailure(call: Call<OrderAddModel?>, throwable: Throwable) {
            showError(throwable.localizedMessage)
        }
    }

    private fun showError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private var selectedListener: AdapterView.OnItemSelectedListener? = null


    private var countriesCodes: Array<String>
    private var servicesCodes: Array<String>

    init {
        Application.getComponents().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_add, container, false)
        return mBinding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.btnOrderAdd.setOnClickListener { v: View? ->
            options!!["name"] = mBinding!!.inputName.text.toString()
            options!!["age"] = mBinding!!.inputAge.text.toString()
            options!!["city"] = mBinding!!.inputCity.text.toString()
            options!!["count"] = mBinding!!.inputCount.text.toString()

            DLog.d(options.toString())
            service!!.orderAdd(
                preferencesHelper!!.apiKey,
                options
            ).enqueue(addOrderCallback)
        }

        this.modifyContainer(getString(R.string.title_order_add), false)

        options = HashMap()
        selectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                var code = ""
                val parentId = parent.id
                if (parentId == R.id.country) {
                    code = countriesCodes[position]
                    options["country"] = code
                } else if (parentId == R.id.service) {
                    code = servicesCodes[position]
                    options["service"] = code
                }

                Toast.makeText(context, code, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        countriesCodes = resources.getStringArray(R.array.country_codes)
        servicesCodes = resources.getStringArray(R.array.service_codes)

        val countries = resources.getStringArray(R.array.country_names)


        val adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item, countries
        )

        mBinding!!.country.adapter = adapter
        mBinding!!.country.onItemSelectedListener = selectedListener

        val handler = getString(R.string.service_handler)
        val array = resources.getStringArray(R.array.service_names)
        for (i in array.indices) {
            array[i] = String.format(handler, array[i])
        }

        val adapterService = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item, array
        )
        mBinding!!.service.adapter = adapterService
        mBinding!!.service.onItemSelectedListener = selectedListener

        //gender
        val genderList = ArrayList<String>()
        genderList.add("male")
        genderList.add("female")

        val adp = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item, genderList
        )
        mBinding!!.sGender.adapter = adp
        //dropdownGender.setSelection(adapter.getPosition("###"));
        mBinding!!.sGender.onItemSelectedListener = genderListener
        mBinding!!.cbOptions.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        options!!["options"] = if ((isChecked)) "on" else ""
        enableExtarnalOptions(isChecked)
    }

    private fun enableExtarnalOptions(isChecked: Boolean) {
        mBinding!!.inputAge.isEnabled = isChecked
        mBinding!!.inputCity.isEnabled = isChecked
        mBinding!!.inputName.isEnabled = isChecked
        mBinding!!.sGender.isEnabled = isChecked
    } /*private OnItemSelectedListener typeSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Log.d(TAG, "user selected : "
                    + typeSpinner.getSelectedItem().toString());

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private OnTouchListener typeSpinnerTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            selected = true;
            ((BaseAdapter) typeSpinnerAdapter).notifyDataSetChanged();
            return false;
        }
    };*/
}
