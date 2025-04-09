package com.android.chatapp1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.chatapp1.R
import com.android.chatapp1.classes.ChatApplication
import com.android.chatapp1.classes.ChatListAdapter
import com.android.chatapp1.classes.ChatListItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var chatListRecyclerView: RecyclerView
    private lateinit var emptyChatListTextView: TextView
    private lateinit var startNewChatFab: FloatingActionButton
    private lateinit var logoutButton: Button
    private lateinit var profileButton: Button
    private lateinit var chatListAdapter: ChatListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        chatListRecyclerView = findViewById(R.id.chatListRecyclerView)
        emptyChatListTextView = findViewById(R.id.emptyChatListTextView)
        startNewChatFab = findViewById(R.id.startNewChatFab)
        logoutButton = findViewById(R.id.logoutButton)
        profileButton = findViewById(R.id.profileButton)

        chatListRecyclerView.layoutManager = LinearLayoutManager(this)

        val chatConversations = ChatApplication.getConversations().map { ChatListItem(it.withUser) }
        chatListAdapter = ChatListAdapter(chatConversations) { chatItem ->
            val intent = Intent(this, ChatScreenActivity::class.java)
            intent.putExtra("otherUsername", chatItem.otherUsername)
            startActivity(intent)
        }
        chatListRecyclerView.adapter = chatListAdapter

        updateChatListVisibility()

        startNewChatFab.setOnClickListener {
            val intent = Intent(this, StartNewChatActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            ChatApplication.logoutUser()
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }

        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateChatList()
    }

    private fun updateChatList() {
        val chatConversations = ChatApplication.getConversations().map { ChatListItem(it.withUser) }
        chatListAdapter = ChatListAdapter(chatConversations) { chatItem ->
            val intent = Intent(this, ChatScreenActivity::class.java)
            intent.putExtra("otherUsername", chatItem.otherUsername)
            startActivity(intent)
        }
        chatListRecyclerView.adapter = chatListAdapter
        updateChatListVisibility()
    }

    private fun updateChatListVisibility() {
        Log.d("MainActivity", "Adapter item count: ${chatListAdapter.itemCount}")
        if (chatListAdapter.itemCount == 0) {
            chatListRecyclerView.visibility = View.GONE
            emptyChatListTextView.visibility = View.VISIBLE
        } else {
            chatListRecyclerView.visibility = View.VISIBLE
            emptyChatListTextView.visibility = View.GONE
        }
    }
}