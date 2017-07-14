package com.example.yung.streamer


import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class Menu : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // return inflater!!.inflate(R.layout.fragment_menu, container, false)
        val rootview = inflater?.inflate(R.layout.fragment_menu, container, false)

        val menu_search = rootview?.findViewById(R.id.menu_search) as Button
        menu_search.setOnClickListener {
            val fragment_search = Search()
            val fragment_manager = fragmentManager
            if (home_container != null) {
                fragment_manager.beginTransaction().add(R.id.fragment_container, fragment_search).commit()
            } else {
                fragment_manager.beginTransaction().replace(R.id.fragment_container, fragment_search).commit()
            }
        }

        val menu_view = rootview.findViewById(R.id.menu_view) as Button
        menu_view.setOnClickListener {
            val fragment_view = Saved()
            val fragment_manager = fragmentManager
            val file: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path.toString(), ".streamer")
            val directory = file
            val files = directory.listFiles()
            if (files.count() != 0) {
                if (home_container != null) {
                    fragment_manager.beginTransaction().replace(R.id.fragment_container, fragment_view).commit()
                } else {
                    fragment_manager.beginTransaction().replace(R.id.fragment_container, fragment_view).commit()
                }
            } else {
                Snackbar.make(rootview, "No Offline Videos Found. Stream a video to save automatically", Snackbar.LENGTH_LONG).show()
            }

        }

        //LogOut Menu
        val menu_logout = rootview.findViewById(R.id.menu_logout) as Button
        menu_logout.setOnClickListener {
            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
            val user: FirebaseUser? = mAuth.currentUser
            if (user != null) {
                mAuth.signOut()
            }
            val intent: Intent? = Intent(rootview.context, Login::class.java)
            startActivity(intent)
        }

        //Delete Menu
        val menu_delete = rootview.findViewById(R.id.menu_delete) as Button
        menu_delete.setOnClickListener {
            val file: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path.toString(), ".streamer")
            val directory = file
            val files = directory.listFiles()
            if (files.count() != 0) {
                for (i in files.indices) {
                    files[i].delete()
                }
                Snackbar.make(rootview, "All Videos Deleted Successfully", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(rootview, "No Offline Video Found", Snackbar.LENGTH_LONG).show()
            }
        }
        return rootview
    }


}
