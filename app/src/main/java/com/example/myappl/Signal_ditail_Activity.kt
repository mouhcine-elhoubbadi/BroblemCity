package com.example.myappl

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myappl.Activites.ReportsActivity
import java.util.Locale.Category

class Signal_ditail_Activity : AppCompatActivity() {
    private lateinit var titleText: TextView
    private lateinit var date: TextView
    private lateinit var category: TextView
    private lateinit var descriptionText: TextView
    private lateinit var imageView: ImageView
    private lateinit var locationText: TextView
    private lateinit var imageButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signal_ditail)
        titleText = findViewById(R.id.signalTitle)
        descriptionText = findViewById(R.id.signalDescription)
        imageView = findViewById(R.id.signalImage)
        date = findViewById(R.id.signalDate)
        category = findViewById(R.id.category)
        locationText = findViewById(R.id.location)
        imageButton = findViewById(R.id.retour)


        val reportTitle = intent.getStringExtra("reportTitle")
        val reportDescription = intent.getStringExtra("reportDescription")
        val reportImage = intent.getStringExtra("reportImage")
        val typeprobleme = intent.getStringExtra("typeprobleme")
        val time = intent.getStringExtra("time")
        val location = intent.getStringExtra("location")



        titleText.text = reportTitle
        descriptionText.text = reportDescription
        date.text = time
        category.text = typeprobleme
        locationText.text = location



        if (!reportImage.isNullOrEmpty()) {
            Glide.with(this).load(reportImage).into(imageView)
        } else {
            imageView.setImageResource(R.drawable.sample_image)
        }

        imageButton.setOnClickListener{
            val intent = Intent(this, ReportsActivity::class.java)
            startActivity(intent)
        }
    }
}