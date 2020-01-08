package com.example.search

import android.app.AlertDialog
import android.app.ProgressDialog
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.*
//import kotlinx.coroutines.GlobalScope.coroutineContext
//import kotlinx.coroutines.tasks.await

import okhttp3.internal.wait
import java.nio.file.ProviderMismatchException

class AddedFriend : AppCompatActivity() {
    lateinit var recyclerView:RecyclerView
    lateinit var adapter:FriendAdapter
    val db= FirebaseFirestore.getInstance()
    lateinit var selfName: String
    val friends= ArrayList<Friend>()
    val emails= ArrayList<String>()
    lateinit var context: Context
    lateinit var progressDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_added_friend)
        setTitle("Friend List")
        selfName="kh@gmail.com"
        context=this
        recyclerView=findViewById(R.id.recyclerView)
        recyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        getFriends().execute()
//    val dialogView=layoutInflater.inflate(R.layout.progress_dialog,null)
//        displayLoading()
//        getAddedFriend()
    }
    internal inner class getFriends:AsyncTask<Void,Void,String>(){

        override fun doInBackground(vararg params: Void?): String {
            //   if(isNetworkAvailable())
//            CoroutineScope(coroutineContext).launch { }
            getAddedFriend()
            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()
            displayLoading()
//            progressDialog=
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()

            setView()
        }
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
                val intent = Intent(this, AddFriend::class.java)
                startActivity(intent)
                return true
            } R.id.search-> return true
            R.id.FriendRequest-> {
                val intent = Intent(this, FriendRequest::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun getAddedFriend():Boolean{

//       var fref =db.collection("New").document("kh@gmail.com").collection("AddedFriend").get().await()
//        fref.forEach{
//            documents->
//
//        }

        db.collection("New").document("kh@gmail.com").collection("AddedFriend")
            .get()
            .addOnSuccessListener {
                    documents ->
                for (document in documents) {
                    Toast.makeText(applicationContext, document.id, Toast.LENGTH_SHORT).show()
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



        return true
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

        adapter=FriendAdapter(this,friends)
        recyclerView.adapter=adapter
        return true
    }

    fun displayLoading(){
        progressDialog=ProgressDialog(context)
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
//
//        Toast.makeText(applicationContext,,Toast.LENGTH_SHORT).show()
//        val builder=AlertDialog.Builder(this)
//        val dialogView=layoutInflater.inflate(R.layout.progress_dialog,null)
//        builder.setView(dialogView)
//        builder.setCancelable(false)
//        val dialog=builder.create()
//        dialog.show()
//        Handler().postDelayed({dialog.dismiss()
//        setView()
//        },3000)

    }
}



