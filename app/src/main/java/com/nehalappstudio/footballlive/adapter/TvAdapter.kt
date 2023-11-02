package com.nehalappstudio.footballlive.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.nehalappstudio.footballlive.R
import com.nehalappstudio.footballlive.models.TeamCardModel
import com.nehalappstudio.footballlive.models.TvModel
import com.squareup.picasso.Picasso

class TvAdapter(private val context: Context, private val dataList: ArrayList<TvModel>, private val onItemClick: (Int) -> Unit):RecyclerView.Adapter<TvAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tv_card, parent, false);
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]

        holder.txtTvName.text = currentItem.channel_title
        holder.cvTV.setOnClickListener {
            onItemClick(position)
        }
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imgLogo: ShapeableImageView = itemView.findViewById(R.id.tvLogo)
        val txtTvName: TextView = itemView.findViewById(R.id.tvName)
        val cvTV: CardView = itemView.findViewById(R.id.cvTV)
    }
}