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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.myapplication.shopingmarket.dataModel.ProviderModel

class SignupTabFragment : Fragment() {
    var mail: EditText? = null
    var pass: EditText? = null
    var confPass: EditText? = null
    var mobile: TextView? = null
    var signUp: Button? = null
    var fabTemp = 0f
    private var dB = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.signup_tab_fragment, container, false) as ViewGroup
        mail = root.findViewById(R.id.mail_up)
        mobile = root.findViewById(R.id.mobile)
        pass = root.findViewById(R.id.pass_up)
        confPass = root.findViewById(R.id.pass_conf)
        signUp = root.findViewById(R.id.sign_button)
        setupAnimations()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
    }

    private fun setupFragment() {
        signUp!!.setOnClickListener {
            if (mail!!.text.isNotEmpty() && pass!!.text.isNotEmpty() && confPass!!.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail!!.text.toString(),pass!!.text.toString())
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            fullRegistration(it.result.user?.email, ProviderModel.BASIC)
                        }else{
                            showAlert()
                        }
                    }
            }else{
                Toast.makeText(activity,"no negro escriba alguna miereda", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fullRegistration(myEmail: String?, provider: ProviderModel) {
        val prefs = context?.getSharedPreferences(resources.getString(R.string.prefKey), Context.MODE_PRIVATE)?.edit()
        prefs!!.putString("email",myEmail)
        prefs.putString("provider",provider.toString())
        prefs.apply()
        val data = hashMapOf(
            "provider" to provider.toString(),
            "cell" to "",
            "user_name" to ""
        )
        myEmail?.let { dB.collection("user").document(it).set(data).addOnSuccessListener {
            startActivity(Intent(activity,HomeActivity::class.java))
            }
        }

    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(activity).setTitle("Error")
            .setMessage("An error has occurred creating the user")
            .setPositiveButton("Aceptar",null)
        val dialog: AlertDialog=builder.create()
        dialog.show()

    }

    private fun setupAnimations() {
        mail?.translationX=800F
        mobile?.translationX = 800f
        pass?.translationX = 800f
        confPass?.translationX = 800f
        signUp?.translationX = 800f

        mail?.alpha=fabTemp
        mobile?.alpha = fabTemp
        pass?.alpha = fabTemp
        confPass?.setAlpha(fabTemp)
        signUp?.alpha = fabTemp

        mail!!.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
        mobile!!.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
        pass!!.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(700).start()
        confPass!!.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(900).start()
        signUp!!.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(1100).start()
    }
}