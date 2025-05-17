package com.example.myappl

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myappl.Activites.ReportsActivity
import com.example.myappl.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var  drawerLayout :DrawerLayout
    lateinit var activite : Activity
    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

         drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
       // val button = findViewById<Button>(R.id.button)



        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)


        val btnNav = findViewById<BottomNavigationView>(R.id.btnNav)
        activite =Activity()

        btnNav.setOnItemSelectedListener { index ->
            when (index.itemId) {
               // R.id.home -> setCurrentFragment(activite)
               // R.id.profile ->ProfileFragment()
                R.id.share ->shareApp()
                R.id.logOut ->showLogoutConfirmation()
            }
            true
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_new_report -> {
                startActivity(Intent(this, Add_signal_Activity::class.java))

            }

            R.id.nav_reports -> {
                startActivity(Intent(this, ReportsActivity::class.java))
            }
        }


            drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun shareApp() {
        val appLink = "https://play.google.com/store/apps/details?id=com.example.myappl"

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: $appLink")
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun logout() {

        sharedPreferences.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ -> logout() }
            .setNegativeButton("Cancel", null)
            .show()
    }
}