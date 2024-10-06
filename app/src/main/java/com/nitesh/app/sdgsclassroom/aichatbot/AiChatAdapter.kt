package com.nitesh.app.sdgsclassroom.aichatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nitesh.app.sdgsclassroom.R

class AiChatAdapter(private val messages: List<Pair<String, String>>) :
    RecyclerView.Adapter<AiChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val senderTextView: TextView = view.findViewById(R.id.senderTextView)
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ai_chat_items, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val (sender, message) = messages[position]
        holder.senderTextView.text = sender
        holder.messageTextView.text = message
    }

    override fun getItemCount() = messages.size
}
