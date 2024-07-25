package com.learning.expencetracker.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.expencetracker.Adapter.GraphItemAdapter
import com.learning.expencetracker.Model.BookNamesDisplayModel
import com.learning.expencetracker.Model.GraphShownModel
import com.learning.expencetracker.R
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.ViewModel.BookViewModel
import com.learning.expencetracker.ViewModel.MoneyTransViewModel
import com.learning.expencetracker.databinding.FragmentStatisticsBinding
import org.eazegraph.lib.models.PieModel
import java.lang.Math.abs
import java.util.Random


class StatisticsFragment : Fragment() {
    var dialog: Dialog?=null
    lateinit var binding: FragmentStatisticsBinding
    lateinit var viewModel: MoneyTransViewModel
    lateinit var viewModel1: BookViewModel
    lateinit var token :String

    var lis:ArrayList<BookNamesDisplayModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =FragmentStatisticsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[MoneyTransViewModel::class.java]
        viewModel1 = ViewModelProvider(requireActivity())[BookViewModel::class.java]
        try{
            val sharedPreference =  requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            token = sharedPreference.getString(Constants.TOKEN,"defaultName").toString()
            val type = sharedPreference.getInt(Constants.PAYMENT_TYPE,0)
            if(type !=0)
            {
                showProgressBar(requireActivity())
                viewModel1.getBooks(requireContext(),this,"Bearer ${token}")
                viewModel1.observerForGetBooks().observe(viewLifecycleOwner, Observer {
                        result->

                    if(result!=null)
                    {
                        cancelProgressBar()
                        lis.clear()
                        var liss= ArrayList<String>()
                        for( i in 0..result.data!!.data!!.size-1)
                        {
                            result.data!!.data?.get(i)?.name?.let { liss.add(it) }
                            lis.add(BookNamesDisplayModel(result.data!!.data?.get(i)?.name,
                                result.data!!.data?.get(i)!!._id,null,null))
                        }
                        viewModel1.clearGetBookForUser()
                        displayMembersDialog(liss)
                    }
                })
                viewModel.observerForGetDataForCategory().observe(viewLifecycleOwner, Observer {
                        result->
                    if(result!=null)
                    {
                        cancelProgressBar()
                        try {
                            var lisAmountIn=ArrayList<GraphShownModel>()
                            var lisAmountOut=ArrayList<GraphShownModel>()


                            binding.piechart.clearChart()
                            binding.piechart1.clearChart()


                            for(i in 0..result.data!!.data!!.size-1)
                            {
                                val color = abs(getRandomHex()).toString(16)
                                result.data!!.data?.get(i)?._id?.let {
                                    GraphShownModel("#${color}",
                                        it,result.data!!.data?.get(i)!!.amountIn.toString())
                                }?.let { lisAmountIn.add(it) }

                                result.data!!.data?.get(i)?._id?.let {
                                    GraphShownModel("#${color}",
                                        it,result.data!!.data?.get(i)!!.amountOut.toString())
                                }?.let { lisAmountOut.add(it) }


                                binding.piechart.addPieSlice( PieModel(
                                    "${result.data!!.data?.get(i)?._id}", (result.data!!.data?.get(i)!!.amountIn)!!.toFloat(),
                                    Color.parseColor("#${color}")
                                ))
                                binding.piechart1.addPieSlice( PieModel(
                                    "${result.data!!.data?.get(i)?._id}", (result.data!!.data?.get(i)!!.amountOut)!!.toFloat(),
                                    Color.parseColor("#${color}")
                                ))
                            }
                            binding.LL1.visibility=View.VISIBLE
                            binding.LL2.visibility=View.VISIBLE
                            binding.Tv1.visibility=View.VISIBLE
                            binding.Tv2.visibility=View.VISIBLE

                            binding.piechart.startAnimation()
                            binding.piechart1.startAnimation()

                            adapterForAmountIn(lisAmountIn)
                            adapterForAmountOut(lisAmountOut)



                            viewModel.clearGetDataForCat()
                        }catch (err:Exception)
                        {
                            Log.d("rk",err.message.toString())
                        }

                    }
                })
            }
            else
            {
                binding.Tv3.visibility=View.VISIBLE
            }

        }catch (err:Exception)
        {
            
        }


        return binding.root
    }
    fun showProgressBar(context: Context)
    {
        dialog = Dialog(context)
        dialog!!.setContentView(R.layout.progress_bar)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun cancelProgressBar()
    {
        if(dialog!=null)
        {
            dialog!!.cancel()
        }
    }

    fun toast(context: Context, message:String)
    {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }
    fun errorFn(message: String) {
        cancelProgressBar()
        toast(requireActivity(), message)
    }


    fun displayMembersDialog(llis: List<String?>?) {
        val builderSingle = AlertDialog.Builder(requireContext())
        builderSingle.setTitle("Select One Book to show Stats")

        val arrayAdapter =
            ArrayAdapter<String>(requireActivity(), android.R.layout.select_dialog_singlechoice)
        if (lis != null) {
            for( i in 0 .. lis.size-1)
            {
                arrayAdapter.add(llis?.get(i))
            }
        }

        builderSingle.setNegativeButton(
            "cancel"
        ) { dialog, which -> dialog.dismiss() }

        builderSingle.setAdapter(
            arrayAdapter
        ) { dialog, which ->
            dialog.dismiss()
            showProgressBar(requireActivity())
            lis[which]._id?.let {
                viewModel.getDataForCategory(this,requireContext(),"Bearer ${token}",
                    it
                )
            }
        }

        builderSingle.setPositiveButton("Show")
        {
            dialog,which->
            dialog.dismiss()
            showProgressBar(requireActivity())
            lis[which]._id?.let {
                viewModel.getDataForCategory(this,requireContext(),"Bearer ${token}",
                    it
                )
            }
        }
        builderSingle.show()
    }
    fun getRandomHex() :Int
    {
        val random = Random()
        return  Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }


    fun adapterForAmountIn(lis : ArrayList<GraphShownModel>){
        binding.recycleViewAmountIn.layoutManager = LinearLayoutManager(requireActivity())
        val ItemAdapter = GraphItemAdapter(lis)
        binding.recycleViewAmountIn.adapter = ItemAdapter
    }
    fun adapterForAmountOut(lis : ArrayList<GraphShownModel>){
        binding.recycleViewAmountOut.layoutManager = LinearLayoutManager(requireActivity())
        val ItemAdapter = GraphItemAdapter(lis)
        binding.recycleViewAmountOut.adapter = ItemAdapter
    }

}