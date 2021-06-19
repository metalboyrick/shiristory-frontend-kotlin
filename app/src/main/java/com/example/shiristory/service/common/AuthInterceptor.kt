package com.example.shiristory.service.common

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.shiristory.R
import com.example.shiristory.Shiristory
import com.example.shiristory.service.authentication.AuthenticationApiService
import com.example.shiristory.ui.auth.LoginActivity
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class AuthInterceptor : Interceptor {

    private val TAG: String = this.javaClass.name
    private val _context: Context? = Shiristory.instance



    override fun intercept(chain: Interceptor.Chain): Response {

        val sharedPref: SharedPreferences = getDefaultSharedPreferences(_context)


        val accessToken: String? = sharedPref.getString(R.string.jwt_access_key.toString(), null)

        // Creating the origin request object
        var request: Request = chain.request()

        val _service: AuthenticationApiService = RetrofitBuilder.authenticationApiService

        Log.d(TAG, "Intercept request: " + request.url())

        // Add auth header
        if (accessToken != null) {
            request = request.newBuilder()
                .addHeader("Authorization", accessToken)
                .build()
        }

        // Executing the request by invoking procced function
        // on the chain with the request object
        var response: Response = chain.proceed(request)

        // Verifying the whether the request is executed or not
        if (response.code() != 200) {
            if (response.code() == 401) {
                // This block executes when the token is exprie
                var refreshToken: String? =
                    sharedPref.getString(R.string.jwt_refresh_key.toString(), null)

                // If there is no refresh token
                // Redirect to login page
                if (refreshToken == null) {
                    Log.d(TAG, "User not logged in.")
                    Log.d(TAG, "Redirect to login page.")
                    val intent = Intent(_context, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    _context?.startActivity(intent)
                    return response
                }

                val data = mapOf("refresh" to refreshToken)
                Log.d(TAG, Gson().toJson(data))

                // get new access token
                val call = _service.refresh(json_body = Gson().toJson(data))

                // Invoking the service call
                val refreshTokenResponse = call.execute()

                if (refreshTokenResponse.code() == 401) {
                    Log.d(TAG, "Invalid refresh token")
                    Log.d(TAG, "Redirect to login page.")
                    val intent = Intent(_context, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    _context?.startActivity(intent)
                    return response
                }

                //saving the access token in preference
                val editor = sharedPref.edit()
                editor.putString(
                    R.string.jwt_access_key.toString(),
                    refreshTokenResponse.body()?.access
                )
                editor.apply()

                //replacing the new token in the origin request object
                request = request.newBuilder()
                    .addHeader("Authorization", refreshTokenResponse.body()?.access!!)
                    .build()

                // Reinitialize the origin request
                response = chain.proceed(request)
            } else {
                //handle other errors here
                Log.e(TAG, "Something bad happened. Code: " + response.code().toString())
            }
        }

        return response

    }
}