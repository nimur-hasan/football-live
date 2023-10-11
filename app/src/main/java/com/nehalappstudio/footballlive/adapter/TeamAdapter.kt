package com.nehalappstudio.footballlive.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.nehalappstudio.footballlive.R
import com.nehalappstudio.footballlive.models.TeamCardModel
import com.squareup.picasso.Picasso

class TeamAdapter(private val context: Context, private val dataList: ArrayList<TeamCardModel>):RecyclerView.Adapter<TeamAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.team_card, parent, false);
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.txtTeamName.text = currentItem.name
        holder.txtTeamLocation.text = currentItem.location
        holder.txtTeamFounded.text = currentItem.founded
        Picasso.get().load(currentItem.img).into(holder.imgTeam)
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val txtTeamName: TextView = itemView.findViewById(R.id.txtTeamName)
        val txtTeamLocation: TextView = itemView.findViewById(R.id.txtTeamLocation)
        val txtTeamFounded: TextView = itemView.findViewById(R.id.txtFounded)
        val imgTeam: ShapeableImageView = itemView.findViewById(R.id.imgTeam)
    }
}