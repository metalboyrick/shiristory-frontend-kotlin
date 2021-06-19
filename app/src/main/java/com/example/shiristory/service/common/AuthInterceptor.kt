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

        val apiService: AuthenticationApiService = RetrofitBuilder.authenticationApiService

        val sharedPref: SharedPreferences = getDefaultSharedPreferences(_context)

        val accessToken: String? = sharedPref.getString(R.string.jwt_access_key.toString(), null)

        // Creating the origin request object
        var request: Request = chain.request()
        Log.d(TAG, "Intercept request: " + request.url())

        // Add auth header if access token is available
        if (accessToken != null) {
            Log.d(TAG, "Add accessToken to request: " + request.url())
            request = addTokenToRequest(request, accessToken)
        }

        Log.d(TAG, "Proceed request: " + request.url())
        var response: Response = chain.proceed(request)
        Log.d(TAG, "Response code: " + response.code())


        if (response.code() != 200 && !(request.url().toString().contains("/login")) ) {

            // If authentication error (token expired)
            if (response.code() == 401) {

                val refreshToken: String? =
                    sharedPref.getString(R.string.jwt_refresh_key.toString(), null)

                // If there is no refresh token
                // Redirect to login page
                if (refreshToken == null) {
                    Log.d(TAG, "User not logged in.")
                    redirectToLogin()
                    return response
                }

                val data = mapOf("refresh" to refreshToken)

                // Get new access token
                val call = apiService.getAccessToken(json_body = Gson().toJson(data))
                val refreshTokenResponse = call.execute()

                // If refresh token is invalid
                // Redirect to login page
                if (refreshTokenResponse.code() == 401) {
                    Log.d(TAG, "Invalid refresh token.")
                    redirectToLogin()
                    return response
                }
                // Else save access token
                else {
                    val editor = sharedPref.edit()
                    editor.putString(
                        R.string.jwt_access_key.toString(),
                        refreshTokenResponse.body()?.access
                    )
                    editor.apply()
                }

                // Retry initial request
                Log.d(TAG, "Add new accessToken to request: " + request.url())
                val retryRequest = addTokenToRequest(request, refreshTokenResponse.body()?.access!!)
                Log.d(TAG, "Retry request: " + request.url())
                response.close()
                response = chain.proceed(retryRequest)
                Log.d(TAG, "Response code: " + response.code())

            } else {
                //handle other errors here
                Log.e(TAG, "Something bad happened. Code: " + response.code().toString())
            }
        }

        Log.d(TAG, "Request completed.\n")
        return response

    }

    private fun redirectToLogin() {
        Log.d(TAG, "Redirect to login page.")
        val intent = Intent(_context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        _context?.startActivity(intent)
    }

    private fun addTokenToRequest(request: Request, token: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
    }
}