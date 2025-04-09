package com.android.chatapp1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.android.chatapp1.R
import com.android.chatapp1.classes.ChatApplication

class StartNewChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_new_chat)

        val userListView = findViewById<ListView>(R.id.userListView)
        val users = ChatApplication.getRegisteredUsers()
        userListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, users)

        userListView.setOnItemClickListener { _, _, position, _ ->
            val withUser = users[position]
            ChatApplication.startNewChat(withUser)
            val intent = Intent(this, ChatScreenActivity::class.java)
            intent.putExtra("otherUsername", withUser)
            startActivity(intent)
            finish()
        }
    }
}