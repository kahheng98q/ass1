package com.example.search

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class FriendRequest : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter:FriendRequestAdapter
    val db= FirebaseFirestore.getInstance()
    lateinit var selfName: String
    val friends= ArrayList<Friend>()
    val emails= ArrayList<String>()
    //table and self email
    val userTable="New"
    val currentEmail="kh@gmail.com"
    val currentName="Kah Heng"
    val currentImage="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2iIe9gEuJbItE3_azb1sOA29i8Py_A0TaZDTTUKyJoAEVbgYr&s"
    //category
    val request="FriendRequest"
    val sent="SentFriendRequest"
    val added="AddedFriend"
    val Email="Email"
    val Image="Image"
    val Name="Name"
    var note:HashMap<String,Object>
            = HashMap<String, Object>()
    var noteself:HashMap<String,Object>
            = HashMap<String, Object>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_request)
        setTitle("Friend Request")

        recyclerView=findViewById(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        displayFriendRequest()
        displayLoading()

    }


    fun displayFriendRequest(){
        db.collection(userTable).document(currentEmail).collection(request)
            .get()
            .addOnSuccessListener {
                    documents ->
                for (document in documents) {
//                    Toast.makeText(applicationContext,document.id, Toast.LENGTH_SHORT).show()
                    Log.d("ss", "${document.id} => ${document.data}")
                    //   if(document.get("Status").toString().equals("Accepted")){
                    //  friends.add(Friend(document.get("Name").toString(),"GG",document.id,document.get("Image").toString()))
                    emails.add(document.id)
                    retrieveFriends(document.id)
                    //   }
                }

            }
            .addOnFailureListener { exception ->
                Log.w("ss", "Error getting documents: ", exception)
                //  friends.add(Friend("GG","GG","hi"))
            }

    }

    fun retrieveFriends(email:String){
        db.collection("New").document(email)
            .get()
            .addOnSuccessListener {
                    document->
                if (document!=null&&document.exists()){
                    //  Toast.makeText(applicationContext,document.id,Toast.LENGTH_SHORT).show()
                    Log.d("exist","Document data:${document.data}")
                    friends.add(Friend(document.get("Name").toString(),document.get("Address").toString()
                        ,document.get("Email").toString(),document.get("Image").toString()))
                }
            }
            .addOnFailureListener{
                    exception ->
                Log.d("Error DB","Fail",exception)
            }
    }
    fun setView():Boolean{

        adapter=FriendRequestAdapter(this,friends)
        recyclerView.adapter=adapter
        return true
    }
    fun displayLoading(){
//        Toast.makeText(applicationContext,,Toast.LENGTH_SHORT).show()
        val builder= AlertDialog.Builder(this)
        val dialogView=layoutInflater.inflate(R.layout.progress_dialog,null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog=builder.create()
        dialog.show()
        Handler().postDelayed({dialog.dismiss()
            setView()

        },3000)

    }


    }


