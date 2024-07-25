package com.learning.expencetracker.Activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.expencetracker.Adapter.PaymentHistoryAdapter
import com.learning.expencetracker.Model.PaymentHistoryModel
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.ViewModel.PaymentViewModel
import com.learning.expencetracker.databinding.ActivityPaymentHistoryBinding

class PaymentHistoryActivity : BaseActivity() {
    lateinit var binding:ActivityPaymentHistoryBinding
    lateinit var ItemAdapter:PaymentHistoryAdapter
    lateinit var token :String
    lateinit var viewModel:PaymentViewModel
    lateinit var lis:ArrayList<PaymentHistoryModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityPaymentHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        try{
            viewModel = ViewModelProvider(this)[PaymentViewModel::class.java]
            val sharedPreference=  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            token = sharedPreference.getString(Constants.TOKEN,"").toString()
            showProgressBar(this)
            viewModel.paymentHistory("Bearer ${token}", this)

            viewModel.observerForPaymentHistory().observe(this, Observer {
                result->
                cancelProgressBar()
                if(result!=null)
                {
                    lis= ArrayList()

                    for(i in 0..result.data!!.history!!.size-1)
                    {
                        lis.add(PaymentHistoryModel(result.data!!.history!![i]!!.amount.toString(),
                            result.data!!.history!![i]!!.date.toString(),
                            result.data!!.history!![i]!!.validTill.toString()))
                    }

                    adapter(lis)
                }

            })

        }catch (err:Exception){
            Log.d("rk",err.toString())
        }
    }


    fun adapter(lis : ArrayList<PaymentHistoryModel>){
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        ItemAdapter = PaymentHistoryAdapter(lis)
        binding.recycleView.adapter = ItemAdapter
    }

    fun errorFn(message: String) {
        cancelProgressBar()
        toast(this, message)
    }
}