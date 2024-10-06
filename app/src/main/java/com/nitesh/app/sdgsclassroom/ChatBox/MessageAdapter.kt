package com.nitesh.app.sdgsclassroom.ChatBox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.nitesh.app.sdgsclassroom.R


class MessageAdapter(val context : Context, val messageList : ArrayList<Message>)
    : RecyclerView.Adapter<ViewHolder>() {


    val ITEM_RECIVE = 1
    val ITEM_SEND = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 1){
            val view = LayoutInflater.from(context).inflate(R.layout.recieve_layout,parent,false)
            return ReciveViewHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.send_layout,parent,false)
            return SendViewHolder(view)
        }

    }

    override fun getItemCount(): Int {

        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderid)){
            return ITEM_SEND
        }else{
            return ITEM_RECIVE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            viewHolder.sendtv.text = currentMessage.message
            viewHolder.senderNameTv.text = currentMessage.senderName // Display sender's name
        } else {
            val viewHolder = holder as ReciveViewHolder
            viewHolder.recivetv.text = currentMessage.message
            viewHolder.senderNameTv.text = currentMessage.senderName // Display sender's name
        }
    }

    class SendViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
        val sendtv = itemview.findViewById<TextView>(R.id.send_tv)
        val senderNameTv = itemView.findViewById<TextView>(R.id.sender_name_tv)

    }

    class ReciveViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
        val recivetv = itemview.findViewById<TextView>(R.id.recive_tv)
        val senderNameTv = itemView.findViewById<TextView>(R.id.reciever_name_tv)

    }
}

