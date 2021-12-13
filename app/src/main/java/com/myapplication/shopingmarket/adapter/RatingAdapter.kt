package com.myapplication.shopingmarket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.shopingmarket.R
import com.myapplication.shopingmarket.dataModel.RatingModel


class RatingAdapter (private var context: AppCompatActivity,
                       private val dataSet: MutableList<RatingModel>) : RecyclerView.Adapter<RatingAdapter.RatingViewHolder>(){
    class RatingViewHolder(layoutView: View): RecyclerView.ViewHolder(layoutView) {

        val user = layoutView.findViewById<TextView>(R.id.user_rating)
        val rating = layoutView.findViewById<RatingBar>(R.id.user_rating_bar)
        val comment = layoutView.findViewById<TextView>(R.id.rating_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RatingViewHolder {
        return RatingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ratings_recycle,parent,false))
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {

        holder.user.text = dataSet[position].user
        holder.comment.text = dataSet[position].short_comments
        holder.rating.rating = dataSet[position].score
    }

    override fun getItemCount() = dataSet.size

}

