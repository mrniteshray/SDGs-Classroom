package com.nitesh.app.sdgsclassroom.ChatBox

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nitesh.app.sdgsclassroom.R
import com.nitesh.app.sdgsclassroom.databinding.FragmentChatBoxBinding
import com.nitesh.app.sdgsclassroom.databinding.FragmentHomeBinding

class ChatBox : Fragment() {

    private lateinit var messageList : ArrayList<Message>
    private lateinit var adapter: MessageAdapter
    private lateinit var dbref : DatabaseReference

    private var _binding : FragmentChatBoxBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBoxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageList = ArrayList<Message>()
        adapter = MessageAdapter(requireContext(), messageList)
        binding.chatRvc.layoutManager = LinearLayoutManager(requireContext())
        binding.chatRvc.adapter = adapter
        // Set up Firebase Database reference for community chat
        dbref = FirebaseDatabase.getInstance().reference.child("CommunityChats")

        // Listen for new messages in the community chat
        dbref.child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (snap in snapshot.children) {
                    val message = snap.getValue(Message::class.java)
                    message?.let {
                        messageList.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
            }
        })

        // Send message to community chat
        binding.btnSend.setOnClickListener {
            val messageText = binding.messagebox.text.toString()
            val senderUid = FirebaseAuth.getInstance().currentUser?.uid
            val senderName = FirebaseAuth.getInstance().currentUser?.displayName  // Retrieve user's name

            if (messageText.isNotEmpty()) {
                val messageObject = Message(messageText, senderUid!!, senderName!!)
                dbref.child("messages").push().setValue(messageObject)
                binding.messagebox.setText("")
            }
        }

    }


}


