package com.android.chatapp1.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.chatapp1.R
import com.android.chatapp1.classes.ChatApplication
import com.android.chatapp1.classes.MessageAdapter

class ChatScreenActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var noMessagesTextView: TextView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var otherUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)

        // Set up the Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back button
        otherUsername = intent.getStringExtra("otherUsername")
            ?: throw IllegalStateException("No user specified")
        supportActionBar?.title = otherUsername // Set the title to the other user's name

        messageRecyclerView = findViewById(R.id.messageRecyclerView)
        noMessagesTextView = findViewById(R.id.noMessagesTextView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        updateMessages()

        sendButton.setOnClickListener {
            val content = messageInput.text.toString().trim()
            if (content.isNotEmpty()) {
                ChatApplication.sendMessage(otherUsername, content)
                messageInput.text.clear()
                updateMessages()
            }
        }
    }

    private fun updateMessages() {
        val conversation = ChatApplication.getConversations().find { it.withUser == otherUsername }
        val messages = conversation?.messages ?: emptyList()
        messageAdapter = MessageAdapter(messages)
        messageRecyclerView.adapter = messageAdapter

        if (messages.isEmpty()) {
            messageRecyclerView.visibility = TextView.GONE
            noMessagesTextView.visibility = TextView.VISIBLE
        } else {
            messageRecyclerView.visibility = TextView.VISIBLE
            noMessagesTextView.visibility = TextView.GONE

            val userSentMessages = messages.count { it.isSentByCurrentUser }
            if (userSentMessages == 1) {
                messageRecyclerView.post {
                    val layoutManager = messageRecyclerView.layoutManager as LinearLayoutManager
                    val recyclerViewHeight = messageRecyclerView.height
                    val firstUserMessagePosition = messages.indexOfFirst { it.isSentByCurrentUser }
                    val firstMessageView = layoutManager.findViewByPosition(firstUserMessagePosition)
                    val messageHeight = firstMessageView?.height ?: 0
                    val offset = (recyclerViewHeight - messageHeight) / 2
                    layoutManager.scrollToPositionWithOffset(firstUserMessagePosition, offset)
                }
            } else {
                messageRecyclerView.scrollToPosition(messages.size - 1)
            }
        }
    }

    // Handle back button press
    override fun onSupportNavigateUp(): Boolean {
        finish() // Go back to the previous activity (MainActivity or StartNewChatActivity)
        return true
    }
}