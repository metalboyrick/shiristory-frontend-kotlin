package com.example.shiristory

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.dd.processbutton.iml.ActionProcessButton
import com.example.shiristory.service.authentication.AuthenticationApiService
import com.example.shiristory.service.authentication.models.Token
import com.example.shiristory.service.common.RetrofitBuilder
import com.google.gson.Gson
import com.rengwuxian.materialedittext.MaterialEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private var sign_up_button: ActionProcessButton? = null
    private var sign_up_username: MaterialEditText? = null
    private var sign_up_password: MaterialEditText? = null
    private var sign_up_email: MaterialEditText? = null

    // API related
    private var _signUpResponse: MutableLiveData<Token> = MutableLiveData<Token>()
    private val _service: AuthenticationApiService = RetrofitBuilder.authenticationApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // initialize components
        sign_up_button = findViewById<View>(R.id.sign_up_button) as ActionProcessButton?
        sign_up_button!!.setMode(ActionProcessButton.Mode.ENDLESS)
        sign_up_username = findViewById(R.id.sign_up_username)
        sign_up_password = findViewById(R.id.sign_up_password)
        sign_up_email = findViewById(R.id.sign_up_email)

    }

    fun _signUp(credentials: Map<String, String>): LiveData<Token> {

        _signUpResponse.value = null

        val call: Call<Token> =
            _service.signUp(json_body = Gson().toJson(credentials))

        call.enqueue(object : Callback<Token> {

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Log.e("sign up", t.message!!)
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                _signUpResponse.value = response.body()
            }

        })

        return _signUpResponse
    }

    fun signUp(view: View) {

        val username: String = sign_up_username!!.getText().toString()
        val password: String = sign_up_password!!.getText().toString()
        val email: String = sign_up_email!!.getText().toString()

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            return
        }

        val credentials = mapOf("username" to username, "password" to password, "email" to email)

        _signUp(credentials).observe(this, Observer {
            if (it != null) {
                val sharedPref: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this)
                val editor = sharedPref.edit()

                editor.putString(R.string.jwt_access_key.toString(), it.access)
                editor.putString(R.string.jwt_refresh_key.toString(), it.refresh)
                editor.apply()

                sign_up_button!!.progress = 100

                finish()
            }
        })

        sign_up_button!!.progress = 1

    }
}