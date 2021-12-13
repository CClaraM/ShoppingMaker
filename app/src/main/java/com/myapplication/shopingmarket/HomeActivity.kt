package com.myapplication.shopingmarket

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.myapplication.shopingmarket.adapter.RecommendAdapter
import com.myapplication.shopingmarket.dataModel.IntroSlideR
import com.myapplication.shopingmarket.dataModel.ProviderModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.math.abs

class HomeActivity : AppCompatActivity() {

    /*** Slider Recommend Var***/
    private lateinit var sliderItemList: ArrayList<IntroSlideR>
    private lateinit var recommendAdapter: RecommendAdapter
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRun: Runnable
    private  var email : String? = null
    private  var provider : ProviderModel? = null
    /*** Slider Recommend Var***/

    override fun onCreate(savedInstanceState: Bundle?) {
        /**Splash**/
        Thread.sleep(2000)
        setTheme(R.style.Theme_ShopingMarket)
        /**Splash**/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val analiyticBundle = Bundle()
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        analiyticBundle.putString("message","HomeActivity")
        analytics.logEvent("InitScreen",analiyticBundle)
        /**Session**/
        loadSession()
        /**Tool bar**/
        setSupportActionBar(findViewById(R.id.mainToolbar))
        /**slider**/
        sliderItems()
        itemSliderView()

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.product_fragment_view, CategoryFragment::class.java,null,"TagFragmentCategory")
                .commit()
        }
    }

    /***  Slider Recommend ***/

    private fun sliderItems() {
        sliderItemList = ArrayList()
        recommendAdapter = RecommendAdapter(recommendSlider,sliderItemList)
        recommendSlider.adapter=recommendAdapter
        recommendSlider.clipToPadding=false
        recommendSlider.clipChildren=false
        recommendSlider.offscreenPageLimit=3
        recommendSlider.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

        val comPosPageTarn = CompositePageTransformer()
        comPosPageTarn.addTransformer(MarginPageTransformer(40))
        comPosPageTarn.addTransformer { page,position ->
            val r : Float = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        recommendSlider.setPageTransformer(comPosPageTarn)
        sliderHandler= Handler()
        sliderRun = Runnable {
            recommendSlider.currentItem = recommendSlider.currentItem + 1
        }
        recommendSlider.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRun)
                    sliderHandler.postDelayed(sliderRun,2000)
                }
            }
        )

    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRun)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRun,2000)
    }

    //Add items
    private fun itemSliderView() {
        sliderItemList.add(IntroSlideR(R.mipmap.ic_launcher))
        sliderItemList.add(IntroSlideR(R.mipmap.ic_launcher))
        sliderItemList.add(IntroSlideR(R.mipmap.ic_launcher))
        sliderItemList.add(IntroSlideR(R.mipmap.ic_launcher))
        sliderItemList.add(IntroSlideR(R.mipmap.ic_launcher))
        sliderItemList.add(IntroSlideR(R.mipmap.ic_launcher))
        sliderItemList.add(IntroSlideR(R.mipmap.ic_launcher))
        sliderItemList.add(IntroSlideR(R.mipmap.ic_launcher))
    }
    /***  Slider Recommend ***/

    /***  Toolbar ***/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
            if(provider != null) {
                if (provider == ProviderModel.UNSIGNED) {
                    val item :MenuItem = menu!!.findItem(R.id.login_btn_bar)
                    item.isVisible = true
                    item.isEnabled = true
                }else{
                    val item :MenuItem = menu!!.findItem(R.id.logout_btn_bar)
                    val itemA:MenuItem = menu!!.findItem(R.id.car_button)
                    item.isVisible = true
                    item.isEnabled = true
                    itemA.isVisible = true
                    itemA.isEnabled = true
                }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.action_search ->{

            true
        }
        R.id.login_btn_bar ->{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.car_button ->{
            val intent = Intent(this,ShoppingActivity::class.java)
            intent.putExtra("email",email)
            intent.putExtra("provider",provider.toString())
            startActivity(intent)
            true
        }
        R.id.logout_btn_bar ->{
            FirebaseAuth.getInstance().signOut()
            val prefs = getSharedPreferences(resources.getString(R.string.prefKey), Context.MODE_PRIVATE)?.edit()
            prefs!!.remove("email")
            prefs.remove("provider")
            prefs.apply()
            val intent = intent
            finish()
            startActivity(intent)
            true
        }
        else ->{
            super.onOptionsItemSelected(item)
        }
    }
    /***  Toolbar ***/

    private fun loadSession() {
        val prefs = getSharedPreferences(resources.getString(R.string.prefKey), Context.MODE_PRIVATE)
        val tempEmail = prefs.getString("email", null)
        val tempProvider = prefs.getString("provider", null)

        if (tempEmail == null && tempProvider == null) {
            provider= ProviderModel.UNSIGNED
            email= ProviderModel.UNSIGNED.toString()
            showToast(1)
//            Log.d("Prov", "Data0 $provider")
//            Log.d("Prov", "Data1 $email")
        }else{
            email=tempEmail
            provider= ProviderModel.valueOf(tempProvider!!)
//            Log.d("Prov", "Data0 $provider")
//            Log.d("Prov", "Data1 $email")
        }
    }

    private fun showToast(int: Int){
        when (int){
            1 ->Toast.makeText(applicationContext,R.string.noLogin,Toast.LENGTH_LONG).show()
            2 ->Toast.makeText(applicationContext,"",Toast.LENGTH_LONG).show()

        }

    }

}

