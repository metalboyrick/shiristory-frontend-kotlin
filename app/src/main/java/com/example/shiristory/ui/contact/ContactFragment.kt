package com.example.shiristory.ui.contact

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.Layout
import android.util.Log
import android.view.*
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.user.models.User


class ContactFragment : Fragment() {

    private val _model: ContactViewModel by viewModels()
    private lateinit var _recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_contact, container, false)
        setHasOptionsMenu(true);
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _recyclerView = view.findViewById(R.id.contact_recyclerview)

        _recyclerView.layoutManager = LinearLayoutManager(context)

        getFriends()
    }

    override fun onCreateOptionsMenu(
        menu: Menu, inflater: MenuInflater
    ) {
        inflater.inflate(R.menu.contact_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()

        getFriends()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // id of menu options pressed
        val id: Int = item.getItemId()
        var friend_op_name: String = "Default"
        var layoutId: Int = R.layout.friend_search_dialog

        // change layout id according to operation
        if (id == R.id.search_friend) {
            Log.d("search friend", "pressed")
            friend_op_name = "Search"
            layoutId = R.layout.friend_search_dialog

        } else if (id == R.id.add_friend) {
            Log.d("add friend", "pressed")
            friend_op_name = "Add"
            layoutId = R.layout.friend_add_dialog
        }

        // build a new dialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(friend_op_name)

        // use layout for dialog
        val viewInflated: View = LayoutInflater.from(context)
            .inflate(layoutId, view as ViewGroup?, false)

        // get the only input box in both search and add friend layout
        val input = viewInflated.findViewById(R.id.input) as EditText
        input.inputType = InputType.TYPE_CLASS_TEXT

        // get other components
        val submit_button: Button = viewInflated.findViewById(R.id.submit)
        val exit_button: Button = viewInflated.findViewById(R.id.exit)
        val server_message : TextView = viewInflated.findViewById(R.id.message)

        builder.setView(viewInflated)

        // show the dialog
        var dialog : AlertDialog = builder.show()

        exit_button.setOnClickListener {
            dialog.dismiss()
        }

        submit_button.setOnClickListener {
            if(input.text.length == 0){
                // lambda function
                return@setOnClickListener
            }
            Log.d("Friend OP", friend_op_name)
            if (friend_op_name == "Search") {
                // call the search friend API
                var res : LiveData<ArrayList<User>> = _model.searchFriend(input.text.toString())

                res.observe(viewLifecycleOwner, Observer {
                        if(it != null) {
                            // reinitialize adapter with new list of friends
                            _recyclerView.adapter = FriendAdapter(it, _model)

                            // close dialog
                            dialog.dismiss()
                        }
                    })
            }
            else if (friend_op_name == "Add") {
                // call the add friend API
                _model.addFriend(input.text.toString())
                    .observe(viewLifecycleOwner, Observer {
                        if(it != null) {
                            Log.d("status code received by observer", it[0])

                            val statusCode = Integer.parseInt(it[0])
                            val message: String? = it[1]

                            Log.d("message received by observer", message!!)

                            // close dialog if successful
                            if (statusCode == 200) {
                                getFriends()
                                dialog.dismiss()
                            }

                            // show server response
                            server_message.setText(message)
                        }
                    })
            }
        }



        return super.onOptionsItemSelected(item)
    }

    fun getFriends(){
        var res : LiveData<ArrayList<User>> = _model.getFriends()

        res.observe(viewLifecycleOwner, Observer {
            if(it != null){
                _recyclerView.adapter = FriendAdapter(it, _model)
            }

        })
    }


}
