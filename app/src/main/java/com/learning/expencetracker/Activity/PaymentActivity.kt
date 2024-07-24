package com.learning.expencetracker.Activity

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.learning.expencetracker.Adapter.ViewPagerAdapter2
import com.learning.expencetracker.Model.VerifyTransAndAddToDB.TransSuccessModel
import com.learning.expencetracker.Model.VerifyTransAndAddToDB.VerifyTransAndAddToDBInputModel
import com.learning.expencetracker.R
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.Utils.HashGenerationUtils
import com.learning.expencetracker.ViewModel.PaymentViewModel
import com.learning.expencetracker.databinding.ActivityPaymentBinding
import com.payu.base.models.ErrorResponse
import com.payu.base.models.PayUPaymentParams
import com.payu.checkoutpro.PayUCheckoutPro
import com.payu.checkoutpro.utils.PayUCheckoutProConstants
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener

class PaymentActivity : BaseActivity() {
    lateinit var viewPagerAdapter: ViewPagerAdapter2
    lateinit var binding: ActivityPaymentBinding
    var type:Int?=null
    lateinit var viewModel:PaymentViewModel
    lateinit var token:String
    lateinit var tnxId:String
    lateinit var amt:String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityPaymentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        try {
            viewModel = ViewModelProvider(this)[PaymentViewModel::class.java]
            val sharedPreference=  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            type = sharedPreference.getInt(Constants.PAYMENT_TYPE,0)
            token = sharedPreference.getString(Constants.TOKEN,"").toString()
            Log.d("rk",type.toString()+token)

            if(type == getItem(0))
            {
                binding.chooseBtn.text = "Current"
                binding.chooseBtn.setBackgroundResource(R.drawable.cash_dialog)
            }
            else
            {
                binding.chooseBtn.setBackgroundResource(R.drawable.filter_bg)
                binding.chooseBtn.text = "Choose"
            }
            showAdapter()
            binding.chooseBtn.setOnClickListener {
                if (getItem(0) != type) {
                    val pos = getItem(0)
                    if (pos == 1) {
                        showProgressBar(this)
                        viewModel.getKeys("Bearer $token", this,"600")
                    }else if(pos==2)
                    {
                        showProgressBar(this)
                        viewModel.getKeys("Bearer $token", this,"1200")
                    }
                }
            }

            viewModel.observerForGetKeys().observe(this, Observer {
                result->
                cancelProgressBar()
                tnxId = result.data!!.transId!!
                amt = result.data!!.amount!!
                setUp(result!!.data!!.amount!!,
                    result.data!!.key!!,
                    result.data!!.mobileNumber!!, result.data!!.transId!!,
                    result.data!!.firstName!!, result.data!!.email!!, result.data!!.UserCred!!,
                    result.data!!.salt!!)
            })


            viewModel.observerForVerifyTransAndAddToDB().observe(this , Observer {
                result->
                val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                if(amt == "600")
                {
                    editor.putInt(Constants.PAYMENT_TYPE,1)
                    type=1
                }
                else if(amt == "1200")
                {
                    editor.putInt(Constants.PAYMENT_TYPE,2)
                    type=2
                }
                else
                {
                    editor.putInt(Constants.PAYMENT_TYPE,0)
                    type=0
                }
                editor.apply()

                if(type == getItem(0))
                {
                    binding.chooseBtn.text = "Current"
                    binding.chooseBtn.setBackgroundResource(R.drawable.cash_dialog)
                }
                else
                {
                    binding.chooseBtn.setBackgroundResource(R.drawable.filter_bg)
                    binding.chooseBtn.text = "Choose"
                }

                tnxId = ""
                amt= ""
            })
        } catch (e: Exception) {
            Log.d("rk", e.message.toString())
        }
    }

     fun showAdapter() {
         binding.chooseBtn.visibility= View.VISIBLE
        viewPagerAdapter = ViewPagerAdapter2(this)
        binding.slideViewPager.adapter = viewPagerAdapter
        binding.slideViewPager.addOnPageChangeListener(viewListener)
    }


    private fun setUp( amount :String , key:String ,mobileNumber:String,transId:String, firstName:String,email:String,userCred:String, salt:String) {
        val additionalParamsMap: HashMap<String, Any?> = HashMap()
        additionalParamsMap[PayUCheckoutProConstants.SODEXO_SOURCE_ID] = "srcid123"

        val payUPaymentParams = PayUPaymentParams.Builder()
            .setAmount(amount)
            .setIsProduction(false)
            .setKey(key)
            .setProductInfo("AccessAppFeatures")
            .setPhone(mobileNumber)
            .setTransactionId(transId)
            .setFirstName(firstName)
            .setEmail(email)
            .setSurl("https://cbjs.payu.in/sdk/success")
            .setFurl("https://cbjs.payu.in/sdk/failure")
            .setUserCredential(userCred)
            .build()

        PayUCheckoutPro.open(this, payUPaymentParams, object : PayUCheckoutProListener {
                override fun onPaymentSuccess(response: Any) {
                    Log.d("rk",tnxId)

                    viewModel.verifyTransAndAddToDB("Bearer ${token}", this@PaymentActivity, VerifyTransAndAddToDBInputModel(transId))

                }

                override fun onPaymentFailure(response: Any) {
                    response as HashMap<*, *>
                    val payUResponse = response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]?.toString()
                    if (payUResponse != null) {
                        val transSuccessModel: TransSuccessModel = Gson().fromJson(payUResponse, TransSuccessModel::class.java)
                        val res = Gson().toJson(transSuccessModel)
                        Log.d("rk", payUResponse)
                        Log.d("rk", res)
                    } else {
                        Log.d("rk", "payUResponse is null")
                    }

                }

                override fun onPaymentCancel(isTxnInitiated: Boolean) {
                    Log.d("rk", "Transaction Initiated: $isTxnInitiated")
                }

                override fun onError(errorResponse: ErrorResponse) {
                    var errorMessage: String? = null
                    if (!errorResponse.errorMessage.isNullOrEmpty()) {
                        errorMessage = errorResponse.errorMessage
                    }
                    if (errorMessage != null) {
                        Log.d("rk", errorMessage)
                    }
                }

                override fun setWebViewProperties(webView: WebView?, bank: Any?) {
                }

                override fun generateHash(valueMap: HashMap<String, String?>, hashGenerationListener: PayUHashGenerationListener) {
                    val hashData = valueMap[PayUCheckoutProConstants.CP_HASH_STRING]
                    val hashName = valueMap[PayUCheckoutProConstants.CP_HASH_NAME]
                    if (!hashData.isNullOrEmpty() && !hashName.isNullOrEmpty()) {
                        val hash: String? = HashGenerationUtils.generateHashFromSDK(hashData, salt)
                        if (!TextUtils.isEmpty(hash)) {
                            val dataMap: HashMap<String, String?> = HashMap()
                            dataMap[hashName] = hash
                            hashGenerationListener.onHashGenerated(dataMap)
                        }
                    }
                }
            }
        )
    }



    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            if(position == type)
            {
                binding.chooseBtn.text = "Current"
                binding.chooseBtn.setBackgroundResource(R.drawable.cash_dialog)
            }
            else
            {
                binding.chooseBtn.setBackgroundResource(R.drawable.filter_bg)
                binding.chooseBtn.text = "Choose"
            }
        }
        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun getItem(i: Int): Int {
        return binding.slideViewPager.currentItem.plus(i) ?: 0
    }


    fun errorFn(message: String) {
        cancelProgressBar()
        toast(this, message)
    }
}