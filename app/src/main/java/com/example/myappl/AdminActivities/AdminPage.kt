package com.example.myappl.AdminActivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myappl.Activites.ReportsActivity
import com.example.myappl.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminPage : AppCompatActivity() {
    private lateinit var reportsCountText: TextView
    private lateinit var categoriesCountText: TextView
    private lateinit var usersCountText: TextView
    private lateinit var manageReportsButton: Button
    private lateinit var manageCategoriesButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_page)
        reportsCountText = findViewById(R.id.reportsCount)
        categoriesCountText = findViewById(R.id.categoriesCount)
        usersCountText = findViewById(R.id.usersCount)
        manageReportsButton = findViewById(R.id.manageReportsButton)
        manageCategoriesButton = findViewById(R.id.manageCategoriesButton)

        loadCounts()

        manageReportsButton.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }

    }
    private fun loadCounts() {
        val db = FirebaseFirestore.getInstance()

        db.collection("reports").get().addOnSuccessListener {
            reportsCountText.text = "البلاغات: ${it.size()}"
        }

       /* db.collection("categories").get().addOnSuccessListener {
            categoriesCountText.text = "الفئات: ${it.size()}"
        }*/

        db.collection("users").get().addOnSuccessListener {
            usersCountText.text = "المستخدمين: ${it.size()}"
        }
    }
}