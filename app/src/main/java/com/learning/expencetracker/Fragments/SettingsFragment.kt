package com.learning.expencetracker.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.learning.expencetracker.Activity.IntroActivity
import com.learning.expencetracker.Activity.PaymentActivity
import com.learning.expencetracker.Activity.PaymentHistoryActivity
import com.learning.expencetracker.Activity.ProfileActivity
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    lateinit var binding:FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.logoutLL.setOnClickListener {
            val sharedPreference =  requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.remove(Constants.TOKEN)
            editor.remove(Constants.BOOKS_DATA)
            editor.commit()
            startActivity(Intent(requireActivity(), IntroActivity::class.java))
            requireActivity().finish()
        }
        binding.profileLL.setOnClickListener {
            startActivity(Intent(requireActivity(), ProfileActivity::class.java))
        }

        binding.paymentLL.setOnClickListener {
            startActivity(Intent(requireActivity(), PaymentActivity::class.java))
        }

        binding.paymentHistoryLL.setOnClickListener {
            startActivity(Intent(requireActivity(), PaymentHistoryActivity::class.java))
        }
        return binding.root
    }
}