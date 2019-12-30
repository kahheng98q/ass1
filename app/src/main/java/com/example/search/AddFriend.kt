package com.example.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView

class AddFriend : AppCompatActivity() {
//    lateinit var adapter:FriendAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        setTitle("Add Friend")
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //val inflater=menuInflater
        menuInflater.inflate(R.menu.addfriend, menu)


        return true
    }

}
