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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.getItemId()
        var friend_op_name: String = "Default"
        var layoutId: Int = R.layout.friend_search_dialog

        if (id == R.id.search_friend) {
            Log.d("search friend", "pressed")
            friend_op_name = "Search"
            layoutId = R.layout.friend_search_dialog

        } else if (id == R.id.add_friend) {
            Log.d("add friend", "pressed")
            friend_op_name = "Add"
            layoutId = R.layout.friend_add_dialog
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(friend_op_name)

        val viewInflated: View = LayoutInflater.from(context)
            .inflate(layoutId, view as ViewGroup?, false)

        val input = viewInflated.findViewById(R.id.input) as EditText
        input.inputType = InputType.TYPE_CLASS_TEXT

        val submit_button: Button = viewInflated.findViewById(R.id.submit)
        val exit_button: Button = viewInflated.findViewById(R.id.exit)
        val server_message : TextView = viewInflated.findViewById(R.id.message)

        builder.setView(viewInflated)

        var dialog : AlertDialog = builder.show()

        exit_button.setOnClickListener {
            dialog.dismiss()
        }

        submit_button.setOnClickListener {
            val v : View = it
            Log.d("Friend OP", friend_op_name)
            if (friend_op_name == "Search") {
                Log.d("search friend", "Search friend API called")
                //_model.addFriend(input.text.toString())
            } else if (friend_op_name == "Add") {
                _model.addFriend(input.text.toString())
                    .observe(viewLifecycleOwner, Observer {
                        val statusCode = it
                        if (statusCode == 200) {
                            Log.d("add friend status","200")
                            getFriends()
                            dialog.dismiss()
                        }
                    })
            }
        }



        return super.onOptionsItemSelected(item)
    }

    private fun getFriends(){
        _model.getFriends().observe(viewLifecycleOwner, Observer {
            _recyclerView.adapter = FriendAdapter(it, _model)
        })
    }


}
