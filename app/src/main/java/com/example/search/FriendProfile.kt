package com.example.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class FriendProfile : AppCompatActivity() {
    val selfKey="FirstUser"
    val friendKey="SecondUser"
    val statusKey="Status"
    lateinit var friendName: TextView
    lateinit var friendbutton: Button
    val db=FirebaseFirestore.getInstance()
    val self="kh@gmail.com"
    val friend="kc@gmail.com"
    val status="Request"
    val statusCancel="Cancel"

    var note:HashMap<String,Object>
        = HashMap<String, Object>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_profile)


         friendName=findViewById(R.id.userid) as TextView
         friendbutton=findViewById(R.id.addFriend) as Button


        val relation=db.collection("User").document("Relation").collection("FirstUser").document(friend)
        val user=db.collection("User").document("User").collection(friend).document(friend)
               user.get()
            .addOnSuccessListener {
                    document->
                if (document!=null){
                    Log.d("exist","Document data:${document.data}")
                    friendName.text=document.getString("Name")
                }else{
                    Log.d("non exist","Non exist")
                }

            }
            .addOnFailureListener{
                    exception ->
                Log.d("Error DB","Faill",exception)
            }

        relation.get()
            .addOnSuccessListener {
                    document->
                if (document!=null){
                    Log.d("exist","Document data:${document.data}")
                    if(document.getString("Status").toString().equals("Request")){
                        friendbutton.setText("Friend Request Sent")
                    }else{
                        friendbutton.setText("Add Friend")
                    }

                }else{
                    Log.d("non exist","Non exist")
                    friendbutton.setText("Add Friend")
                }

            }
            .addOnFailureListener{
                    exception ->
                Log.d("Error DB","Faill",exception)
            }


    }



    fun sendRequest(view: View){

        if(friendbutton.text.equals("Add Friend")){
            note.put(selfKey,self as Object)
            note.put(friendKey,friend as Object)
            note.put(statusKey,status as Object)
            db.collection("User").document("Relation").collection(self).document(friend).set(note)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext,"Sent friend Request",Toast.LENGTH_SHORT).show()
                    friendbutton.setText("Friend Request Sent")
                }
        }else{
            note.put(statusKey,statusCancel as Object)
            db.collection("User").document("Relation").collection(self).document(friend).set(note)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext,"Cancelled friend Request",Toast.LENGTH_SHORT).show()
                    friendbutton.setText("Add Friend")
                }
        }

    }
}
