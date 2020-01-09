package com.example.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    val db = FirebaseFirestore.getInstance()
    var isfriend = 0
    //table and self email
    val userTable = "New"
    val currentEmail = "kh@gmail.com"
    val currentName = "Kah Heng"
    val currentImage =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2iIe9gEuJbItE3_azb1sOA29i8Py_A0TaZDTTUKyJoAEVbgYr&s"
    //category
    val request = "FriendRequest"
    val sent = "SentFriendRequest"
    val added = "AddedFriend"
    val Email = "Email"
    val Image = "Image"
    val Name = "Name"
//    val self="kh@gmail.com"
//    val friend="kc@gmail.com"
//    val status="Request"
//    val statusCancel="Cancel"

    var note: HashMap<String, Object> = HashMap<String, Object>()
    var noteself: HashMap<String, Object> = HashMap<String, Object>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_profile)

        friendName = findViewById(R.id.userid)
        friendbutton = findViewById(R.id.addFriend)
        friendImage = findViewById(R.id.friendProfilePic)
        friendEmail = findViewById(R.id.email)
        displayAddedProfile()


        checkAddedFriend()
        displayLoading()


    }

    // onClick button
    fun sendRequest(view: View) {
        val fEmail = intent.getStringExtra("Email")
        val fName = intent.getStringExtra("Name")
        val fImage = intent.getStringExtra("Image")
        var fAddress = intent.getStringExtra("Address")

        if (friendbutton.text.equals("Add Friend")) {
            note.put(Name, fName as Object)
            note.put(Image, fImage as Object)
            note.put(Email, fEmail as Object)

            db.collection(userTable).document(currentEmail).collection(sent).document(fEmail)
                .set(note)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Friend Request Sent", Toast.LENGTH_SHORT)
                        .show()
                    friendbutton.setText(R.string.SentFriendRequest)
                }
            noteself.put(Name, currentName as Object)
            noteself.put(Image, currentImage as Object)
            noteself.put(Email, currentEmail as Object)

            db.collection(userTable).document(fEmail).collection(request).document(currentEmail)
                .set(noteself)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Friend Request Sent", Toast.LENGTH_SHORT)
                        .show()
                    friendbutton.setText(R.string.SentFriendRequest)
                }
        } else if ((friendbutton.text.equals("Sent Friend Request"))) {

            db.collection(userTable).document(currentEmail).collection(sent).document(fEmail)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(
                        applicationContext,
                        "Cancelled friend Request",
                        Toast.LENGTH_SHORT
                    ).show()
                    friendbutton.setText(R.string.AddFriend)
                }
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Do you want to detele " + fName + " ?")
            builder.setPositiveButton("Yes") { dialog, which ->
                db.collection(userTable).document(currentEmail).collection(added).document(fEmail)
                    .delete()
                db.collection(userTable).document(fEmail).collection(added).document(currentEmail)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Friend Deleted", Toast.LENGTH_SHORT)
                            .show()
                        friendbutton.setText(R.string.AddFriend)
                    }
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                //   Toast.makeText(applicationContext,"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }

    fun checkAddedFriend() {
        if (intent.hasExtra("Email")) {
            val fEmail = intent.getStringExtra("Email")
            db.collection(userTable).document(currentEmail).collection(added).document(fEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Toast.makeText(applicationContext, document.id, Toast.LENGTH_SHORT).show()
                        //   Log.d("exist", "Document data:${document.data}")
                        System.out.println("1")
                        friendbutton.setText("Delete Friend")

                    } else {
                        Log.d("non exist", "Non exist")
                    }
                }


        }

//        return isfriend
    }

    //button
    fun checkRequestFriend() {
        val fEmail = intent.getStringExtra("Email")
        db.collection(userTable).document(currentEmail).collection(sent).document(fEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("exist", "Document data:${document.data}")
                    friendbutton.setText(R.string.SentFriendRequest)

                    //    }
                } else {
                    Log.d("non exist", "Non exist")
                    friendbutton.setText(R.string.AddFriend)
                }

            }
            .addOnFailureListener { exception ->
                Log.d("Error DB", "Faill", exception)
            }
    }

    fun displayAddedProfile() {
        if (intent.hasExtra("Email")) {
            val fEmail = intent.getStringExtra("Email")
            val goToUserData = db.collection(userTable).document(fEmail)
            goToUserData.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("exist", "Document data:${document.data}")
//
                        setProfile(
                            document.getString("Image").toString(),
                            document.getString("Name").toString(),
                            document.getString("Address").toString(),
                            document.getString("Email").toString()
                        )
                    } else {
                        Log.d("non exist", "Non exist")
                    }

                }
                .addOnFailureListener { exception ->
                    Log.d("Error DB", "Fail", exception)
                }


        } else {

        }
    }

    fun setProfile(Image: String, Name: String, Address: String, Email: String) {
        friendName.setText(Name)
        friendEmail.setText(Email)
        Glide.with(this)
            .asBitmap()
            .load(Image)
            .into(friendImage)
    }

    fun displayLoading() {
//        Toast.makeText(applicationContext,,Toast.LENGTH_SHORT).show()
        val builder = android.app.AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
        Handler().postDelayed({
            dialog.dismiss()
            Toast.makeText(applicationContext, friendbutton.text.toString(), Toast.LENGTH_SHORT)
                .show()
            if (!friendbutton.text.toString().equals("Delete Friend")) {
                checkRequestFriend()
            }
        }, 3000)

    }
}
