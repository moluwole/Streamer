package com.example.yung.streamer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button_register.setOnClickListener { attemptRegistration()  }
    }

    fun attemptRegistration(){
        var emailStr = register_email.text.toString()
        var passwordStr = register_pwd.text.toString()
        var confirmPassStr = register_confirm_pwd.text.toString()

        var cancel = false
        var focusView: View? = null

        //Email Validation
        if(TextUtils.isEmpty(emailStr)){
            register_email.error = "Please enter your Email"
            focusView = register_email
            cancel = true
        }
        else if(!validateEmail(emailStr)){
            register_email.error = "Please provide a valid Email Address"
            focusView = register_email
            cancel = true
        }

        //Password Verification
        if(TextUtils.isEmpty(passwordStr)){
            register_pwd.error = "Enter your Password"
            focusView = register_pwd
            cancel = true
        }
        else if(TextUtils.isEmpty(confirmPassStr)){
            register_confirm_pwd.error = "Confirm your password to continue"
            focusView = register_confirm_pwd
            cancel = true
        }
        else if(!validatePassword(passwordStr, confirmPassStr)){
            register_confirm_pwd.error = "Ensure password match"
            register_pwd.error = "Ensure password match"
            focusView = register_pwd
            cancel = true
        }

        if(cancel){
            focusView?.requestFocus()
        }
        else{
            showProgress(true)
            var mAuth = FirebaseAuth.getInstance()
            mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener{
                task: Task<AuthResult> ->
                    if(task.isSuccessful){
                        showProgress(false)
                        Snackbar.make(register_container, "Registration Successful", Snackbar.LENGTH_LONG).show()
                        var home_intent = Intent(this, Home::class.java)

                        startActivity(home_intent)
                    }
                    else{
                        showProgress(false)
                        Snackbar.make(register_container,"Unable to Complete Registration", Snackbar.LENGTH_LONG)
                    }
            }
        }
    }

    fun validateEmail(email: String): Boolean {
        return email.contains("@")
    }

    fun validatePassword(password: String, confirmPass: String): Boolean{
       return password == confirmPass
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            register_form.visibility = if (show) View.GONE else View.VISIBLE
            register_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            register_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            reg_progress.visibility = if (show) View.VISIBLE else View.GONE
            reg_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            reg_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            reg_progress.visibility = if (show) View.VISIBLE else View.GONE
            register_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

}
