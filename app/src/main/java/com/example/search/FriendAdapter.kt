package com.example.search

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class FriendAdapter (val context: Context,val friendList:ArrayList<Friend>):RecyclerView.Adapter<FriendAdapter.ViewHolder>(),Filterable{
   internal var filterResultList : ArrayList<Friend>
    var fcontext:Context

    init {
        this.fcontext=context
        this.filterResultList=friendList
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
//            intent.putExtra("Activity")
            intent.putExtra("Name",friend.name)
            intent.putExtra("Email",friend.email)
            intent.putExtra("Image",friend.image)
            intent.putExtra("Address",friend.address)
            fcontext.startActivity(intent)

    }
}
    class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){

        var friendProfile: LinearLayout
        internal val textViewName:TextView
        internal val textViewAddress:TextView
        internal val textGmail:TextView
        internal val image: CircleImageView

        init{

            textViewName=itemView.findViewById(R.id.textViewName) as TextView
            textViewAddress=itemView.findViewById(R.id.textViewAddress) as TextView
            textGmail=itemView.findViewById(R.id.textViewAddress) as TextView
            friendProfile=itemView.findViewById(R.id.friendProfile) as LinearLayout
            image=itemView.findViewById(R.id.profilePic) as CircleImageView



            }

        }
    }

