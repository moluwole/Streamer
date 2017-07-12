package com.example.yung.streamer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.activity_home.*


/**
 * A simple [Fragment] subclass.
 * Use the [Menu.newInstance] factory method to
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
        var rootview = inflater?.inflate(R.layout.fragment_menu, container, false)

        var menu_search = rootview?.findViewById(R.id.menu_search) as Button
        menu_search.setOnClickListener {
            val fragment_search = Search()
            val fragment_manager = fragmentManager
            if (home_container != null) {
                fragment_manager.beginTransaction().add(R.id.fragment_container, fragment_search).commit()
            } else {
                fragment_manager.beginTransaction().replace(R.id.fragment_container, fragment_search).commit()
            }
        }

        var menu_view = rootview.findViewById(R.id.menu_view) as Button
        menu_view.setOnClickListener {
            val fragment_view = Saved()
            val fragment_manager = fragmentManager
            if (home_container != null) {
                fragment_manager.beginTransaction().replace(R.id.fragment_container, fragment_view).commit()
            } else {
                fragment_manager.beginTransaction().replace(R.id.fragment_container, fragment_view).commit()
            }
        }
        return rootview
    }


}
