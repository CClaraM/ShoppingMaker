package com.myapplication.shopingmarket


import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import android.os.Bundle
import com.myapplication.shopingmarket.adapter.LoginAdapter
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var fabTemp = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Login menu")
        analytics.logEvent("LoginActivity",bundle)

        tab_layout.addTab(tab_layout.newTab().setText("Login").setId(1))
        tab_layout.addTab(tab_layout.newTab().setText("Signup").setId(2))
        tab_layout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = LoginAdapter(supportFragmentManager, this, tab_layout.tabCount)
        view_pager.adapter = adapter
        view_pager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tab_layout))

        setupAnimations()
        setupActivity()

    }

    private fun setupActivity() {

    }

    private fun setupAnimations() {
        fab_meta.translationY = 300f
        fab_google.translationY = 300f
        tab_layout.translationY = 300f

        fab_meta.alpha = fabTemp
        fab_google.alpha = fabTemp
        tab_layout.alpha = fabTemp

        fab_meta.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(400).start()
        fab_google.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(600).start()
        tab_layout.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(100).start()
    }
}