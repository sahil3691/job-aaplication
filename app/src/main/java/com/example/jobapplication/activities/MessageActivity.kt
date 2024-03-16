package com.example.jobapplication.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buzzhub.notification.NotificationData
import com.example.buzzhub.notification.PushNotification
import com.example.buzzhub.notification.api.ApiUtilities
import com.example.jobapplication.R
import com.example.jobapplication.adapters.ChatAdapter
import com.example.jobapplication.models.Chat
import com.example.jobapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageActivity : AppCompatActivity() {

    private lateinit var username: TextView
    private lateinit var imageView: ImageView
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var mChat: ArrayList<Chat>
    private lateinit var userId: String

    private lateinit var seenListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_message)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        imageView = findViewById(R.id.image_profile)
        username = findViewById(R.id.username)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerView_messages)
        messageEditText = findViewById(R.id.message)
        sendButton = findViewById(R.id.send)
        recyclerView.setHasFixedSize(true)


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.title = null


        var intent = intent
        userId = intent.getStringExtra("userId")!!
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isDestroyed) {
                    val user = snapshot.getValue(User::class.java)
                    username.text = user?.username
                    if (user?.image == "default") {
                        imageView.setImageResource(R.drawable.user)
                    } else {
                        Glide.with(applicationContext)
                            .load(user?.image)
                            .into(imageView)
                    }
                    readMessage(firebaseUser.uid, userId, user?.image)
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })

        sendButton.setOnClickListener {
            val message = messageEditText.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(firebaseUser.uid, userId, message)
            }
            messageEditText.setText("")
        }

        seenMessage(userId)

        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager


    }

    private fun seenMessage(userId: String) {
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        seenListener = reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat?.receiver == firebaseUser.uid && chat.sender == userId) {
                        val hashMap: HashMap<String, Any> = HashMap()
                        hashMap["isseen"] = true
                        dataSnapshot.ref.updateChildren(hashMap)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun sendMessage(sender: String, receiver: String, message: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["sender"] = sender
        hashMap["receiver"] = receiver
        hashMap["message"] = message
        hashMap["isseen"] = false
        reference.child("Chats").push().setValue(hashMap)


        sendNotification(message)


        val chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
            .child(firebaseUser.uid).child(userId)

        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    chatRef.child("id").setValue(userId)
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }


    private fun readMessage(myId: String, userId: String, imageURL: String?) {
        mChat = ArrayList()
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mChat.clear()
                for (dataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat?.sender == myId && chat.receiver == userId || chat?.receiver == myId && chat.sender == userId) {
                        mChat.add(chat)
                    }
                }
                chatAdapter = ChatAdapter(this@MessageActivity, mChat, imageURL!!)
                recyclerView.adapter = chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }


    private fun checkStatus(status: String) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["status"] = status
        reference.updateChildren(hashMap)
    }


    override fun onResume() {
        super.onResume()
        checkStatus("online")
    }


    override fun onPause() {
        super.onPause()
        reference.removeEventListener(seenListener)
        checkStatus("offline")
    }

    private fun sendNotification(message: String) {

        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val receiverUser = snapshot.getValue(User::class.java)
                if (receiverUser!=null) {
                    val notificationData = PushNotification(
                        NotificationData("New Message", message),
                        receiverUser.fcmToken
                    )

                    ApiUtilities.getInstance().sendNotification(
                        notificationData
                    ).enqueue(object : Callback<PushNotification> {
                        override fun onResponse(
                            call: Call<PushNotification>,
                            response: Response<PushNotification>
                        ) {
                            // Toast.makeText(this@MessageActivity,"Notification sent!", Toast.LENGTH_LONG).show()
                        }

                        override fun onFailure(call: Call<PushNotification>, t: Throwable) {
                            Toast.makeText(this@MessageActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                        }

                    })

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

}