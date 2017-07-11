package com.example.yung.streamer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    private var initial_intent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if(currentUser == null){    initial_intent = Intent(this, Login::class.java)  }
        else{  initial_intent = Intent(this,Home::class.java)   }

        startActivity(initial_intent)
        finish()
    }
}
