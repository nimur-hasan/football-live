package com.nehalappstudio.footballlive.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.nehalappstudio.footballlive.LeagueDetails
import com.nehalappstudio.footballlive.R
import com.nehalappstudio.footballlive.models.LeagueCardModel
import com.squareup.picasso.Picasso

class LeagueSectionAdapter(private val context:Context, private val dataList:ArrayList<LeagueCardModel>, private val onItemClick: (Int) -> Unit):RecyclerView.Adapter<LeagueSectionAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.league_section_item, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        val currentItem = dataList[position]
        Picasso.get().load(currentItem.img).into(holder.leagueLogo)


        holder.container.setOnClickListener {
            if(currentItem.id != "null" || currentItem.id != ""){
                onItemClick(currentItem.id.toInt())
            }
        }

    }


    class ViewHolderClass(itemView: View):RecyclerView.ViewHolder(itemView) {
        val leagueLogo: ShapeableImageView = itemView.findViewById(R.id.imgLeagueSection)
        val container: LinearLayoutCompat = itemView.findViewById(R.id.leagueSectionContainer)
    }
}