package com.myapplication.shopingmarket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.myapplication.shopingmarket.R
import com.myapplication.shopingmarket.dataModel.IntroSlideR

class RecommendAdapter (private val viewPager : ViewPager2, private val sliderList : ArrayList<IntroSlideR>) :
    RecyclerView.Adapter<RecommendAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(var view : View) : RecyclerView.ViewHolder(view){

        val barRating = view.findViewById<RatingBar>(R.id.rancking_bar_home)
        val iconImage = view.findViewById<ImageView>(R.id.recommended_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recommend_item,parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val resourceData = sliderList[position]
        holder.iconImage.setImageResource(resourceData.imgIcon)
        if (position == sliderList.size -2){
            viewPager.post(run)
        }
    }
    private val run = Runnable {
        sliderList.addAll(sliderList)

    }

    override fun getItemCount(): Int = sliderList.size

}