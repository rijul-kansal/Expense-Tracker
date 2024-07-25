package com.learning.expencetracker.Activity


import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.learning.expencetracker.Adapter.TransDisplayAdapter
import com.learning.expencetracker.Model.AddMoneyTrans.AddMoneyTransInputModel
import com.learning.expencetracker.Model.TransDisplayModel
import com.learning.expencetracker.Model.UpdateSingleTrans.UpdateSingleTransInputModel
import com.learning.expencetracker.R
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.ViewModel.MoneyTransViewModel
import com.learning.expencetracker.databinding.ActivityAllTransDisplayBinding
import java.text.SimpleDateFormat
import java.util.Date


class AllTransDisplayActivity : BaseActivity() {
    var dialogFor3Dots:Dialog?=null
    val requestPermessionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            viewModel.downloadExcelSheet(this, "Bearer ${token}", bookId,bookName)
        } else {
            checkPermission()
        }
    }
    lateinit var binding:ActivityAllTransDisplayBinding
    lateinit var viewModel:MoneyTransViewModel
    lateinit var ItemAdapter:TransDisplayAdapter
    lateinit var addTransDialog:Dialog
    var token:String =""
    var bookId : String =""
    var bookName : String =""
    var moneyIn:Int=0
    var moneyOut:Int=0
    var filterMembers:String?=null
    var filterType:String?=null
    var filterStartDate : String?=null
    var filterEndDate : String?=null
    var filterCat:String?=null
    lateinit var lisCat:ArrayList<String>
    lateinit var lisMembers:ArrayList<String>
    var lis : ArrayList<TransDisplayModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityAllTransDisplayBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        try{
            setSupportActionBar(binding.toolbar)
            binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24);
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            binding.toolbar.inflateMenu(R.menu.toolbar_menu);
            lisCat=ArrayList()
            viewModel = ViewModelProvider(this)[MoneyTransViewModel::class.java]
            if(intent.hasExtra(Constants.BOOKID) && intent.hasExtra(Constants.TOKEN))
            {
                token = intent.getStringExtra(Constants.TOKEN).toString()
                bookId = intent.getStringExtra(Constants.BOOKID).toString()
                bookName = intent.getStringExtra(Constants.BOOKNAME).toString()
                lisMembers= intent.getStringArrayListExtra(Constants.MEMBERS)!!


            }
            showProgressBar(this)
            viewModel.getAllTrans(this,"Bearer ${token}",bookId)

            viewModel.observerForGetAllTrans().observe(this, Observer {
                    result->
                try {
                    if(result !=null && result.data!=null)
                    {
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
            viewModel.observerForGetTransFilter().observe(this, Observer {

                    result->
                try {
                    var moneyInn=0
                    var moneyOutt=0
                    cancelProgressBar()
                    var lis2 = ArrayList<TransDisplayModel>()
                    for(i in 0..result.data!!.data!!.size-1)
                    {
                        if(result.data!!.data!!.get(i).moneyType == "In")
                        {
                            moneyInn+=(result.data!!.data?.get(i)!!.amount)!!.toInt()
                        }
                        else
                        {
                            moneyOutt+=(result.data!!.data?.get(i)!!.amount)!!.toInt()

                        }

                        lis2.add(TransDisplayModel(result.data!!.data?.get(i)!!.amount.toString(),
                            result.data!!.data!![i].category, result.data!!.data!![i].description,
                            result.data!!.data!![i].moneyType,
                            result.data!!.data!![i].addedAt.toString(), result.data!!.data!![i].addedBy,
                            result.data!!.data!![i]._id))
                    }
                    binding.moneyInTV.text = moneyInn.toString()
                    binding.moneyOutTV.text = moneyOutt.toString()
                    binding.totalBalanceTV.text=(moneyInn-moneyOutt).toString()
                    adapter(lis2)
                }catch (err:Exception)
                {
                    Log.d("rk",err.message.toString())
                }

            })

            viewModel.observerForDownloadExcelSheet().observe(this, Observer {
                    result->
                try {
                    cancelProgressBar()
                }catch (err:Exception)
                {
                    Log.d("rk",err.message.toString())
                }

            })
            binding.typeFilterLL.setOnClickListener {
                binding.typeFilterLL.setBackgroundResource(R.drawable.filter_bg)
                showTypeDialog()
            }
            binding.dateFilterLL.setOnClickListener {
                binding.dateFilterLL.setBackgroundResource(R.drawable.filter_bg)
                showDateDialog()
            }
            binding.membersFilterLL.setOnClickListener {
                binding.membersFilterLL.setBackgroundResource(R.drawable.filter_bg)
                showMembersDialog(lisMembers)
            }
            binding.categoryFilterLL.setOnClickListener {
                try {
                    binding.categoryFilterLL.setBackgroundResource(R.drawable.filter_bg)
                    var set = mutableSetOf<String>()
                    for(i in 0..lis.size-1) lis[i].category?.let { it1 -> set.add(it1) }
                    lisCat.clear()
                    for(i in set) lisCat.add(i)

                    showCatDialog(lisCat)
                }catch (err:Exception)
                {
                    Log.d("rk",err.message.toString())
                }

            }

            binding.moneyInBtn.setOnClickListener {
                transDialog(0,"add","","","",-1)
            }
            binding.moneyOutBtn.setOnClickListener {
                transDialog(1,"add","","","",-1)
            }

            binding.searchFilter.setOnClickListener {
                var finalDate: String?=null
                var finalType= filterType
                if(filterStartDate!=null)
                {
                    finalDate = filterStartDate
                    if(filterEndDate!=null) finalDate+=",${filterEndDate}"
                }





                if(finalDate!=null || finalType!=null || filterMembers!=null || filterCat!=null)
                {
                    showProgressBar(this)
                    viewModel.getTransFilter(finalType,filterMembers,finalDate,filterCat,this,"Bearer ${token}",bookId)
                }
                else
                {
                    toast(this,"Atleast select one filter option")
                }
            }
        }catch (err:Exception)
        {
            Log.d("rk",err.message.toString())
        }
    }

    private fun showDateDialog() {
        var choosedOption =-1
        val arr = arrayOf("Today","Yesterday","Single Date","Multiple Date")
        binding.dateFilterTV.text = "Date"
        filterEndDate=null
        filterStartDate=null
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle("Choose one option")
            .setPositiveButton("Select") { dialog, which ->
                dialog.dismiss()
                if(choosedOption!=-1)
                    binding.dateFilterTV.text = arr[choosedOption]
                binding.dateFilterLL.setBackgroundResource(R.drawable.cash_dialog)

            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
                binding.dateFilterTV.text = "Date"
                binding.dateFilterLL.setBackgroundResource(R.drawable.filter_bg)
                filterStartDate=null
                filterEndDate=null
                adapter(lis)
                binding.moneyInTV.text = moneyIn.toString()
                binding.moneyOutTV.text = moneyOut.toString()
                binding.totalBalanceTV.text=(moneyIn-moneyOut).toString()
            }
            .setSingleChoiceItems(
                arrayOf("Today", "Yesterday", "Single Day","Multiple Day"), -1
            ) { dialog, which ->
                if(which == 0)
                {
                    choosedOption=0
                    filterStartDate=System.currentTimeMillis().toString()
                }
                if(which == 1)
                {
                    choosedOption=1
                    filterStartDate=(System.currentTimeMillis()-86400000).toString()
                }
                if(which == 2)
                {
                    choosedOption=2
                    showDatePicker()
                }
                if(which==3)
                {
                    choosedOption=3
                    showDatePicker()
                    showDatePicker()
                }
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun showTypeDialog() {

        var choosedOption =-1
        val arr = arrayOf("Both","In","Out")
        binding.typeFilterTV.text = "Type"
        filterType=null
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle("Choose one option")
            .setPositiveButton("Select") { dialog, which ->
                dialog.dismiss()
                if(choosedOption!=-1)
                    binding.typeFilterTV.text = arr[choosedOption]
                binding.typeFilterLL.setBackgroundResource(R.drawable.cash_dialog)

            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
                binding.typeFilterTV.text = "Type"
                binding.typeFilterLL.setBackgroundResource(R.drawable.filter_bg)
                filterType=null
                adapter(lis)
                binding.moneyInTV.text = moneyIn.toString()
                binding.moneyOutTV.text = moneyOut.toString()
                binding.totalBalanceTV.text=(moneyIn-moneyOut).toString()
            }
            .setSingleChoiceItems(
                arrayOf("Both", "In", "Out"), -1
            ) { dialog, which ->
                if(which == 0)
                {
                    choosedOption=0
                    filterType="In,Out"
                }
                if(which == 1)
                {
                    choosedOption=1
                    filterType="In"
                }
                if(which == 2)
                {
                    choosedOption=2
                    filterType="Out"
                }
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun showMembersDialog(liss :ArrayList<String>) {
        var choosedOption =-1

        binding.memberFilterTV.text = "Type"
        filterMembers=null
        var set = mutableSetOf<Int>()
        val builderMultiple = AlertDialog.Builder(this)
        builderMultiple.setIcon(R.drawable.logo)
        builderMultiple.setTitle("Select members")
        val itemsArray = liss.toTypedArray()
        val checkedItems = BooleanArray(liss.size) { false }
        builderMultiple.setMultiChoiceItems(itemsArray, checkedItems) { dialog, which, isChecked ->
            if(isChecked)
            {
                set.add(which)
            }
            else
            {
                set.remove(which)
            }
        }

        builderMultiple.setNegativeButton(
            "cancel"
        ) {
                dialog, which ->
            dialog.dismiss()
            binding.memberFilterTV.text = "Type"
            binding.membersFilterLL.setBackgroundResource(R.drawable.filter_bg)
            filterType=null
            adapter(lis)
            binding.moneyInTV.text = moneyIn.toString()
            binding.moneyOutTV.text = moneyOut.toString()
            binding.totalBalanceTV.text=(moneyIn-moneyOut).toString()
        }

        builderMultiple.setPositiveButton("Select") {
                dialog, which ->
            if(set.size==1)
            binding.memberFilterTV.text = "Single Member"
            else if(set.size >1)
            {
                binding.memberFilterTV.text = "Multiple members"
            }
            binding.membersFilterLL.setBackgroundResource(R.drawable.cash_dialog)
            for(i in set)
            {
                if(filterMembers==null)
                {
                    filterMembers= liss[i]
                }
                else
                {
                    filterMembers+=",${liss[i]}"
                }
            }
        }
        builderMultiple.show()
    }
    private fun showCatDialog(liss :ArrayList<String>) {
        binding.categoryFilterTV.text = "Category"
        filterCat=null
        var set = mutableSetOf<Int>()
        val builderMultiple = AlertDialog.Builder(this)
        builderMultiple.setTitle("Select category")
        val itemsArray = liss.toTypedArray()
        val checkedItems = BooleanArray(liss.size) { false }
        builderMultiple.setMultiChoiceItems(itemsArray, checkedItems) { dialog, which, isChecked ->
            if(isChecked)
            {
                set.add(which)
            }
            else
            {
                set.remove(which)
            }
        }

        builderMultiple.setNegativeButton(
            "cancel"
        ) {
                dialog, which ->
            dialog.dismiss()
            binding.categoryFilterTV.text = "Category"
            binding.categoryFilterLL.setBackgroundResource(R.drawable.filter_bg)
            filterType=null
            adapter(lis)
            binding.moneyInTV.text = moneyIn.toString()
            binding.moneyOutTV.text = moneyOut.toString()
            binding.totalBalanceTV.text=(moneyIn-moneyOut).toString()
        }

        builderMultiple.setPositiveButton("Select") {
                dialog, which ->
            binding.categoryFilterLL.setBackgroundResource(R.drawable.cash_dialog)
            if(set.size==1)
                binding.categoryFilterTV.text = "Single Category"
            else if(set.size >1)
            {
                binding.categoryFilterTV.text = "Multiple category"
            }
            for(i in set)
            {
                if(filterCat==null)
                {
                    filterCat= liss[i]
                }
                else
                {
                    filterCat+=",${liss[i]}"
                }
            }
        }
        builderMultiple.show()
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
    fun showDatePicker()
    {
        var d=""
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.show(supportFragmentManager, "DatePicker")
        datePicker.addOnPositiveButtonClickListener {
            // formatting date in dd-mm-yyyy format.
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            d = dateFormatter.format(Date(it))
            if(filterStartDate==null)
            {
                filterStartDate= SimpleDateFormat("dd-MM-yyyy").parse(d).time.toString()
                Log.d("tk", filterStartDate!!)
            }
            else
            {
                filterEndDate=SimpleDateFormat("dd-MM-yyyy").parse(d).time.toString()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(com.learning.expencetracker.R.menu.toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {

            R.id.all ->{
                var t = findViewById<View>(R.id.all)
                val location = IntArray(2)
                t.getLocationOnScreen(location)
                displayDialogFor3Dots(location)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermessionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            requestPermessionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    fun displayDialogFor3Dots(location : IntArray)
    {

        dialogFor3Dots =Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.menu_dialog_2, null)
        dialogFor3Dots!!.setContentView(view)
        val window = dialogFor3Dots!!.window
        val wmlp = window!!.attributes
        wmlp.x = (location[0]) //x position
        wmlp.y = (location[1]-1000)

        val resetLL = view.findViewById<LinearLayout>(R.id.resetLL)
        val deleteBtn = view.findViewById<LinearLayout>(R.id.deleteLL1)
        val downloadLL = view.findViewById<LinearLayout>(R.id.downlardLL)

        resetLL.setOnClickListener {
            toast(this,"Reset Filters")
            filterMembers=null
            filterType=null
            filterCat=null
            filterStartDate=null
            filterEndDate=null
            binding.categoryFilterLL.setBackgroundResource(R.drawable.filter_bg)
            binding.membersFilterLL.setBackgroundResource(R.drawable.filter_bg)
            binding.typeFilterLL.setBackgroundResource(R.drawable.filter_bg)
            binding.dateFilterLL.setBackgroundResource(R.drawable.filter_bg)
            adapter(lis)
            binding.moneyInTV.text = moneyIn.toString()
            binding.moneyOutTV.text = moneyOut.toString()
            binding.totalBalanceTV.text=(moneyIn-moneyOut).toString()
            dialogFor3Dots!!.dismiss()
        }

        downloadLL.setOnClickListener {
            dialogFor3Dots!!.dismiss()
            showProgressBar(this)
            viewModel.downloadExcelSheet(this, "Baerer ${token}" , bookId, bookName)
        }
        dialogFor3Dots!!.show()
    }
}