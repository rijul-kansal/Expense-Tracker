package com.learning.expencetracker.Activity

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.learning.expencetracker.R

open class BaseActivity : AppCompatActivity() {
    var dialog: Dialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showProgressBar(context:Context)
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
}