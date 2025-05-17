package com.example.myappl.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.myappl.R

class ShareFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the share button
        val shareButton = view.findViewById<Button>(R.id.btn_share_app)

        // Set click listener
        shareButton.setOnClickListener {
            shareApp()
        }
    }

    private fun shareApp() {
        // Replace with your app's actual Play Store link
        val appLink = "https://play.google.com/store/apps/details?id=com.example.myappl"

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: $appLink")
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}
