package com.example.shiristory.ui.auth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.dd.processbutton.iml.ActionProcessButton
import com.example.shiristory.R
import com.example.shiristory.service.authentication.AuthenticationApiService
import com.example.shiristory.service.authentication.models.Token
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.common.models.APIError
import com.example.shiristory.service.timeline.TimelineApiService
import com.example.shiristory.service.timeline.models.Post
import com.example.shiristory.service.user.UserApiService
import com.example.shiristory.service.user.models.User
import com.google.gson.Gson
import com.rengwuxian.materialedittext.MaterialEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private var btnSignIn:ActionProcessButton? = null
    private var login_username: MaterialEditText? = null
    private var login_password: MaterialEditText? = null

    // shared preference related
    var sharedpreferences: SharedPreferences? = null
    val shiristory_PREFERENCES = "shiristory"

    // API related
    private var _loginResponse: MutableLiveData<Token> = MutableLiveData<Token>()
    private val _service: AuthenticationApiService = RetrofitBuilder.authenticationApiService

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

    fun _login(credentials: Map<String,String>):LiveData<Token> {

        _loginResponse.value = null

        val call: Call<Token> =
            _service.login(json_body = Gson().toJson(credentials))

        call.enqueue(object : Callback<Token> {

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Log.e("login", t.message!!)
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                _loginResponse.value = response.body()
            }

        })

        return _loginResponse
    }

    fun login(view: View){

        val username: String = login_username!!.getText().toString()
        val password: String = login_password!!.getText().toString()

        if (username.length == 0 || password.length == 0){
            return
        }

        val credentials = mapOf("username" to username,"password" to password)

        _login(credentials).observe(this, Observer {
            if(it != null){
                val sharedPref: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this)
                val editor = sharedPref.edit()

                Log.d("login access token",it.access!!)

                editor.putString(R.string.jwt_access_key.toString(), it.access)
                editor.putString(R.string.jwt_refresh_key.toString(), it.refresh)
                editor.apply()

                btnSignIn!!.setProgress(100)

                finish()
            }
        })

        btnSignIn!!.setProgress(1)

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