package com.nehalappstudio.footballlive

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nehalappstudio.footballlive.fragment.Leagues
import com.nehalappstudio.footballlive.fragment.Matches
import com.nehalappstudio.footballlive.fragment.More
import com.nehalappstudio.footballlive.fragment.TV
import com.nehalappstudio.footballlive.fragment.Team

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        replaceFragment(Matches())

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_matches -> replaceFragment(Matches())
                R.id.navigation_leagues -> replaceFragment(Leagues())
                R.id.navigation_live_tv -> replaceFragment(TV())
                R.id.navigation_team -> replaceFragment(Team())
                R.id.navigation_more -> replaceFragment(More())
            }
            true
        }



    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right, // Pop enter animation (for back stack)
                R.anim.slide_out_right, // Pop exit animation (for back stack)
                R.anim.slide_in_left,  // Enter animation
                R.anim.slide_out_left, // Exit animation

            )
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        Log.d("back_pressed", fragmentManager.backStackEntryCount.toString())
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else if(bottomNavigationView.selectedItemId != R.id.navigation_matches){
            replaceFragment(Matches())
            bottomNavigationView.selectedItemId = R.id.navigation_matches;
        }else {
            super.onBackPressed()
        }
    }

}