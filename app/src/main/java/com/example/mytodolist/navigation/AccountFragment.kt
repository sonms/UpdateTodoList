package com.example.mytodolist.navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import com.example.mytodolist.MainActivity
import com.example.mytodolist.R
import com.example.mytodolist.SharedPref
import com.example.mytodolist.databinding.FragmentAccountBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var accountBinding: FragmentAccountBinding
    //상태유지
    var sharedPref : SharedPref? = null
    private var switch : SwitchCompat? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountBinding = FragmentAccountBinding.inflate(inflater, container, false)

        sharedPref = this.context?.let { SharedPref(it) }
        if (sharedPref!!.loadNightModeState()) {
            context?.setTheme(R.style.darktheme)
        } else {
            context?.setTheme(R.style.AppTheme)
        }

        val email = activity?.intent!!.getStringExtra("email")
        val password = activity?.intent!!.getStringExtra("password")
        switch = accountBinding.themeSwitch

        accountBinding.inputEmail.text = email
        accountBinding.inputPassword.text = password

        accountBinding.inputEmail.setOnClickListener {
            println("click")
        }


        if (sharedPref!!.loadNightModeState()) {
            switch!!.isChecked = true
        }

        switch!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPref!!.setNightModeState(true)
                restartApp()
            } else {
                sharedPref!!.setNightModeState(false)
                restartApp()
            }
        }

        return accountBinding.root
    }

    //테마 변경 시 적용을 위한 재시작
    fun restartApp() {
        val intent = Intent(context?.applicationContext, MainActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}