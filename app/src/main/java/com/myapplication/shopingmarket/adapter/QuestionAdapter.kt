package com.myapplication.shopingmarket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.shopingmarket.R
import com.myapplication.shopingmarket.dataModel.QuestionsModel
import java.text.SimpleDateFormat


class QuestionAdapter (private var context: AppCompatActivity,
                       private val dataSet: MutableList<QuestionsModel>) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>(){
    class QuestionViewHolder(layoutView: View): RecyclerView.ViewHolder(layoutView){
        val userQuestionText = layoutView.findViewById<TextView>(R.id.userQ)
        val questionDate = layoutView.findViewById<TextView>(R.id.question_date)
        val userQuestion = layoutView.findViewById<TextView>(R.id.question)
        val replyTitle = layoutView.findViewById<TextView>(R.id.reply_title)
        val replyDate = layoutView.findViewById<TextView>(R.id.reply_date)
        val reply = layoutView.findViewById<TextView>(R.id.reply)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.questions_item_recycle,parent,false))
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        holder.userQuestion.text = dataSet[position].userQuestion
        holder.reply.text = dataSet[position].reply
        holder.replyDate.text = sdf.format(dataSet[position].timeReply).toString()
        holder.questionDate.text = sdf.format(dataSet[position].timeQuestion).toString()
    }

    override fun getItemCount() = dataSet.size

}


