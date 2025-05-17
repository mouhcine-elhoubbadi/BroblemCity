package com.example.myappl.AdminActivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myappl.Activites.ReportsActivity
import com.example.myappl.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminPage : AppCompatActivity() {
    private lateinit var reportsCountText: TextView
    private lateinit var usersCountText: TextView
    private lateinit var manageReportsButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_page)
        reportsCountText = findViewById(R.id.reportsCount)
        usersCountText = findViewById(R.id.usersCount)
        manageReportsButton = findViewById(R.id.manageReportsButton)

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()
        loadCounts(dialog)

        manageReportsButton.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }

    }
    private fun loadCounts(dialog: AlertDialog) {
        val db = FirebaseFirestore.getInstance()


        db.collection("reports").get().addOnSuccessListener {
            reportsCountText.text = "NEMBRE DES UTULISATEUR EST:  ${it.size()}"
            dialog.dismiss()
        }



       /* db.collection("categories").get().addOnSuccessListener {
            categoriesCountText.text = "CATEGORY: ${it.size()}"
        }*/

        db.collection("users").get().addOnSuccessListener {
            usersCountText.text = "NEMBRE DES RAPORTS EST:  ${it.size()}"
        }
    }
}