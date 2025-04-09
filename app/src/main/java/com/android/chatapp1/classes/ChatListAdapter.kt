package com.android.chatapp1.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.chatapp1.R

class ChatListAdapter(
    private val chatList: List<ChatListItem>,
    private val onItemClick: (ChatListItem) -> Unit // Callback for item clicks
) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val otherUsernameTextView: TextView = itemView.findViewById(R.id.otherUsernameTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(chatList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentItem = chatList[position]
        holder.otherUsernameTextView.text = currentItem.otherUsername
    }

    override fun getItemCount() = chatList.size
}