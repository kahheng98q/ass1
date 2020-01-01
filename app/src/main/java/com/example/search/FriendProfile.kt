package com.example.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.HashMap

class FriendProfile : AppCompatActivity() {

    lateinit var friendName: TextView
    lateinit var friendEmail: TextView
    lateinit var friendbutton: Button
    lateinit var friendImage: CircleImageView
    val db=FirebaseFirestore.getInstance()
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
//    val self="kh@gmail.com"
//    val friend="kc@gmail.com"
//    val status="Request"
//    val statusCancel="Cancel"

    var note:HashMap<String,Object>
        = HashMap<String, Object>()
    var noteself:HashMap<String,Object>
            = HashMap<String, Object>()

    var isfriend=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_profile)

         friendName=findViewById(R.id.userid)
         friendbutton=findViewById(R.id.addFriend)
         friendImage=findViewById(R.id.friendProfilePic)
        friendEmail=findViewById(R.id.email)
        displayAddedProfile()
//diplay user profile
//        val goToUserData=db.collection(userTable).document(goTo)
//             goToUserData.get()
//            .addOnSuccessListener {
//                    document->
//                if (document!=null){
//                    Log.d("exist","Document data:${document.data}")
//                    friendName.text=document.getString("Name")
//                    Glide.with(this)
//                        .asBitmap()
//                        .load(document.getString("Image"))
//                        .into(friendImage)
//                }else{
//                    Log.d("non exist","Non exist")
//                }
//
//            }
//            .addOnFailureListener{
//                    exception ->
//                Log.d("Error DB","Fail",exception)
//            }
        //check the status

        isfriend=checkAddedFriend()
      //  displayAddedProfile()
        if(!isfriend){
            checkRequestFriend()
        }

    }


// onClick button
    fun sendRequest(view: View){
    val fEmail=intent.getStringExtra("Email")
    val fName=intent.getStringExtra("Name")
    val fImage=intent.getStringExtra("Image")
    var fAddress=intent.getStringExtra("Address")

        if(friendbutton.text.equals("Add Friend")){
            note.put(Name,fName as Object)
            note.put(Image,fImage as Object)
            note.put(Email,fEmail as Object)

            db.collection(userTable).document(currentEmail).collection(sent).document(fEmail).set(note)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext,"Friend Request Sent",Toast.LENGTH_SHORT).show()
                    friendbutton.setText(R.string.SentFriendRequest)
                }
            note.put(Name,currentName as Object)
            note.put(Image,currentImage as Object)
            note.put(Email,currentEmail as Object)

            db.collection(userTable).document(fEmail).collection(request).document(currentEmail).set(note)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext,"Friend Request Sent",Toast.LENGTH_SHORT).show()
                    friendbutton.setText(R.string.SentFriendRequest)
                }
        }else{
            //note.put(statusKey,statusCancel as Object)
            db.collection(userTable).document(currentEmail).collection(sent).document(fEmail).delete()
                .addOnSuccessListener {
                    Toast.makeText(applicationContext,"Cancelled friend Request",Toast.LENGTH_SHORT).show()
                    friendbutton.setText(R.string.AddFriend)
                }
        }

    }

    fun checkAddedFriend():Boolean{
        var isfriend = false
        if(intent.hasExtra("Email")) {
            val fEmail=intent.getStringExtra("Email")
            db.collection(userTable).document(currentEmail).collection(added).document(fEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        Toast.makeText(applicationContext, document.id, Toast.LENGTH_SHORT).show()
                        Log.d("exist", "Document data:${document.data}")
                        //  if(document.id.equals(goTo)){
//                        document.id
                        friendbutton.visibility = View.GONE
                        isfriend = true
                        // }
                    } else {
                        Log.d("non exist", "Non exist")
                        //  friendbutton.visibility = View.GONE
                    }

                }
                .addOnFailureListener { exception ->
                    Log.d("Error DB", "Faill", exception)
                }

        }
        return isfriend
    }
    fun checkRequestFriend(){

        val fEmail=intent.getStringExtra("Email")
        db.collection(userTable).document(currentEmail).collection(sent).document(fEmail)
            .get()
            .addOnSuccessListener {
                    document->
                if (document!=null&&document.exists()){
                   // Toast.makeText(applicationContext,document.id,Toast.LENGTH_SHORT).show()
                    Log.d("exist","Document data:${document.data}")
                  //  if(document.id.equals(goTo)){
//                        document.id
                        friendbutton.setText(R.string.SentFriendRequest)

                //    }
                }else{
                    Log.d("non exist","Non exist")
                    friendbutton.setText(R.string.AddFriend)
                }

            }
            .addOnFailureListener{
                    exception ->
                Log.d("Error DB","Faill",exception)
            }
    }
    fun displayAddedProfile(){
        if(intent.hasExtra("Email")){

//            var fName=intent.getStringExtra("Name")
//            var fImage=intent.getStringExtra("Image")
//            var fAddress=intent.getStringExtra("Address")
            val fEmail=intent.getStringExtra("Email")
            val goToUserData=db.collection(userTable).document(fEmail)
            goToUserData.get()
                .addOnSuccessListener {
                        document->
                    if (document!=null){
                        Log.d("exist","Document data:${document.data}")
//                        friendName.text=document.getString("Name")
//                        Glide.with(this)
//                            .asBitmap()
//                            .load(document.getString("Image"))
//                            .into(friendImage)
                        setProfile(document.getString("Image").toString(),document.getString("Name").toString(),
                            document.getString("Address").toString(),document.getString("Email").toString()
                        )
                    }else{
                        Log.d("non exist","Non exist")
                    }

                }
                .addOnFailureListener{
                        exception ->
                    Log.d("Error DB","Fail",exception)
                }


//            setImage(fImage,fName)
        }else{
         //   Toast.makeText(applicationContext,"GGGGGGGGGGGGGGGG",Toast.LENGTH_SHORT).show()
        }
    }
    fun setProfile(Image:String,Name:String,Address:String,Email:String){
        friendName.setText(Name)
        friendEmail.setText(Email)
        Glide.with(this)
            .asBitmap()
            .load(Image)
            .into(friendImage)
    }
}
