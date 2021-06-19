package com.example.shiristory.ui.story

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.common.*
import com.example.shiristory.service.common.Constants.BASE_WS_URL
import com.example.shiristory.service.common.MediaType
import com.example.shiristory.service.story.models.OutMessage
import com.example.shiristory.service.story.models.StoryEntry
import com.google.gson.Gson
import okhttp3.*
import okio.ByteString
import java.io.File

class StoryActivity : AppCompatActivity() {

    private val TAG = this.javaClass.name

    private val _context = this@StoryActivity
    private val _client: OkHttpClient = OkHttpClient()
    private lateinit var _ws : WebSocket

    private var _currentGroupId: String? = ""
    private val _model: StoryViewModel by viewModels()

    private lateinit var _storyAdapter: StoryAdapter
    private lateinit var _recyclerView: RecyclerView
    private lateinit var _textBoxView: EditText
    private lateinit var _sendMessageBtn: ImageButton
    private lateinit var _selectMediaBtn: ImageButton
    private lateinit var _inputVoicemailBtn: ImageButton
    private lateinit var _inputCameraBtn: ImageButton
    private lateinit var _inputVideoBtn: ImageButton

    private val _mediaUtil =
        MediaUtil(this)
    private var _mediaType: MediaType? = null
    private var _mediaUri: Uri? = null

    private var _recordAudioTrigger: Boolean = true


    private val _page: Int = 1
    private val _size: Int = 30


    val gson: Gson = Gson()

    inner class StoryWebSocketListener : WebSocketListener() {

        private val NORMAL_CLOSURE_STATUS = 1000
        private val SOCKET_TAG = "STORY_SOCKET"

        override fun onOpen(webSocket: WebSocket, response: Response) {
//            webSocket.send("Hello!")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(SOCKET_TAG, "Receiving : $text")

            if ("story_id" in text) {
                // parse JSON string to object
                val newMsg: StoryEntry = gson.fromJson(text, StoryEntry::class.java)

                runOnUiThread {
                    // Stuff that updates the UI
                    // display the item
                    _storyAdapter.addItem(newMsg)

                }


            }

        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d(SOCKET_TAG, "Receiving bytes : " + bytes.hex())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            Log.d(SOCKET_TAG, "Closing : $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d(SOCKET_TAG, "Error : " + t.message)
        }
    }

    // function to send messages through WS
    fun sendMessage(username: String, mediaType: MediaType, content: String){
        var newMsg: OutMessage = OutMessage(
            "chat_message",
            _currentGroupId!!,
            "vaaniscool",                       // hardcoded for now
            mediaType.id,
            content,
            mediaType.id,
            0
        )

        val jsonStr: String = gson.toJson(newMsg)

        _ws.send(jsonStr)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        // get the group id from the previous clicked item
        _currentGroupId = intent.getStringExtra("groupId")

        _recyclerView = findViewById(R.id.story_recyclerview)
        _recyclerView.layoutManager = LinearLayoutManager(_context)
        _sendMessageBtn = findViewById(R.id.send_btn)
        _textBoxView = findViewById(R.id.input_msg)
        _selectMediaBtn = findViewById(R.id.input_attachment)
        _inputCameraBtn = findViewById(R.id.input_camera)
        _inputVideoBtn = findViewById(R.id.input_video)
        _inputVoicemailBtn = findViewById(R.id.voicemail_btn)




        // set the title of the actionbar
        val actionBar = supportActionBar
        actionBar?.title = intent.getStringExtra("groupName")

        // display the messages
        if (_currentGroupId != null) {
            _model.getPostedStories(_page, _size, _currentGroupId!!).observe(this, Observer {
                if (it != null) {
                    Log.d(TAG, it.toString())
                    _storyAdapter = StoryAdapter(ArrayList(it), _model)
                    _recyclerView.adapter = _storyAdapter
                }
            })
        }

        // websocket connection handling
        // start the ws
        val request: Request =
            Request.Builder().url(BASE_WS_URL + "ws/$_currentGroupId/stories").build()
        val listener = StoryWebSocketListener()
        _ws = _client.newWebSocket(request, listener)

//        _client.dispatcher().executorService().shutdown()

        // set click listener for send button
        _sendMessageBtn.setOnClickListener {
            // send message
            sendMessage("vaaniscool", MediaType.TEXT, _textBoxView.text.toString())

            // empty the text box
            _textBoxView.text.clear()

            ToolKits.hideSoftKeyboard(_context, _textBoxView)
        }

        // click listener for select attachment
        _selectMediaBtn.setOnClickListener {
            _mediaUtil.selectMedia()
        }

        // click listener for camera
        _inputCameraBtn.setOnClickListener {
            _mediaUtil.takePhoto()
        }

        // click listener for video
        _inputVideoBtn.setOnClickListener {
            _mediaUtil.recordVideo()
        }

        // click listener for audio
        // the audio recorder is embedded, thus cannot use a delegated operation
        _inputVoicemailBtn.setOnClickListener {
            _mediaUtil.recordAudio(_recordAudioTrigger)

            if(_recordAudioTrigger)
                _inputVoicemailBtn.background = ContextCompat.getDrawable(_context, R.drawable.round_corner_red)
            else{
                _inputVoicemailBtn.background = ContextCompat.getDrawable(_context, R.drawable.round_corner_green)
                Log.d(TAG, "uploading audio")

                _mediaType = MediaType.AUDIO
                _mediaUri = _mediaUtil.getAudioUri()

                _model.uploadFile(_mediaType!!,_mediaUri!!).observe(this, Observer {
                    if (it != null) {
                        Log.d(TAG, it.fileUrl)
                        sendMessage("vaaniscool", _mediaType!!, it.fileUrl)
                    }
                })
            }




            _recordAudioTrigger = !_recordAudioTrigger
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.story_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_story_goto_settings -> {
                val settingsIntent = Intent(_context, StorySettingsActivity::class.java).apply {
                    putExtra("groupId", _currentGroupId)
                }
                ContextCompat.startActivity(_context, settingsIntent, null)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            RequestCodes.REQUEST_MEDIA_PICKER_SELECT, RequestCodes.REQUEST_MEDIA_VIDEO_RECORD -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    _mediaUri = data.data
                    Log.d(TAG, "raw media uri:" + _mediaUri?.path!!)

                    _mediaUri?.also {
                        if (it.toString().contains("image")) {
                            _mediaType = MediaType.IMAGE

                            val previewIntent = Intent(_context, SendPreviewActivity::class.java).apply{
                                putExtra("URI", _mediaUri.toString())
                                putExtra("type", MediaType.IMAGE.id)
                            }
                            startActivityForResult(previewIntent, RequestCodes.REQUEST_PREVIEW_IMAGE)

                        } else if (it.toString().contains("video")) {
                            _mediaType = MediaType.VIDEO

                            val previewIntent = Intent(_context, SendPreviewActivity::class.java).apply{
                                putExtra("URI", _mediaUri.toString())
                                putExtra("type", MediaType.VIDEO.id)
                            }
                            startActivityForResult(previewIntent, RequestCodes.REQUEST_PREVIEW_VIDEO)
                        }

                        val file: File = FileUtil.from(this, it)

                        _mediaUri = Uri.parse(file.path)
                        Log.d(TAG, "parsed media uri:" + _mediaUri?.path!!)
                    }
                }
            }

            RequestCodes.REQUEST_MEDIA_CAMERA_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    _mediaUri = _mediaUtil.getPhotoUri()
                    _mediaType = MediaType.IMAGE

                    // TODO: do ui stuff for captured image
                    val previewIntent = Intent(_context, SendPreviewActivity::class.java).apply{
                        putExtra("URI", _mediaUri.toString())
                        putExtra("type", MediaType.IMAGE.id)
                    }
                    startActivityForResult(previewIntent, RequestCodes.REQUEST_PREVIEW_IMAGE)

                    Log.d(TAG, "raw camera capture uri:" + _mediaUri?.path!!)
                }
            }

            RequestCodes.REQUEST_PREVIEW_IMAGE , RequestCodes.REQUEST_PREVIEW_VIDEO-> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Log.d(TAG, "uploading images")
                    _model.uploadFile(_mediaType!!,_mediaUri!!).observe(this, Observer {
                        if (it != null) {
                            Log.d(TAG, it.fileUrl)
                            sendMessage("vaaniscool", _mediaType!!, it.fileUrl)
                        }
                    })

                }
            }


        }

        super.onActivityResult(requestCode, resultCode, data)
    }

}