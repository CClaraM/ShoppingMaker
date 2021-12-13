package com.myapplication.shopingmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.myapplication.shopingmarket.dataModel.CarItem
import com.myapplication.shopingmarket.dataModel.ProviderModel
import kotlinx.android.synthetic.main.activity_popup_shopping.*

class activity_popup_shopping : AppCompatActivity() {
    private lateinit var email: String
    lateinit var provider : ProviderModel
    lateinit var items : MutableList<CarItem>
    var total: Float = 0F
    var articles: Int = 0
    private var dB = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_shopping)
        val displayMetrics = DisplayMetrics()
        val windowsManager = applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceWidth = (displayMetrics.widthPixels)*0.8
        val deviceHeight = (displayMetrics.heightPixels)*0.44
        window.setLayout(deviceWidth.toInt(),deviceHeight.toInt())

        email=intent.getStringExtra("email").toString()
        provider= ProviderModel.valueOf(intent.getStringExtra("provider") as String)
        items=mutableListOf()
        btn_purchase.isEnabled=false
        getItems(email)

        btn_purchase.setOnClickListener {
            shopList()
            btn_purchase.isEnabled=false
        }

    }

    private fun getItems(email: String) {
        dB.collection("user").document(email).collection("shopping_cart").get().addOnSuccessListener { documents ->
            for (document in documents) {
                if (document.get("check") as Boolean) {
                    items.add(
                        CarItem(
                            document.id,document.get("itemAmount").toString().toFloat(),"",
                            0F,0F,0,0,
                            document.get("check") as Boolean
                        )
                    )
                }
            }
            createList()
        }.addOnFailureListener {
            Log.d("Query", "Error Data1 ",it)
        }
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
            }.addOnFailureListener {
                Log.d("Query", "Error Data0",it)
            }
        }
    }


    private fun sumAll(){
        total=0F
        articles = 0

        for (item in items.indices){
            if(items[item].itemCheck){
                var temp = 0F
                articles+=items[item].itemAmount.toInt()
                temp+=items[item].itemPrice
                temp-=items[item].itemDiscount
                total+=temp*items[item].itemAmount
            }
        }
        order_text_value.text=total.toString()

        if(articles>1) {
            order_text1.text = getString(R.string.comfirm_text) + " "+ articles + " items"
        }else{
            order_text1.text = getString(R.string.comfirm_text)+ " " + articles + " item"
        }
        btn_purchase.isEnabled=true
    }

    private fun shopList(){
        var data = hashMapOf(
            "timestamp" to FieldValue.serverTimestamp(),
            "total" to total,
            "items" to articles,
            "purchase_status" to "Processing"
        )

        dB.collection("user").document(email).collection("purchase").add(data)
            .addOnSuccessListener { documentReference ->

                for (item in items.indices){
                    val map = hashMapOf("price" to items[item].itemPrice,
                        "discount" to items[item].itemDiscount,
                        "itemAmount" to items[item].itemAmount)
                    dB.collection("user").document(email).collection("purchase")
                        .document(documentReference.id).collection("product").document(items[item].itemId).set(map)
                }
                deleteItem()
            }
    }
    private fun deleteItem() {
        for(item in items.indices) {
            dB.collection("user").document(email).collection("shopping_cart").document(items[item].itemId).delete()
                .addOnCompleteListener {

                    for (item in items.indices){
                        val t = (items[item].itemStock-items[item].itemAmount).toInt()
                        val data = hashMapOf("stock" to t)
                        dB.collection("product").document(items[item].itemId).set(data, SetOptions.merge())
                    }
                    items.clear()

                    order_complete.visibility=View.VISIBLE
                    textView7.visibility=View.INVISIBLE
                    textView6.visibility=View.INVISIBLE
                    order_text1.visibility=View.INVISIBLE
                    btn_purchase.visibility=View.INVISIBLE
                    order_text_value.visibility=View.INVISIBLE
                    
                    super.onBackPressed()
                }



        }
    }
}

