package com.myapplication.shopingmarket.adapter

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.myapplication.shopingmarket.ItemActivity
import com.myapplication.shopingmarket.R
import com.myapplication.shopingmarket.dataModel.CarItem
import com.squareup.picasso.Picasso

class ShoppingAdapter (private var context: AppCompatActivity,
                       private var dataSet: MutableList<CarItem>,
                       private val email : String) : RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>(){

   class ShoppingViewHolder(layoutView: View): RecyclerView.ViewHolder(layoutView) {
        val itemImage = layoutView.findViewById<ImageView>(R.id.car_item_image)
        var itemPrice = layoutView.findViewById<TextView>(R.id.car_text_price)
        val itemSpinner = layoutView.findViewById<Spinner>(R.id.item_spinner)
        val priceTextNormal = layoutView.findViewById<TextView>(R.id.car_text_price_n)
        val currency = layoutView.findViewById<TextView>(R.id.currency_text_n)
        val check = layoutView.findViewById<CheckBox>(R.id.item_checkBox)
        val indicator = layoutView.findViewById<ImageView>(R.id.indicator_selected)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        return ShoppingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.car_recycle,parent,false))
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {

        val pos: Int = dataSet[position].itemAmount.toInt()
        val price: Float = dataSet[position].itemPrice
        val itemDiscount: Float = dataSet[position].itemDiscount
        val max: Int = dataSet[position].itemMax
        val myStock: Int = dataSet[position].itemStock
        val count : MutableList<Int> = mutableListOf()
        var currentPosition: Int = position

        holder.itemImage.setOnClickListener {
            context.let{
                val intent = Intent (it, ItemActivity::class.java)
                intent.putExtra("Product",dataSet[position].itemId)
                it.startActivity(intent)
            }
        }

        holder.check.setOnClickListener{
            if(holder.check.isChecked){
                writeChange(true,dataSet[position].itemId)
                holder.indicator.setBackgroundResource(R.color.active_green)
                dataSet[position].itemCheck=true
                sumAll()
            }else{
                writeChange(false,dataSet[position].itemId)
                holder.indicator.setBackgroundResource(R.color.active_grey)
                dataSet[position].itemCheck=false
                sumAll()
            }

        }

        holder.itemSpinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(itemDiscount>0){
                    holder.itemPrice.text = ((price-itemDiscount)*(position+1).toFloat()).toString()
                    holder.priceTextNormal.text = (price * (position+1).toFloat()).toString()
                    holder.priceTextNormal.visibility = View.VISIBLE
                    holder.currency.visibility = View.VISIBLE
                    val layoutParams = holder.itemPrice.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.setMargins(8,0,0,82)
                }else {
                    holder.itemPrice.text = (price * (position + 1).toFloat()).toString()
                }
                val t = position+1
                dataSet[currentPosition].itemAmount=t.toFloat()
                savePosition(position+1,dataSet[currentPosition].itemId)
                sumAll()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        holder.priceTextNormal.visibility= View.INVISIBLE
        holder.priceTextNormal.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.currency.visibility= View.INVISIBLE
        holder.currency.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

        if (dataSet[position].itemCheck){
            holder.indicator.setBackgroundResource(R.color.active_green)
            holder.check.isChecked=true
        }else{
            holder.indicator.setBackgroundResource(R.color.active_grey)
            holder.check.isChecked=false
        }

        if(myStock>max){
            for (i in 1..max){
                    count.add(i)
                }
        }else{
            for (i in 1..myStock){
                count.add(i)
            }
        }
        holder.itemSpinner.adapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,count)
        holder.itemSpinner.setSelection(pos-1)
        if (dataSet[position].itemImage != ""){
            Picasso.get().load(dataSet[position].itemImage).into(holder.itemImage) //picasso image tool
        }
    }

    private fun sumAll(){
        var total = 0F
        val textview = context.findViewById<View>(R.id.valor_text) as TextView
        for (item in dataSet.indices){
            if(dataSet[item].itemCheck){
                var temp = 0F
                temp+=dataSet[item].itemPrice
                temp-=dataSet[item].itemDiscount
                total+=temp*dataSet[item].itemAmount
            }
        }
        textview.text=total.toString()
    }

    private fun savePosition(amount: Int,id: String) {
        val data = hashMapOf("itemAmount" to amount)
        FirebaseFirestore.getInstance().collection("user").document(email).collection("shopping_cart")
            .document(id).set(data, SetOptions.merge())
    }

    private fun writeChange(check :Boolean,id : String) {
        val data = hashMapOf("check" to check)
        FirebaseFirestore.getInstance().collection("user").document(email).collection("shopping_cart")
            .document(id).set(data, SetOptions.merge())
    }

    override fun getItemCount() = dataSet.size


}