package com.example.shiristory.service.common

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.shiristory.R
import com.example.shiristory.Shiristory
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val context: Context? = Shiristory().getAppContext()
        val sharedPref: SharedPreferences = getDefaultSharedPreferences(context)

        var accessToken: String? = sharedPref.getString(R.string.jwt_access_key.toString(), null)

        // Creating the origin request object
        var request: Request = chain.request()

        // Add auth header
        if ( accessToken != null ){
            request = request.newBuilder()
                .header("Authorization", accessToken)
                .build()
        }

        // Executing the request by invoking procced function
        // on the chain with the request object
        var response: Response = chain.proceed(request)

        // Verifying the whether the request is executed or not
        if (response.code() != 200) {
            if (response.code() == 401) {
                // This block executes when the token is expried

                // creating get access token service call object
                val call = apiService.requestNewToken(getrefreshTokenBody())

                // Invoking the service call
                val refreshTokenResponse = call.execute()

                //saving the access token in preference
                SharedPrefManager.mTokenValue = refreshTokenResponse.token

                //replacing the new token in the origin request object
                request = request.newBuilder()
                    .header("Authorization", refreshTokenResponse.token)
                    .build()

                //reintializing the origin request
                response = chain.proceed(request)
            } else {
                //handle other errors here
            }
        }

        return response

    }
}