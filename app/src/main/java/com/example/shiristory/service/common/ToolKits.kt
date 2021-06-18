package com.example.shiristory.service.common

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ToolKits {
    companion object {
        fun hideSoftKeyboard(context: Context, view: View) {
            val imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun parseMultiPartFile(path: Uri, mediaType: String): MultipartBody.Part {
            val file = File(path.path!!)

            return MultipartBody.Part.createFormData(
                file.name, file.name, RequestBody.create(
                    MediaType.parse(mediaType), file
                )
            )
        }

        fun parseMultiPartFormData(data: String): RequestBody {
            return RequestBody.create(okhttp3.MediaType.parse("text/plain"), data)
        }
    }
}