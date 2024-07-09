package com.learning.expencetracker.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.expencetracker.Activity.AllTransDisplayActivity
import com.learning.expencetracker.Adapter.BookNameDisplayAdapter
import com.learning.expencetracker.Model.BookNamesDisplayModel
import com.learning.expencetracker.Model.CreateNewBook.CreateNewBookInputModel
import com.learning.expencetracker.Model.UpdateBookModel.UpdateBookInputModel
import com.learning.expencetracker.R
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.ViewModel.BookViewModel
import com.learning.expencetracker.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    var dialog: Dialog?=null
    var positionForDots:Int =-1
    var dialogToEnterOneInput: Dialog?=null
    lateinit var binding:FragmentHomeBinding
    lateinit var token :String
    lateinit var viewModel: BookViewModel
    var bookNameDialog :Dialog?=null
    var ItemAdapter : BookNameDisplayAdapter?=null
    var lis : ArrayList<BookNamesDisplayModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentHomeBinding.inflate(inflater, container, false)

        try{
            viewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]
            val sharedPreference =  requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            token = sharedPreference.getString(Constants.TOKEN,"defaultName").toString()
            Log.d("rk",token)

            if(lis.size == 0)
            {
                showProgressBar(requireActivity())
                viewModel.getBooks(requireContext(),this, "Bearer ${token}")
            }

            viewModel.observerForGetBooks().observe(viewLifecycleOwner , Observer {
                    result->
                if(result!=null)
                {
                    binding.refreshLayout.isRefreshing = false
                    cancelProgressBar()
                    lis.clear()
                    for(i in 0.. result.data!!.data!!.size -1)
                    {
                        var m = BookNamesDisplayModel(result.data!!.data?.get(i)?.name,result.data!!.data?.get(i)?._id ,result.data!!.data?.get(i)?.updatedLast,result.data!!.data?.get(i)?.userId)
                        lis.add(m)
                    }

                    adapter(lis)
                    viewModel.clearGetBookForUser()
                }

            })

            binding.addBookBtn.setOnClickListener {
                addingNewBook()
            }

            viewModel.observerForCreateNewBook().observe(viewLifecycleOwner , Observer {
                result->
                try {
                    if (result != null && result.data != null) {
                        bookNameDialog?.dismiss()
                        lis.clear()
                        viewModel.getBooks(requireContext(),this, "Bearer ${token}")
                        viewModel.clearCreateNewBook()
                    }
                }catch (err:Exception)
                {
                    Log.d("rk",err.message.toString())
                }
            })

            viewModel.observerForUpdateBooks().observe(viewLifecycleOwner , Observer {
                result->
                try {
                    if(result!=null && result.data!=null)
                    {
                        dialogToEnterOneInput?.dismiss()
                        toast(requireContext(),"Book Updated")
                        lis.clear()
                        viewModel.getBooks(requireContext(),this, "Bearer ${token}")
                        viewModel.clearUpdateBook()
                    }
                }catch (err:Exception)
                {
                    Log.d("rk",err.toString())
                }

            })

            viewModel.observerForDeleteBooks().observe(viewLifecycleOwner , Observer {
                result->
            try {
                if(result!=null)
                {
                    Log.d("rk",result.toString())
                    dialog?.dismiss()
                    toast(requireContext(),"Book deleted")
                    lis.clear()
                    viewModel.getBooks(requireContext(),this, "Bearer ${token}")
                    viewModel.clearDeleteBook()
                }

            }catch (err:Exception)
            {
                Log.d("rk",err.message.toString())
            }

            })

            binding.refreshLayout.setOnRefreshListener{
                viewModel.getBooks(requireContext(),this,"Bearer ${token}")
            }
        }catch(err:Exception)
        {
            Log.d("rk",err.toString())
        }

        return binding.root
    }

    fun errorFn(message: String) {

        if(message == "a user has not created any book")
        {
            lis.clear()
            adapter(lis)
        }
        cancelProgressBar()
        toast(requireActivity(), message)
        binding.refreshLayout.isRefreshing = false
    }

    fun showProgressBar(context: Context)
    {
        dialog = Dialog(context)
        dialog!!.setContentView(R.layout.progress_bar)
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

    fun addingNewBook() {
        try {
            bookNameDialog = Dialog(requireContext(), android.R.style.Theme_Translucent_NoTitleBar)
            bookNameDialog = Dialog(requireContext())

            val view: View = LayoutInflater.from(requireActivity()).inflate(R.layout.adding_book_dialog, null)

            bookNameDialog?.setContentView(view)
            bookNameDialog?.setCanceledOnTouchOutside(false)

            val window = bookNameDialog?.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.setGravity(Gravity.BOTTOM)

            val submitButton = view.findViewById<TextView>(R.id.createBookBtn)
            submitButton.setOnClickListener {
                showProgressBar(requireActivity())
                val bookName = view.findViewById<EditText>(R.id.nameET).text.toString()
                viewModel.createNewBook(CreateNewBookInputModel(bookName), requireContext(), this, "Bearer ${token}")
            }

            bookNameDialog?.show()
        }catch (err:Exception)
        {
            Log.d("rk",err.message.toString())
        }
    }

    fun adapter(lis : ArrayList<BookNamesDisplayModel>){
        binding.recycleView.layoutManager = LinearLayoutManager(requireActivity())
        ItemAdapter = BookNameDisplayAdapter(lis)
        binding.recycleView.adapter = ItemAdapter


        ItemAdapter!!.setOnClickListener(object :
            BookNameDisplayAdapter.OnClickListener {
            override fun onClick(position: Int, model: BookNamesDisplayModel) {
                var arr= ArrayList<String>()
                for(i in 0..lis[position].userId!!.size-1)
                {
                    lis[position].userId?.get(i)?.let { arr.add(it) }
                }
                Log.d("rk",arr.toString())
                var intent = Intent(requireActivity(),AllTransDisplayActivity::class.java)
                intent.putExtra(Constants.BOOKID,lis[position]._id)
                intent.putExtra(Constants.TOKEN,token)
                intent.putExtra(Constants.MEMBERS,arr)
                requireActivity().startActivity(intent)
            }
        })

        ItemAdapter!!.setOnClickListenerForMembers(
            object : BookNameDisplayAdapter.OnClickListenerForMembers{
                override fun onClickForMembers(position: Int, model: LinearLayout) {
                    displayMembersDialog(lis[position].userId)
                }
            }
        )
        ItemAdapter!!.setOnClickListenerFor3Dots(
            object : BookNameDisplayAdapter.OnClickListenerFor3Dots{
                override fun onClickFor3Dots(position: Int, location : IntArray) {
                    Log.d("rk",location[0].toString())
                    displayDialogFor3Dots(location,position)
                }
            }
        )
    }

    fun displayMembersDialog(lis: List<String?>?) {
        val builderSingle = AlertDialog.Builder(requireContext())
        builderSingle.setIcon(R.drawable.logo)
        builderSingle.setTitle("Select One Name to chat")

        val arrayAdapter =
            ArrayAdapter<String>(requireActivity(), android.R.layout.select_dialog_singlechoice)
        if (lis != null) {
            for( i in 0 .. lis.size-1)
            {
                arrayAdapter.add(lis?.get(i))
            }
        }

        builderSingle.setNegativeButton(
            "cancel"
        ) { dialog, which -> dialog.dismiss() }

        builderSingle.setAdapter(
            arrayAdapter
        ) { dialog, which ->
            val strName = arrayAdapter.getItem(which)
            val builderInner = AlertDialog.Builder(requireContext())
            builderInner.setMessage(strName)
            builderInner.setTitle("Your Selected Item is")
            builderInner.setPositiveButton("Ok"){ dialog, which -> dialog.dismiss() }
            builderInner.show()
        }
        builderSingle.show()
    }

    fun displayMembersDialogForDeletion(lis: List<String?>?, id : String) {

        var arrlis : ArrayList<String> = ArrayList()
        var set = mutableSetOf<Int>()
        val builderMultiple = AlertDialog.Builder(requireContext())
        builderMultiple.setIcon(R.drawable.logo)
        builderMultiple.setTitle("Select members to delete")
        val itemsArray = lis!!.filterNotNull().toTypedArray()
        val checkedItems = BooleanArray(lis!!.size) { false }
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
        ) { dialog, which -> dialog.dismiss() }

        builderMultiple.setPositiveButton("Delete") {
                dialog, which ->
            for(i in set)
            {
                arrlis.add(itemsArray[i])
                Log.d("rk",arrlis.toString())
            }
            viewModel.updateBook(requireContext(),this,"Bearer ${token}",id,
                UpdateBookInputModel(removeUser = arrlis)
            )
            showProgressBar(requireActivity())
        }
        builderMultiple.show()
    }

    fun displayDialogFor3Dots(location : IntArray, pos:Int)
    {

        dialog =Dialog(requireContext())
        val view: View = LayoutInflater.from(requireActivity()).inflate(R.layout.menu_dialog, null)
        dialog!!.setContentView(view)
        val window = dialog!!.window
        val wmlp = window!!.attributes
        wmlp.x = (location[0]) //x position
        wmlp.y = (location[1]-700)

        val renameBtn = view.findViewById<LinearLayout>(R.id.renameLL)
        val deleteBtn = view.findViewById<LinearLayout>(R.id.deleteLL)
        val addMembersBtn = view.findViewById<LinearLayout>(R.id.add_membersLL)
        val deleteMembersBtn = view.findViewById<LinearLayout>(R.id.deleteMemberLL)

        renameBtn.setOnClickListener{

            dialog!!.dismiss()
            dialogToEnterOneInput("Rename book","please enter new book name", "rename","Rename",lis,pos)
        }
        deleteBtn.setOnClickListener{
            dialog!!.dismiss()
            showProgressBar(requireActivity())
            lis[pos]._id?.let { viewModel.deleteBook(requireContext(),this,"Bearer ${token}", it) }
        }

        addMembersBtn.setOnClickListener {
            dialog!!.dismiss()
            dialogToEnterOneInput("Add member to book","please enter email address", "addMember","Add",lis,pos)
        }
        deleteMembersBtn.setOnClickListener {
            var liss =lis[pos].userId
            lis[pos]._id?.let { it1 -> displayMembersDialogForDeletion(liss, it1) }
            dialog!!.dismiss()
        }
        dialog!!.show()
    }
    fun dialogToEnterOneInput(title :String , hint :String , type:String , buttonText :String , lis : ArrayList<BookNamesDisplayModel>, position:Int) {

        try{
            dialogToEnterOneInput = Dialog(requireActivity(), android.R.style.Theme_Translucent_NoTitleBar)
            dialogToEnterOneInput=Dialog(requireActivity())
            val view: View = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_for_common_use, null)
            val submitOtpButton = view.findViewById<TextView>(R.id.sendOtpBtn)
            dialogToEnterOneInput!!.setContentView(view)
            dialogToEnterOneInput!!.setCanceledOnTouchOutside(false)
            val window = dialogToEnterOneInput!!.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.setGravity(Gravity.BOTTOM)


            view.findViewById<TextView>(R.id.titleTv).text = title
//            view.findViewById<TextInputEditText>(R.id.editText1).hint = hint
            view.findViewById<TextView>(R.id.sendOtpBtn).text = buttonText

            if(type == "rename")
            {
                submitOtpButton.setOnClickListener {
                    showProgressBar(requireActivity())
                    var newBookName = view.findViewById<EditText>(R.id.emailET).text.toString()
                    lis[position]._id?.let { it1 ->
                        viewModel.updateBook(requireContext(), this, "Bearer ${token}" , it1,
                            UpdateBookInputModel(name = newBookName)
                        )
                    }
                }
            }else if(type == "addMember")
            {
                submitOtpButton.setOnClickListener {
                    showProgressBar(requireActivity())
                    var newEmail = view.findViewById<EditText>(R.id.emailET).text.toString()
                    lis[position]._id?.let { it1 ->
                        viewModel.updateBook(requireContext(), this, "Bearer ${token}" , it1,
                            UpdateBookInputModel(newUserId = newEmail)
                        )
                    }
                }
            }


            dialogToEnterOneInput!!.show()
        }catch(err:Exception)
        {
            Log.d("rk",err.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog = null
        bookNameDialog = null
        dialogToEnterOneInput = null
        lis.clear()
        ItemAdapter=null

        Log.d("rk", "Fragment is being destroyed")
    }
}