package com.android.chatapp1.classes

import android.app.Application

class ChatApplication : Application() {
    data class Message(val sender: String, val content: String, val timestamp: Long, val isSentByCurrentUser: Boolean)
    companion object {
        private val users = mutableMapOf<String, String>()
        private val conversations = mutableMapOf<String, MutableList<Conversation>>()
        private lateinit var instance: ChatApplication
        private var currentLoggedInUsername: String? = null

        init {
            // Pre-populate with static users
            users["JohnDoe"] = "password123"
            users["JaneSmith"] = "password456"
            users["PeterPan"] = "password789"
            users.forEach { (username, _) -> conversations[username] = mutableListOf() }
        }

        data class Conversation(val withUser: String, val messages: MutableList<Message>)


        fun getInstance(): ChatApplication = instance

        fun registerUser(username: String, password: String): Boolean {
            if (users.containsKey(username)) return false
            users[username] = password
            conversations[username] = mutableListOf()
            return true
        }

        fun loginUser(username: String, password: String): Boolean {
            return users[username] == password
        }

        fun setCurrentLoggedInUser(username: String?) {
            currentLoggedInUsername = username
        }

        fun getCurrentLoggedInUser(): String? = currentLoggedInUsername

        fun logoutUser() {
            currentLoggedInUsername = null
        }

        fun getRegisteredUsers(): List<String> {
            return users.keys.filter { it != currentLoggedInUsername }
        }

        fun startNewChat(withUser: String): Conversation {
            val currentUser = currentLoggedInUsername ?: throw IllegalStateException("No user logged in")
            val userConversations = conversations.getOrPut(currentUser) { mutableListOf() }
            var conversation = userConversations.find { it.withUser == withUser }
            if (conversation == null) {
                conversation = Conversation(withUser, mutableListOf())
                userConversations.add(conversation)
            }
            return conversation
        }

        fun sendMessage(toUser: String, content: String) {
            val currentUser = currentLoggedInUsername ?: throw IllegalStateException("No user logged in")
            val userConversations = conversations.getOrPut(currentUser) { mutableListOf() }
            var conversation = userConversations.find { it.withUser == toUser }
            if (conversation == null) {
                conversation = Conversation(toUser, mutableListOf())
                userConversations.add(conversation)
            }
            conversation.messages.add(Message(currentUser, content, System.currentTimeMillis(), true))
            conversation.messages.add(Message("System", "Message received", System.currentTimeMillis(), false))
        }

        fun getConversations(): List<Conversation> {
            val currentUser = currentLoggedInUsername ?: return emptyList()
            return conversations[currentUser] ?: emptyList()
        }

        fun updateProfile(newUsername: String, newPassword: String): Boolean {
            val currentUser = currentLoggedInUsername ?: return false
            if (newUsername != currentUser && users.containsKey(newUsername)) return false
            val password = users.remove(currentUser)!!
            users[newUsername] = newPassword
            conversations[newUsername] = conversations.remove(currentUser)!!
            currentLoggedInUsername = newUsername
            return true
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}