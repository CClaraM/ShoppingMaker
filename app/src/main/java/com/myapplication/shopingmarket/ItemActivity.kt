package com.myapplication.shopingmarket

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.myapplication.shopingmarket.adapter.ItemSliderAdapter
import com.myapplication.shopingmarket.adapter.QuestionAdapter
import com.myapplication.shopingmarket.adapter.RatingAdapter
import com.myapplication.shopingmarket.dataModel.*
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.details_fragment.*
import java.util.*
import kotlin.collections.ArrayList

class ItemActivity : AppCompatActivity(){

    private lateinit var itemSliderAdapter : ItemSliderAdapter
    private lateinit var questionsAdapter: RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>
    private lateinit var ratingAdapter: RecyclerView.Adapter<RatingAdapter.RatingViewHolder>
    private lateinit var email: String
    lateinit var provider : ProviderModel
    private lateinit var itemId: String
    private var itemData: ItemData? = null
    private var itemInfo: ItemInfo? = null
    private var totalPrice: Float? = null
    private var images = ArrayList<String>()
    private var questions: MutableList<QuestionsModel> = mutableListOf()
    private var ratings: MutableList<RatingModel> = mutableListOf()
    private var dB = FirebaseFirestore.getInstance()


    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        amount_spinner.isEnabled = false
        /**Session**/
        loadSession()

        itemId = intent.getStringExtra("Product").toString()
        itemData = ItemData()
        itemInfo = ItemInfo()
        images = ArrayList()
        getItem(itemId)
        disableUi()
        createImage()
        createQuestion()
        createRating()

        amount_spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (itemData!!.price != null || itemData!!.discount != null ){
                    val myPrice = (itemData!!.price?.minus(itemData!!.discount!!))
                    val normalPrice : Float = itemData!!.price!! * (position+1).toFloat()

                    totalPrice=myPrice!! * (position+1).toFloat()
                    total_price.text=totalPrice.toString()
                    normal_price.text=normalPrice.toString()
                }
                if (itemData!!.discount!! > 0F){
                    if(itemData!!.discontinued == false){
                        normal_price.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                        currency_n.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                        normal_text.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                        currency_n.visibility = View.VISIBLE
                        normal_price.visibility = View.VISIBLE
                        normal_text.visibility = View.VISIBLE
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.description_fragment, FragmentDescription::class.java,null,"TagDescription")
                .commit()
        }

        login_btn.setOnClickListener {
            if(email != "UNSIGNED" && provider != ProviderModel.UNSIGNED) {
                val a = amount_spinner.selectedItemPosition + 1
                generateICar(a,itemId)
            }else {
                Toast.makeText(applicationContext, R.string.noLogin, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun generateICar(amount: Int, itemId: String) {
        val data = hashMapOf("itemAmount" to amount,"check" to true)
        dB.collection("user").document(email).collection("shopping_cart")
            .document(itemId).set(data).addOnSuccessListener {
                Toast.makeText(applicationContext, R.string.added, Toast.LENGTH_LONG).show()
                login_btn.setText(R.string.modify)
                val btn = login_btn.layoutParams
                btn.width=440
                login_btn.layoutParams = btn
        }.addOnFailureListener { e -> Log.w("Prov", "Error writing document", e) }
    }

    private fun getItem(string: String) {

        dB.collection("product").document(string).get().addOnSuccessListener {
            if (it != null) {
                itemData = it.toObject<ItemData>()   /***Asignacion de datos***/
                itemData!!.id =it.id
//                Log.d("Query", "Data0 ${it.data}")
                item_activity_title.text = itemData!!.title_en
                ratingBar.rating = itemData!!.current_score!!.toFloat()
                score.text = itemData!!.current_score!!.toString()
                shortDec.text = itemData!!.description //Descripcion simple

                val max : Int = itemData!!.max_quantity!!.toInt()
                val myStock : Int? = itemData!!.stock?.toInt()
                val count : MutableList<Int> = mutableListOf()
                if (myStock != null) {
                    if (myStock > max){
                        for (i in 1..max){
                            count.add(i)
                        }
                        amount_spinner.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,count)
                        if(itemData!!.discontinued == false){
                            amount_spinner.isEnabled = true
                            login_btn.setBackgroundResource(R.drawable.button_bg)
                            login_btn.isEnabled = true
                            price_total.visibility = View.VISIBLE
                            total_price.visibility = View.VISIBLE
                            currency.visibility = View.VISIBLE
                        }else{
                            item_disable.visibility = View.VISIBLE
                        }
//
                    }else{
                        for (i in 1..myStock){
                            count.add(i)
                        }
                        amount_spinner.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,count)
                        if(itemData!!.discontinued == false){
                            amount_spinner.isEnabled = true
                            login_btn.setBackgroundResource(R.drawable.button_bg)
                            login_btn.isEnabled = true
                            price_total.visibility = View.VISIBLE
                            total_price.visibility = View.VISIBLE
                            currency.visibility = View.VISIBLE
                        }else{
                            item_disable.visibility = View.VISIBLE
                        }
                    }
                }
            }else{
//                Log.d("Query", "No Data0")
            }
        }.addOnFailureListener {
//            Log.d("Query", "Error Data0",it)
        }

        dB.collection("product").document(string).collection("Long_description").document(string).get()
            .addOnSuccessListener {
                if (it != null){
                    itemInfo = it.toObject<ItemInfo>()
                    if (itemInfo?.images != null){
                        val v =itemInfo!!.images.toString().split(",").toTypedArray()
                        for (i in v.indices){
                            images.add(v[i])
//                            Log.d("Query", "image source: ${v[i]}")
                            brand.text = "Brand: "+itemInfo!!.trademark
                        }
                    }else{
                        Log.d("Query", "No Data1")
                        images.add("https://firebasestorage.googleapis.com/v0/b/test-udecaldas.appspot.com/o/resorce%2Fimage_not_found.png?alt=media&token=dab5bdb2-11f7-4dd1-b95b-c09caa891373")
                    }
//                    Log.d("Query", "Data1 ${it.data}")
                }else{
                    Log.d("Query", "No Data1")
                    images.add("https://firebasestorage.googleapis.com/v0/b/test-udecaldas.appspot.com/o/resorce%2Fimage_not_found.png?alt=media&token=dab5bdb2-11f7-4dd1-b95b-c09caa891373")
                }
                if (itemInfo?.condition_new != null) {
                    if (itemInfo?.condition_new != true) {
                        condition_info.text = "Used"
                        condition_info.setTextColor(ContextCompat.getColor(this,R.color.holo_red_dark))
                    } else {
                        condition_info.text = "NEW"
                    }
                }
                if (itemInfo?.material != null){
                    material_info.text= itemInfo!!.material
                }
                if (itemInfo?.warranty != null){
                    if (itemInfo!!.warranty!! > 0F){
                        warranty_info.text = itemInfo!!.warranty?.toInt().toString()+" months warranty"
                    }else{
                        warranty_info.text = "The article does not have a guarantee for damage or malfunction"
                        warranty_info.setTextColor(ContextCompat.getColor(this,R.color.holo_red_dark))
                    }
                }
                if(itemInfo?.detailed_description != null){
                    long_description.text= itemInfo!!.detailed_description
                }
                itemSliderAdapter.notifyDataSetChanged()
            }.addOnFailureListener {
                Log.d("Query", "Error Data1 ",it)
            }

        dB.collection("product").document(string).collection("questions").get().addOnSuccessListener { documents ->
                for (document in documents) {

                    val timestamp = document.get("timeQuestion") as com.google.firebase.Timestamp
                    val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                    val netDate = Date(milliseconds)

                    val timestampA = document.get("timeReply") as com.google.firebase.Timestamp
                    val millisecondsA = timestampA.seconds * 1000 + timestampA.nanoseconds / 1000000
                    val netDateA = Date(millisecondsA)

                    questions.add(
                        QuestionsModel(document.get("idUser").toString(),
                            document.get("reply").toString(),
                            netDate,
                            netDateA,
                            document.get("userQuestion").toString())
                    )
                }
            questionsAdapter.notifyDataSetChanged()
            }

        dB.collection("product").document(string).collection("user_qualification").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val i = document.get("score") as Long
                ratings.add(
                    RatingModel(
                        i.toFloat(),
                        document.get("short_comments").toString(),
                        document.get("user").toString())
                )
            }
            ratingAdapter.notifyDataSetChanged()
        }
    }

    private fun createImage() {
        itemSliderAdapter = ItemSliderAdapter(this@ItemActivity,images)
        item_slider.adapter=itemSliderAdapter
        TabLayoutMediator(dotsImage,item_slider) { tab, position ->
            //Here you can set something for tab like set text...etc
        }.attach()
        item_slider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
//                Snackbar.make(parent_view,"You are selected "+(position+1),Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun disableUi(){
        currency_n.visibility = View.INVISIBLE
        normal_price.visibility = View.INVISIBLE
        normal_text.visibility = View.INVISIBLE
        price_total.visibility = View.INVISIBLE
        total_price.visibility = View.INVISIBLE
        currency.visibility = View.INVISIBLE
        item_disable.visibility = View.INVISIBLE
        login_btn.isEnabled = false
    }

    private fun createQuestion() {
        questionsAdapter = QuestionAdapter(this, questions)
        item_list.setHasFixedSize(true)
        item_list.adapter = questionsAdapter
    }

    private fun createRating() {
        ratingAdapter = RatingAdapter(this,ratings)
        ratings_list.setHasFixedSize(true)
        ratings_list.adapter = ratingAdapter
    }

    private fun loadSession() {
        val prefs = getSharedPreferences(resources.getString(R.string.prefKey), Context.MODE_PRIVATE)
        val tempEmail = prefs.getString("email", null)
        val tempProvider = prefs.getString("provider", null)

        if (tempEmail == null && tempProvider == null) {
            provider= ProviderModel.UNSIGNED
            email= ProviderModel.UNSIGNED.toString()
            Log.d("Prov", "Data0 $provider")
            Log.d("Prov", "Data1 $email")
        }else{
            email=tempEmail!!
            provider= ProviderModel.valueOf(tempProvider!!)
            Log.d("Prov", "Data0 $provider")
            Log.d("Prov", "Data1 $email")
        }
    }
}