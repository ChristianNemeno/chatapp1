package com.android.chatapp1.classes

import android.app.Application

class ChatApplication : Application() {
    data class Message(val sender: String, val content: String, val timestamp: Long, val isSentByCurrentUser: Boolean)

    // Updated User data class with email, address, and age
    data class User(val username: String, val password: String, val email: String, val address: String, val age: Int)

    data class Conversation(val withUser: String, val messages: MutableList<Message>)

    companion object {
        private val users = mutableMapOf<String, User>()
        private val conversations = mutableMapOf<String, MutableList<Conversation>>()
        private lateinit var instance: ChatApplication
        private var currentLoggedInUsername: String? = null

        init {
            // Pre-populate with static users
            users["JohnDoe"] = User("JohnDoe", "password123", "john.doe@example.com", "123 Main St", 25)
            users["JaneSmith"] = User("JaneSmith", "password456", "jane.smith@example.com", "456 Elm St", 30)
            users["PeterPan"] = User("PeterPan", "password789", "peter.pan@example.com", "789 Oak St", 22)
            users.forEach { (username, _) -> conversations[username] = mutableListOf() }
        }

        fun getInstance(): ChatApplication = instance

        fun registerUser(username: String, password: String, email: String, address: String, age: Int): Boolean {
            if (users.containsKey(username)) return false
            users[username] = User(username, password, email, address, age)
            conversations[username] = mutableListOf()
            return true
        }

        fun loginUser(username: String, password: String): Boolean {
            return users[username]?.password == password
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

        fun updateProfile(newUsername: String, newPassword: String, newEmail: String, newAddress: String, newAge: Int): Boolean {
            val currentUser = currentLoggedInUsername ?: return false
            if (newUsername != currentUser && users.containsKey(newUsername)) return false
            val user = users.remove(currentUser)!!
            users[newUsername] = User(newUsername, newPassword, newEmail, newAddress, newAge)
            conversations[newUsername] = conversations.remove(currentUser)!!
            currentLoggedInUsername = newUsername
            return true
        }

        // Method to get user details
        fun getUserDetails(username: String): User? = users[username]
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}