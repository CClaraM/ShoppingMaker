package com.myapplication.shopingmarket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.myapplication.shopingmarket.adapter.ShoppingAdapter
import com.myapplication.shopingmarket.adapter.SwipeDeleteAdapter
import com.myapplication.shopingmarket.dataModel.CarItem
import com.myapplication.shopingmarket.dataModel.ProviderModel
import kotlinx.android.synthetic.main.activity_shopping.*

class ShoppingActivity : AppCompatActivity() {
    private lateinit var shoppingAdapter: RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>
    private lateinit var email: String
    lateinit var provider : ProviderModel
    lateinit var items : MutableList<CarItem>
    var isNotEmptyFlag =false
    var total: Float = 0F
    var articles: Int = 0
    private var dB = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)
        email=intent.getStringExtra("email").toString()
        provider= ProviderModel.valueOf(intent.getStringExtra("provider") as String)
        items=mutableListOf()
        setup()

    }

    private fun getItems(email: String) {
        dB.collection("user").document(email).collection("shopping_cart").get().addOnSuccessListener { documents ->
            for (document in documents) {
                items.add(
                    CarItem(document.id,
                        document.get("itemAmount").toString().toFloat(),"",0F,0F,0,0,document.get("check") as Boolean
                    )
                )
            }
            if (items.isNotEmpty()){
                empty_text.visibility=View.INVISIBLE
                car_btn_purchase.isEnabled=true
                car_btn_purchase.setBackgroundResource(R.drawable.button_bg)
            }else{
                empty_text.visibility=View.VISIBLE
                car_btn_purchase.isEnabled=false
                car_btn_purchase.setBackgroundResource(R.drawable.button_disable)
            }
            createList()
        }.addOnFailureListener {
            Log.d("Query", "Error Data1 ",it)
        }
    }

    private fun setup() {
        car_btn_purchase.isEnabled=false
        shoppingAdapter = ShoppingAdapter(this,items,email)
        car_item_list.setHasFixedSize(true)
        car_item_list.adapter = shoppingAdapter
        val swipeDelete=object : SwipeDeleteAdapter(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                deleteItem(email,items[position].itemId)
                items.clear()
                getItems(email)
                if (items.isEmpty()){
                    empty_text.visibility=View.VISIBLE
                    car_btn_purchase.isEnabled=false
                    car_btn_purchase.setBackgroundResource(R.drawable.button_disable)
                }

            }

        }
        car_btn_purchase.setOnClickListener {
            items.clear()
            getItems(email)
            if (isNotEmptyFlag) {
                val intent = Intent(this, activity_popup_shopping::class.java)
                intent.putExtra("email", email)
                intent.putExtra("provider",provider.toString())
                intent.putExtra("total",total)
                intent.putExtra("articles",articles)
                startActivity(intent)
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeDelete)
        itemTouchHelper.attachToRecyclerView(car_item_list)
    }

    private fun deleteItem(email : String,id : String) {
        dB.collection("user").document(email).collection("shopping_cart").document(id).delete()
    }

    private fun createList() {
        for (i in items){
            dB.collection("product").document(i.itemId).get().addOnSuccessListener {
                for (index in items.indices){
                    if(items[index].itemId == it.id){
                        items[index].itemImage = it.get("image").toString()
                        items[index].itemDiscount = it.get("discount").toString().toFloat()
                        items[index].itemPrice = it.get("price").toString().toFloat()
                        items[index].itemMax = it.get("max_quantity").toString().toInt()
                        items[index].itemStock = it.get("stock").toString().toInt()
                    }
                }
                sumAll()
                shoppingAdapter.notifyDataSetChanged()
            }.addOnFailureListener {
                Log.d("Query", "Error Data0",it)
            }
        }

    }

    private fun sumAll(){
        total=0F
        articles = 0
        isNotEmptyFlag=false
        for (item in items.indices){
            if(items[item].itemCheck){
                isNotEmptyFlag=true
                var temp = 0F
                articles+=1
                temp+=items[item].itemPrice
                temp-=items[item].itemDiscount
                total+=temp*items[item].itemAmount
            }
        }
        valor_text.text=total.toString()
    }

    override fun onResume() {
        //After a pause or at startup
        items.clear()
        super.onResume()
        //Refresh yor stuff here
        getItems(email)
    }
}