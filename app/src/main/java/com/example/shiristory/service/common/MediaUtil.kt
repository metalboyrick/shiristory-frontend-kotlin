package com.example.shiristory.service.common

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MediaUtil(private val _activity: Activity) {

    private lateinit var _photoUri: Uri
    private lateinit var _audioUri: Uri
    val audioRecorder: MediaRecorder = MediaRecorder()

    fun getPhotoUri(): Uri = _photoUri
    fun getAudioUri(): Uri = _audioUri

    // Launch camera intent to capture an image
    // The image will be saved in external cache folder of the app
    // Use getPhotoUri to obtain the saved image uri in OnActivityResult
    // uri result
    // uri://storage/emulated/0/Android/data/com.example.shiristory/cache/JPEG_20210602_031106_tmp1697457150435104809.jpg
    fun takePhoto() {
        requestCameraPermission()
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val photoFile = File.createTempFile(
                "JPEG_${timeStamp}_tmp", /* prefix */
                ".jpg", /* suffix */
                _activity.externalCacheDir /* directory */
            )

            // Continue only if the File was successfully created
            photoFile.also {
                _photoUri = FileProvider.getUriForFile(
                    _activity,
                    "com.example.shiristory.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, _photoUri)

                ActivityCompat.startActivityForResult(
                    _activity, takePictureIntent,
                    RequestCodes.REQUEST_MEDIA_CAMERA_CAPTURE, null
                )
            }
        }
    }

    // Recorded video uri can be obtained by the returned intent.data in OnActivityResult
    // uri result
    // uri:/external/video/media/60
    fun recordVideo() {
        requestCameraPermission()
        ActivityCompat.startActivityForResult(
            _activity,
            Intent(MediaStore.ACTION_VIDEO_CAPTURE),
            RequestCodes.REQUEST_MEDIA_VIDEO_RECORD, null
        )
    }

    // Media uri can be obtained by the returned intent.data in OnActivityResult
    // uri result
    // uri:/document/image:37
    // uri:/document/video:58
    fun selectMedia() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        val mimetypes = arrayOf("image/*", "video/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        ActivityCompat.startActivityForResult(
            _activity,
            intent,
            RequestCodes.REQUEST_MEDIA_PICKER_SELECT, null
        )
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            _activity,
            arrayOf(Manifest.permission.CAMERA),
            RequestCodes.REQUEST_CAMERA_PERMISSION
        )
    }

    // audio recording implementations
    private fun requestMicPermission() {
        ActivityCompat.requestPermissions(
            _activity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            RequestCodes.REQUEST_AUDIO_PERMISSION
        )
    }

    // toggle functions determine to start or stop recording
    @RequiresApi(Build.VERSION_CODES.O)
    fun recordAudio(toggleRecord: Boolean){
        if(toggleRecord == true){
            requestMicPermission()

            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val audioFile = File.createTempFile(
                "3GP_${timeStamp}_tmp", /* prefix */
                ".3gp", /* suffix */
                _activity.externalCacheDir /* directory */
            )

            audioFile.also{
                _audioUri = FileProvider.getUriForFile(
                    _activity,
                    "com.example.shiristory.fileprovider",
                    it
                )

                audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                audioRecorder.setOutputFile(audioFile)
                audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                audioRecorder.prepare()
                audioRecorder.start()
            }
        } else   {
            audioRecorder.stop()
            audioRecorder.release()
        }
    }
}