package com.example.shiristory.ui.auth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dd.processbutton.iml.ActionProcessButton
import com.example.shiristory.R
import com.rengwuxian.materialedittext.MaterialEditText


class LoginActivity : AppCompatActivity() {
    private var btnSignIn:ActionProcessButton? = null
    private var login_username: MaterialEditText? = null
    private var login_password: MaterialEditText? = null
    var sharedpreferences: SharedPreferences? = null
    val shiristory_PREFERENCES = "shiristory"
    val pref_username_field = "username"
    val pref_password_field = "username"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initialize shared preferences
        sharedpreferences = getSharedPreferences(shiristory_PREFERENCES, Context.MODE_PRIVATE);

        // initialize components
        btnSignIn = findViewById<View>(R.id.btnSignIn) as ActionProcessButton?
        btnSignIn!!.setMode(ActionProcessButton.Mode.ENDLESS)
        login_username = findViewById(R.id.login_username)
        login_password = findViewById(R.id.login_password)

    }

    fun login(view: View){

        // call login API, and use the following as callback
//        val username: String = login_username!!.getText().toString()
//        val password: String = login_password!!.getText().toString()
//
//        val editor = sharedpreferences!!.edit()
//
//        editor.putString(pref_username_field, username)
//        editor.putString(pref_password_field, password)
//        editor.apply()
//
//        btnSignIn!!.setProgress(1)
    }
}