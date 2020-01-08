package com.example.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.*

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
//import kotlinx.coroutines.channels.produce

class FriendRequestAdapter (val context: Context, val friendList:ArrayList<Friend>):RecyclerView.Adapter<FriendRequestAdapter.ViewHolder>(),Filterable{
    internal var filterResultList : ArrayList<Friend>
    var fcontext:Context
//    private val inflater: LayoutInflater
    init {
        this.fcontext=context
        this.filterResultList=friendList
//        inflater = LayoutInflater.from(context)
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch:String=constraint.toString()
                if(charSearch.isEmpty()){
                    filterResultList=friendList
                }else{
                    val resultList=ArrayList<Friend>()
                    for(row in friendList){
                        if(row.name!!.toLowerCase().contains(charSearch.toLowerCase())){
                            resultList.add(row)
                        }
                    }
                    filterResultList=resultList
                }
                val filterResults=FilterResults()
                filterResults.values=filterResultList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterResultList=results!!.values as ArrayList<Friend>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.cardviewcontent,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return filterResultList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend:Friend=filterResultList[position]
        holder.textViewName.text=friend.name
        holder.textViewAddress.text=friend.image
        holder.textGmail.text=friend.email
        Glide.with(fcontext)
            .asBitmap()
            .load(friend.image)
            .into(holder.image)

        holder.friendProfile.setOnClickListener {
            //    Toast.makeText(fcontext,friend.name,Toast.LENGTH_SHORT).show()
            val intent= Intent(fcontext,FriendProfile::class.java)
            intent.putExtra("Name",friend.name)
            intent.putExtra("Email",friend.email)
            intent.putExtra("Image",friend.image)
            intent.putExtra("Address",friend.address)
            fcontext.startActivity(intent)

        }
    }
    inner class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){
        var friendProfile: LinearLayout
        internal val textViewName:TextView
        internal val textViewAddress:TextView
        internal val textGmail:TextView
        internal val image: CircleImageView
        internal val acceptButton:Button
        protected var declineButton:Button
        init{

            textViewName=itemView.findViewById(R.id.textViewName) as TextView
            textViewAddress=itemView.findViewById(R.id.textViewAddress) as TextView
            textGmail=itemView.findViewById(R.id.textViewAddress) as TextView
            friendProfile=itemView.findViewById(R.id.friendProfile) as LinearLayout
            image=itemView.findViewById(R.id.profilePic) as CircleImageView
            acceptButton=itemView.findViewById(R.id.buttonAccept) as Button
            declineButton=itemView.findViewById(R.id.buttonDecline) as Button

            acceptButton.visibility=View.VISIBLE
            declineButton.visibility=View.VISIBLE
//            acceptButton.setTag(1, itemView)
//            declineButton.setTag(3, itemView)

            acceptButton.setOnClickListener{
                AcceptRequest(adapterPosition)

            }

            declineButton.setOnClickListener{
                declineRequest(adapterPosition)

            }
        }

             }
    var note:HashMap<String,Object>
            = HashMap<String, Object>()
    val db= FirebaseFirestore.getInstance()
    val userTable="New"
    val currentEmail="kh@gmail.com"
    val currentName="Kah Heng"
    val currentImage="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2iIe9gEuJbItE3_azb1sOA29i8Py_A0TaZDTTUKyJoAEVbgYr&s"
    val request="FriendRequest"
    val sent="SentFriendRequest"
    val added="AddedFriend"
    val Email="Email"
    val Image="Image"
    val Name="Name"
    fun AcceptRequest(position: Int){

        val fEmail=friendList.get(position).email

        note.put("Name",friendList.get(position).name as Object)
        note.put("Image",friendList.get(position).image as Object)
        note.put("Email",friendList.get(position).email as Object)

        db.collection(userTable).document(currentEmail).collection(added).document(fEmail).set(note)
            .addOnSuccessListener {
                Toast.makeText(context,"Friend Accepted",Toast.LENGTH_SHORT).show()

            }
        note.put(Name,currentName as Object)
        note.put(Image,currentImage as Object)
        note.put(Email,currentEmail as Object)

        db.collection(userTable).document(fEmail).collection(added).document(currentEmail).set(note)

        //note.put(statusKey,statusCancel as Object)
        db.collection(userTable).document(currentEmail).collection(request).document(fEmail).delete()
        db.collection(userTable).document(fEmail).collection(sent).document(currentEmail).delete()
        friendList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()

    }
    fun declineRequest(position: Int){

        val fEmail=friendList.get(position).email
        db.collection(userTable).document(currentEmail).collection(request).document(fEmail).delete()
        db.collection(userTable).document(fEmail).collection(sent).document(currentEmail).delete()
        Toast.makeText(context,"Decline Accepted",Toast.LENGTH_SHORT).show()
        friendList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()


    }

    }

