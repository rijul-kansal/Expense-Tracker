package com.learning.expencetracker.Activity

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.expencetracker.Adapter.TransDisplayAdapter
import com.learning.expencetracker.Model.AddMoneyTrans.AddMoneyTransInputModel
import com.learning.expencetracker.Model.TransDisplayModel
import com.learning.expencetracker.Model.UpdateSingleTrans.UpdateSingleTransInputModel
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.ViewModel.MoneyTransViewModel
import com.learning.expencetracker.databinding.ActivityAllTransDisplayBinding

class AllTransDisplayActivity : BaseActivity() {
    lateinit var binding:ActivityAllTransDisplayBinding
    lateinit var viewModel:MoneyTransViewModel
    lateinit var ItemAdapter:TransDisplayAdapter
    lateinit var addTransDialog:Dialog
    var token:String =""
    var bookId : String =""
    var moneyIn:Int=0
    var moneyOut:Int=0

    var lis : ArrayList<TransDisplayModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityAllTransDisplayBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        try{
            viewModel = ViewModelProvider(this)[MoneyTransViewModel::class.java]
            if(intent.hasExtra(Constants.BOOKID) && intent.hasExtra(Constants.TOKEN))
            {
                token = intent.getStringExtra(Constants.TOKEN).toString()
                bookId = intent.getStringExtra(Constants.BOOKID).toString()
                intent.getStringExtra(Constants.BOOKID)?.let { Log.d("rk", it) }
                intent.getStringExtra(Constants.TOKEN)?.let { Log.d("rk", it) }
            }
            showProgressBar(this)
            viewModel.getAllTrans(this,"Bearer ${token}",bookId)

            viewModel.observerForGetAllTrans().observe(this, Observer {
                    result->
                try {
                    if(result !=null && result.data!=null)
                    {
//                        addTransDialog.dismiss()
                        cancelProgressBar()
                        lis.clear()
                        moneyIn=0
                        moneyOut=0
                        for(i in 0..result.data!!.data!!.size-1)
                        {
                            lis.add(TransDisplayModel(result.data!!.data?.get(i)!!.amount.toString(),result.data!!.data!!.get(i).category,result.data!!.data!!.get(i).description,result.data!!.data!!.get(i).moneyType,result.data!!.data!!.get(i).addedAt.toString(),result.data!!.data!!.get(i).addedBy,result.data!!.data!!.get(i)._id))
                            if(result.data!!.data!!.get(i).moneyType == "In")
                            {
                                moneyIn+=(result.data!!.data!!.get(i).amount)!!.toInt()
                            }
                            else
                            {
                                moneyOut+=(result.data!!.data!!.get(i).amount)!!.toInt()
                            }
                        }
                        binding.moneyInTV.text = moneyIn.toString()
                        binding.moneyOutTV.text = moneyOut.toString()
                        binding.totalBalanceTV.text=(moneyIn-moneyOut).toString()
                        adapter(lis)
                    }

                }catch (err:Exception)
                {
                    Log.d("rk",err.message.toString())
                }

            })
            viewModel.observerForAddNewTrans().observe(this, Observer {
                result->
                try {
                    if(result !=null && result.data!=null)
                    {
                        cancelProgressBar()
                        addTransDialog.dismiss()
                        var model=TransDisplayModel(result.data!!.data?.amount.toString(),result.data!!.data!!.category,result.data!!.data!!.description,result.data!!.data!!.moneyType,result.data!!.data!!.addedAt.toString(),result.data!!.data!!.addedBy,result.data!!.data!!._id)
                        var lis2 = ArrayList<TransDisplayModel>()
                        lis2.add(model)
                        lis2.addAll(lis)
                        lis.clear()
                        lis.addAll(lis2)
                        if(result.data!!.data!!.moneyType == "In")
                        {
                            moneyIn+=(result.data!!.data!!.amount)!!.toInt()
                        }
                        else
                        {
                            moneyOut+=(result.data!!.data!!.amount)!!.toInt()
                        }
                        binding.moneyInTV.text = moneyIn.toString()
                        binding.moneyOutTV.text = moneyOut.toString()
                        binding.totalBalanceTV.text=(moneyIn-moneyOut).toString()
                        adapter(lis)

                    }
                }catch (err:Exception)
                {
                    Log.d("rk",err.message.toString())
                }

            })

            viewModel.observerForUpdateSingleTrans().observe(this, Observer {
                    result->
                try {
                    if(result !=null && result.data!=null)
                    {
                        cancelProgressBar()
                        addTransDialog.dismiss()
                        var model=TransDisplayModel(result.data!!.data?.amount!!.toInt().toString(),result.data!!.data!!.category,result.data!!.data!!.description,result.data!!.data!!.moneyType,result.data!!.data!!.addedAt.toString(),result.data!!.data!!.addedBy,result.data!!.data!!._id)

                        for(i in 0..lis.size-1) {
                            if (result.data!!.data!!._id == lis[i]._id) {
                                lis.set(i, model)
                            }
                        }
                        if(result.data!!.data!!.moneyType == "In")
                        {
                            moneyIn+=(result.data!!.data!!.amount)!!.toInt()
                        }
                        else
                        {
                            moneyOut+=(result.data!!.data!!.amount)!!.toInt()
                        }
                        binding.moneyInTV.text = moneyIn.toString()
                        binding.moneyOutTV.text = moneyOut.toString()
                        binding.totalBalanceTV.text=(moneyIn-moneyOut).toString()
                        ItemAdapter.notifyDataSetChanged()

                    }

                }catch (err:Exception)
                {
                    Log.d("rk",err.message.toString())
                }

            })

            viewModel.observerForDeleteTrans().observe(this, Observer {
                try {
                    addTransDialog.dismiss()
                    viewModel.getAllTrans(this," Bearer ${token}",bookId)
                }catch (err:Exception)
                {
                    Log.d("rk",err.message.toString())
                }

            })
            binding.moneyInBtn.setOnClickListener {
                transDialog(0,"add","","","",-1)
            }
            binding.moneyOutBtn.setOnClickListener {
                transDialog(1,"add","","","",-1)

            }
        }catch (err:Exception)
        {
            Log.d("rk",err.message.toString())
        }


    }
    fun errorFn(message: String) {
        cancelProgressBar()
        toast(this, message)
    }
    fun adapter(lis : ArrayList<TransDisplayModel>){
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        ItemAdapter = TransDisplayAdapter(lis)
        binding.recycleView.adapter = ItemAdapter


        ItemAdapter.setOnClickListener(object :
            TransDisplayAdapter.OnClickListener {
            override fun onClick(position: Int, model: TransDisplayModel) {
                toast(this@AllTransDisplayActivity ,lis[position].toString())
                if(lis[position].moneyType == "Out")
                {
                    lis[position].amount?.let {
                        lis[position].category?.let { it1 ->
                            lis[position].description?.let { it2 ->
                                transDialog(1,"Update",
                                    it, it1, it2,position
                                )
                            }
                        }
                    }
                }
                else
                {
                    lis[position].amount?.let {
                        lis[position].category?.let { it1 ->
                            lis[position].description?.let { it2 ->
                                transDialog(0,"Update",
                                    it, it1, it2,position
                                )
                            }
                        }
                    }
                }

            }
        })
    }
    fun transDialog(t:Int, tt:String,amountS:String, catS:String ,desS:String,pos:Int) {
        var type=t
        addTransDialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(com.learning.expencetracker.R.layout.add_trans_dialog, null)
        val submitOtpButton = view.findViewById<TextView>(com.learning.expencetracker.R.id.addTransBtn)
        addTransDialog.setContentView(view)
        val window = addTransDialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.white)
        window?.setGravity(Gravity.BOTTOM)
        var cashInBtn = view.findViewById<TextView>(com.learning.expencetracker.R.id.cashInBtnDialog)
        var cashOutBtn = view.findViewById<TextView>(com.learning.expencetracker.R.id.cashOutBtnDialog)
        var amount = view.findViewById<TextView>(com.learning.expencetracker.R.id.amountEt)
        var cat = view.findViewById<TextView>(com.learning.expencetracker.R.id.categoryEt)
        var des = view.findViewById<TextView>(com.learning.expencetracker.R.id.descriptionEt)
        var delBtn = view.findViewById<ImageView>(com.learning.expencetracker.R.id.deleteTrans)

        var aa:Int=0

        amount.text=amountS
        cat.text=catS
        des.text=desS
        if(tt=="Update")
        {
            aa=amount.text.toString().toInt()
            delBtn.visibility=View.VISIBLE
            cashInBtn.visibility=View.INVISIBLE
            cashOutBtn.visibility=View.INVISIBLE
            submitOtpButton.text="Update"
        }
        if(type == 0)
        {
            cashInBtn.setBackgroundResource(com.learning.expencetracker.R.drawable.cash_dialog)
            cashOutBtn.setBackgroundResource(com.learning.expencetracker.R.drawable.filter_bg)
        }
        else
        {
            cashOutBtn.setBackgroundResource(com.learning.expencetracker.R.drawable.cash_dialog)
            cashInBtn.setBackgroundResource(com.learning.expencetracker.R.drawable.filter_bg)
        }

        delBtn.setOnClickListener {
            showProgressBar(this)
            lis[pos]._id?.let { it1 -> viewModel.deleteTrans(this, "Bearer ${token}", it1) }
        }
        cashInBtn.setOnClickListener {
            type=0
            cashInBtn.setBackgroundResource(com.learning.expencetracker.R.drawable.cash_dialog)
            cashOutBtn.setBackgroundResource(com.learning.expencetracker.R.drawable.filter_bg)
        }
        cashOutBtn.setOnClickListener {
            type=1
            cashOutBtn.setBackgroundResource(com.learning.expencetracker.R.drawable.cash_dialog)
            cashInBtn.setBackgroundResource(com.learning.expencetracker.R.drawable.filter_bg)
        }
        submitOtpButton.setOnClickListener {
            var a =amount.text.toString()
            var c = cat.text.toString()
            var d = des.text.toString()
            if(tt== "Update")
            {
                if(t==0)
                {
                    moneyIn-=aa
                }
                else
                {
                    moneyOut -= aa
                }
                binding.moneyInTV.text = moneyIn.toString()
                binding.moneyOutTV.text = moneyOut.toString()
                binding.totalBalanceTV.text=(moneyIn-moneyOut).toString()
                showProgressBar(this)
                lis[pos]._id?.let { it1 ->
                    viewModel.updateSingleTrans(this,"Bearer ${token}", it1,
                        UpdateSingleTransInputModel(a,c,d)
                    )
                }
            }
            else
            {

                if(a.length == 0)
                {
                    amount.setError("amount should be there")
                    addTransDialog.dismiss()

                }
                else
                {
                    showProgressBar(this)
                    if(type == 0)
                        viewModel.addNewTrans(AddMoneyTransInputModel(a,c,d,"In"),this,"Bearer ${token}",bookId)
                    else
                        viewModel.addNewTrans(AddMoneyTransInputModel(a,c,d,"Out"),this,"Bearer ${token}",bookId)
                }
            }


        }
        addTransDialog.show()
    }
}