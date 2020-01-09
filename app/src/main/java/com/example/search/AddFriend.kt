package com.example.search

import android.app.AlertDialog
import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AddFriend : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FriendAdapter
    val db = FirebaseFirestore.getInstance()
    lateinit var selfName: String
    val friends = ArrayList<Friend>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        setTitle("Add Friend")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //val inflater=menuInflater
        menuInflater.inflate(R.menu.addfriend, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.AddFriendSearch).actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo((componentName)))
        searchView!!.maxWidth = Int.MAX_VALUE
        searchView.setQueryHint("Search Friend")


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                //    adapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                displayLoading()
                val lowercase = query.toLowerCase()
                retrieveFriends(lowercase)
                return false
            }

        })
        return true
    }

    fun retrieveFriends(email: String) {
        friends.clear()
        db.collection("New").document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("exist", "Document data:${document.data}")
                    friends.add(
                        Friend(
                            document.get("Name").toString(), document.get("Address").toString()
                            , document.get("Email").toString(), document.get("Image").toString()
                        )
                    )

                } else {
                    friends.clear()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Error DB", "Fail", exception)
            }
    }

    fun setView(): Boolean {
        adapter = FriendAdapter(this, friends)
        recyclerView.adapter = adapter
        if (friends.isEmpty()) {
            Toast.makeText(applicationContext, "Email didn't exist.", Toast.LENGTH_SHORT).show()
        }

        return true
    }

    fun displayLoading() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
//        val message=dialogView.findViewById<TextView>(R.id.message)
//        message.text
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
        Handler().postDelayed({
            dialog.dismiss()
            setView()
        }, 3000)
//        progressDialog= ProgressDialog(context)
//        progressDialog.setMessage("Loading")
//        progressDialog.setCancelable(false)
//        progressDialog.show()
    }

}
