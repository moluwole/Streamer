package com.example.yung.streamer

import android.os.Bundle
import android.os.Environment
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File

class Home : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                loadMenu()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_saved -> {

                val file: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path.toString(), ".streamer")
                val directory = file
                val files = directory.listFiles()
                if (files.count() != 0) {
                    val fragment_saved = Saved()
                    switchContent(fragment_saved)
                } else {
                    Snackbar.make(container, "No Offline Videos Found. Stream a video to save automatically", Snackbar.LENGTH_LONG).show()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                val fragment_search = Search()
                switchContent(fragment_search)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun switchContent(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment, fragment.toString())
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onBackPressed() {
        finish()
    }

    fun loadMenu() {
        val fragment_menu = Menu()
        val fragment_manager = supportFragmentManager

        if(home_container == null){
            fragment_manager.beginTransaction().add(R.id.fragment_container, fragment_menu).commit()
        }
        else{
            fragment_manager.beginTransaction().replace(R.id.fragment_container, fragment_menu).commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        loadMenu()

        //Load the Menus


    }
}
