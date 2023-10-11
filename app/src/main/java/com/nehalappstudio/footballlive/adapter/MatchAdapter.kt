package com.nehalappstudio.footballlive.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.nehalappstudio.footballlive.LeagueDetails
import com.nehalappstudio.footballlive.MatchDetails
import com.nehalappstudio.footballlive.R
import com.nehalappstudio.footballlive.models.MatchCardModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.TimeZone

class MatchAdapter(val context: Context, private val dataList: ArrayList<MatchCardModel>):
    RecyclerView.Adapter<MatchAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.match_summary_card, parent, false);
        return ViewHolderClass(itemView);
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]

        holder.txtMatchTime.text = getTime(currentItem.time);
        holder.txtMatchDate.text = currentItem.date.substring(0, 10);
        Picasso.get().load(currentItem.team.home.logo).into(holder.imgFcHome);
        holder.txtTeamHome.text = currentItem.team.home.name
        if(currentItem.duration != "null"){
            if(currentItem.duration.toInt() < 90){
                holder.txtMatchStatus.setTextColor(Color.rgb(173, 20, 87))
            }else{
                holder.txtMatchStatus.setTextColor(Color.rgb(106, 107, 111))
            }
        }

        holder.txtMatchStatus.text = currentItem.duration+"'' - "+currentItem.status
        Picasso.get().load(currentItem.team.away.logo).into(holder.imgFcAway);
        holder.txtTeamAway.text = currentItem.team.away.name
        if(currentItem.team.away.goal == "null"){
            holder.txtGoalAway.text = "NS"
            holder.txtGoalHome.text = "NS"
        }else{
            holder.txtGoalAway.text = currentItem.team.away.goal.toString()
            holder.txtGoalHome.text = currentItem.team.home.goal.toString()
        }

        if(currentItem.venue.name == "null"){
            holder.txtVenue.text = "Not Found"
        }else{
            holder.txtVenue.text = currentItem.venue.name
        }

        holder.container.setOnClickListener {
            val intent = Intent(context, MatchDetails::class.java)
            intent.putExtra("id", currentItem.id)
            context.startActivity(intent)
        }
    }

    private fun getTime(time: String): CharSequence? {
        val timestamp = time.toLong()

        // Create a SimpleDateFormat instance for HH:mm:ss format
        val dateFormat = SimpleDateFormat("HH:mm a")

        // Set the time zone if needed (e.g., UTC)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        // Convert the timestamp to a Date object
        val date = Date(timestamp * 1000)

        // Format the Date object as a string in HH:mm:ss format
        val formattedTime = dateFormat.format(date)

        return  formattedTime;
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtMatchDate: TextView = itemView.findViewById(R.id.txtMatchDate)
        val txtMatchTime: TextView = itemView.findViewById(R.id.txtMatchTime)
        val imgFcHome: ShapeableImageView = itemView.findViewById(R.id.imgFcHome)
        val txtTeamHome: TextView = itemView.findViewById(R.id.txtTeamHome)
        val txtGoalHome: TextView = itemView.findViewById(R.id.txtGoalHome)
        val txtMatchStatus: TextView = itemView.findViewById(R.id.txtMatchStatus)
        val imgFcAway: ShapeableImageView = itemView.findViewById(R.id.imgFcAway)
        val txtTeamAway: TextView = itemView.findViewById(R.id.txtTeamAway)
        val txtGoalAway: TextView = itemView.findViewById(R.id.txtGoalAway)
        val txtVenue: TextView = itemView.findViewById(R.id.txtVenue)
        val container: LinearLayoutCompat = itemView.findViewById(R.id.matchSummaryCardContainer)
    }


}