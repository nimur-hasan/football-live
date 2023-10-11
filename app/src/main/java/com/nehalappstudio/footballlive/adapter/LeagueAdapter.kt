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

class LeagueAdapter(private val context:Context, private val dataList:ArrayList<LeagueCardModel>):RecyclerView.Adapter<LeagueAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.league_grid_card, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        val currentItem = dataList[position]
        Picasso.get().load(currentItem.img).into(holder.leagueLogo)

        holder.leagueName.text = currentItem.name
        holder.leagueStart.text = currentItem.start
        holder.leagueEnd.text = currentItem.end

        holder.container.setOnClickListener {
            val intent = Intent(context, LeagueDetails::class.java)
            intent.putExtra("leagueID", currentItem.id)
            intent.putExtra("img", currentItem.img)
            intent.putExtra("name", currentItem.name)
            intent.putExtra("venue", currentItem.venue)
            intent.putExtra("start", currentItem.start)
            intent.putExtra("end", currentItem.end)
            context.startActivity(intent)
        }

    }

    class ViewHolderClass(itemView: View):RecyclerView.ViewHolder(itemView) {
        val leagueLogo: ShapeableImageView = itemView.findViewById(R.id.imgLeague)
        val leagueName: TextView = itemView.findViewById(R.id.txtLeagueName)
        val liveMatches: TextView = itemView.findViewById(R.id.txtLiveMatches)
        val leagueStart: TextView = itemView.findViewById(R.id.txtLeagueStart)
        val leagueEnd: TextView = itemView.findViewById(R.id.txtLeagueEnd)
        val container: LinearLayoutCompat = itemView.findViewById(R.id.container)
    }
}