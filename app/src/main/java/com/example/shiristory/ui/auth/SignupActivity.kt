package com.example.shiristory.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
                if (response.code() == 200) {
                    _signUpResponse.value = response.body()
                } else {
                    sign_up_button?.progress = -1
                    Timer().schedule(3000) {
                        sign_up_button?.progress = 0
                    }
                }

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
            // if sign up successful
            if (it != null) {
                val returnIntent = Intent()
                returnIntent.putExtra("username", username)
                returnIntent.putExtra("password", password)
                setResult(RESULT_OK, returnIntent)
                sign_up_button!!.progress = 100
                finish()
            }
        })

        sign_up_button!!.progress = 1

    }

    fun cancelSignUp(view: View){
        val returnIntent = Intent()
        setResult(RESULT_CANCELED, returnIntent)
        finish()
    }
}