package com.android.chatapp1.classes

import androidx.recyclerview.widget.RecyclerView
import com.android.chatapp1.R

class MessageAdapter(private val messages: List<ChatApplication.Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val messageText: android.widget.TextView = itemView.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): MessageViewHolder {
        // viewType: 0 = left (not current user), 1 = right (current user)
        val layout = if (viewType == 1) R.layout.item_message_right else R.layout.item_message_left
        val view = android.view.LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.content
    }

    override fun getItemViewType(position: Int): Int {
        // Return 1 for right (current user), 0 for left (others)
        return if (messages[position].isSentByCurrentUser) 1 else 0
    }

    override fun getItemCount() = messages.size
}