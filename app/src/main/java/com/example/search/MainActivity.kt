package com.example.search

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView:RecyclerView
    lateinit var adapter:FriendAdapter
    val db= FirebaseFirestore.getInstance()
    lateinit var selfName: String
    val mLayoutManager =LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Friend List");
        selfName="kh@gmail.com"

        recyclerView=findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        val friends= ArrayList<Friend>()

        db.collection("User").document("Relation").collection(selfName)
            .get()
            .addOnSuccessListener {

                    documents ->


                for (document in documents) {
                    //Toast.makeText(applicationContext,"Cancelled friend Request",Toast.LENGTH_SHORT).show()
                    Log.d("ss", "${document.id} => ${document.data}")
                    if(document.get("Status").toString().equals("Accepted")){
                        friends.add(Friend(document.get("Name").toString(),"GG",document.get("Image").toString()))
//                        Toast.makeText(applicationContext,o.toString(),Toast.LENGTH_SHORT).show()

                    }
                }
                adapter=FriendAdapter(this,friends)

                recyclerView.adapter=adapter
              //  recyclerView.setLayoutManager(mLayoutManager);
            }
            .addOnFailureListener { exception ->
                Log.w("ss", "Error getting documents: ", exception)
              //  friends.add(Friend("GG","GG","hi"))
            }


     //   friends.add(Friend("GG","GG","hi"))
//adapter.setOnClickListener{
//
//}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.searchfriend,menu)

        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView=menu!!.findItem(R.id.search).actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo((componentName)))
        searchView!!.maxWidth= Int.MAX_VALUE
        searchView.setQueryHint("Search Friend")


        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                System.out.println(newText)


                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter.filter(query)

                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.AddFriendPage ->{
                val intent = Intent(this, FriendProfile::class.java)
                startActivity(intent)
                return true
            } R.id.search-> return true
        }
        return super.onOptionsItemSelected(item)
    }
}
