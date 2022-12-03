package com.example.mytodolist.navigation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import androidx.preference.SwitchPreferenceCompat
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
class AccountFragment : PreferenceFragmentCompat() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var accountBinding: FragmentAccountBinding
    //상태유지
    var sharedPref : SharedPref? = null
    private var switch : SwitchCompat? = null
    var pref : SharedPreferences? = null
    var themePreference : SwitchPreferenceCompat? = null
    var nicknamePreference : Preference? = null
    var noticePreference : Preference? = null
    var nickname : String? = null
    var actionbar : android.app.ActionBar? = null
    var nicknamePref : SharedPref? = null

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setPreferencesFromResource(R.xml.preference, rootKey)
        //addPreferencesFromResource(R.xml.preference)
        /*if (rootKey == null) {

            themePreference = findPreference("themeKey")
            nicknamePreference = findPreference("nicknameKey")
            noticePreference = findPreference("noticeKey")

            pref = activity?.let { PreferenceManager.getDefaultSharedPreferences(it)}
        }*/
        //themePreference = findPreference("themeKey")
        //nicknamePreference = findPreference("nicknameKey")
       // noticePreference = findPreference("noticeKey")

        //pref = activity?.let { PreferenceManager.getDefaultSharedPreferences(it)}
        /*sharedPref = this.context?.let { SharedPref(it) }
        if (sharedPref!!.loadNightModeState()) {
            context?.setTheme(R.style.darktheme)
        } else {
            context?.setTheme(R.style.AppTheme)
        }*/
    }*/
    
    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
    }*/

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        sharedPref = this.context?.let { SharedPref(it) }
        if (sharedPref!!.loadNightModeState()) {
            context?.setTheme(R.style.darktheme)
        } else {
            context?.setTheme(R.style.AppTheme)
        }

        setPreferencesFromResource(R.xml.preference, rootKey)

        themePreference = findPreference("themeKey")
        /*if (sharedPref!!.loadNightModeState()) {
            themePreference!!.isChecked = true
        }*/
        themePreference!!.setOnPreferenceChangeListener { preference, newValue ->
            var isChecked = false
            if (newValue as Boolean) {
                isChecked = newValue as Boolean
            }
            if (isChecked) {
                preferenceManager.sharedPreferences.edit().putBoolean("themeKey", true).apply()
                sharedPref!!.setNightModeState(true)
                restartApp()
            } else {
                preferenceManager.sharedPreferences.edit().putBoolean("themeKey", false).apply()
                sharedPref!!.setNightModeState(false)
                restartApp()
            }
            return@setOnPreferenceChangeListener true
        }


        nicknamePreference = findPreference("nicknameKey")

        //actionbar!!.title = "test"
        noticePreference = findPreference("noticeKey")

        //addPreferencesFromResource(R.xml.preference)
        if (rootKey == null) {
            pref = activity?.let { PreferenceManager.getDefaultSharedPreferences(it)}
        }
        nicknamePref = this.context?.let { SharedPref(it) }
        if (pref!!.getString("nicknameKey", "") != "") {
            nickname = pref!!.getString("nicknameKey","")
            nicknamePreference?.summary = "현재 설정된 닉네임은 $nickname 입니다"
        }

        //themePreference!!.setOnPreferenceChangeListener(prefListener)
    }
    
    val prefListener =
        SharedPreferences
            .OnSharedPreferenceChangeListener { sharedPreferences:SharedPreferences?, key:String? ->
        when (key) {
            "nicknameKey" -> {
                nickname = pref!!.getString("nicknameKey","")
                //nickname저장
                val pref = PreferenceManager.getDefaultSharedPreferences(activity)
                val editor = pref.edit()
                editor.putString("nicknameKey", nickname).apply()
                nicknamePreference?.summary = "현재 설정된 닉네임은 $nickname 입니다"
            }
        }
    }

    //테마 변경 시 적용을 위한 재시작
    fun restartApp() {
        val intent = Intent(context?.applicationContext, MainActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }

    // 리스너 등록
    override fun onResume() {
        super.onResume()
        val activity : FragmentActivity? = activity
        if (activity != null) {
            (activity as MainActivity).setActionBarTitle("$nickname 의 TodoList")
        }
        pref!!.registerOnSharedPreferenceChangeListener(prefListener)
    }

    // 리스너 해제
    override fun onPause() {
        super.onPause()
        val activity : FragmentActivity? = activity
        if (activity != null) {
            (activity as MainActivity).setActionBarTitle("$nickname 의 TodoList")
        }
        pref!!.unregisterOnSharedPreferenceChangeListener(prefListener)
    }

    /*companion object {
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
    }*/
}