package com.example.shiristory.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.dd.processbutton.iml.ActionProcessButton
import com.example.shiristory.R
import com.example.shiristory.service.authentication.AuthenticationApiService
import com.example.shiristory.service.authentication.models.Token
import com.example.shiristory.service.common.RetrofitBuilder
import com.google.gson.Gson
import com.rengwuxian.materialedittext.MaterialEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule


class LoginActivity : AppCompatActivity() {
    private var login_button: ActionProcessButton? = null
    private var login_username: MaterialEditText? = null
    private var login_password: MaterialEditText? = null

    // API related
    private var _loginResponse: MutableLiveData<Token> = MutableLiveData<Token>()
    private val _service: AuthenticationApiService = RetrofitBuilder.authenticationApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initialize components
        login_button = findViewById<View>(R.id.login_button) as ActionProcessButton?
        login_button!!.setMode(ActionProcessButton.Mode.ENDLESS)
        login_username = findViewById(R.id.login_username)
        login_password = findViewById(R.id.login_password)

    }

    fun _login(credentials: Map<String, String>): LiveData<Token> {

        _loginResponse.value = null

        val call: Call<Token> =
            _service.login(json_body = Gson().toJson(credentials))

        call.enqueue(object : Callback<Token> {

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Log.e("login", t.message!!)
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.code() == 200) {
                    _loginResponse.value = response.body()
                } else {
                    login_button?.progress = -1
                    Timer().schedule(3000){
                        login_button?.progress = 0
                    }
                }

            }

        })

        return _loginResponse
    }

    fun login(view: View) {

        val username: String = login_username!!.getText().toString()
        val password: String = login_password!!.getText().toString()

        if (username.isEmpty() || password.isEmpty()) {
            return
        }

        val credentials = mapOf("username" to username, "password" to password)

        _login(credentials).observe(this, Observer {
            if (it != null) {
                val sharedPref: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this)
                val editor = sharedPref.edit()

                Log.d("login access token", it.access!!)

                editor.putString(R.string.jwt_access_key.toString(), it.access)
                editor.putString(R.string.jwt_refresh_key.toString(), it.refresh)
                editor.apply()

                login_button!!.progress = 100

                finish()
            }
        })

        login_button!!.progress = 1

    }

    fun goSignUp(view:View){
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
        finish()
    }
}