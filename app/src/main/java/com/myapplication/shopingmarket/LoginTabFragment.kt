package com.myapplication.shopingmarket

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.myapplication.shopingmarket.dataModel.ProviderModel


class LoginTabFragment : Fragment() {
    var mail: EditText? = null
    var pass: EditText? = null
    var passBtn: TextInputLayout?=null
    var forgetPass: TextView? = null
    var login: Button? = null
    var fabTemp = 0f
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.login_tab_fragment, container, false) as ViewGroup
        mail = root.findViewById(R.id.mail_field)
        pass = root.findViewById(R.id.pass)
        passBtn = root.findViewById(R.id.pass_btn)
        forgetPass = root.findViewById(R.id.forget_Pass)
        login = root.findViewById(R.id.login_btn)
        setupAnimations()
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
    }

    private fun setupFragment() {
        login!!.setOnClickListener {
            if (mail!!.text.isNotEmpty() && pass!!.text.isNotEmpty()){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(mail!!.text.toString(),pass!!.text.toString())
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            singIn(it.result.user?.email, ProviderModel.BASIC)
                        }else{
                            showAlert()
                        }
                    }
            }else{
                Toast.makeText(activity,"no negro escriba alguna miereda", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(activity).setTitle("Error")
            .setMessage("Wrong username or password ")
            .setPositiveButton("Ok",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

    private fun singIn(myEmail: String?, provider: ProviderModel) {
        val prefs = context?.getSharedPreferences(resources.getString(R.string.prefKey), Context.MODE_PRIVATE)?.edit()
        prefs!!.putString("email",myEmail)
        prefs.putString("provider",provider.toString())
        prefs.apply()
        startActivity(Intent(activity,HomeActivity::class.java))
    }

    private fun setupAnimations() {
        mail?.translationX=800F
        passBtn?.translationX=800F
        pass?.translationX=800F
        forgetPass?.translationX=800F
        login?.translationX=800F

        mail?.alpha=fabTemp
        pass?.alpha=fabTemp
        passBtn?.alpha=fabTemp
        forgetPass?.alpha=fabTemp
        login?.alpha=fabTemp

        mail!!.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
        pass!!.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(500).start()
        passBtn!!.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(500).start()
        forgetPass!!.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(500).start()
        login!!.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(700).start()
    }
}