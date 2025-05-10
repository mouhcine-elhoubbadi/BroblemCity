package com.example.myappl.location


import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myappl.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class MapPickerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_picker)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val safiLocation = LatLng(32.300788, -9.237183)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(safiLocation, 15f))

        var marker: Marker? = null

        map.setOnMapClickListener { latLng ->
            marker?.remove()


            val geocoder = Geocoder(this, Locale.getDefault())
            val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            val address = if (addressList != null && addressList.isNotEmpty()) {
                addressList[0].getAddressLine(0) ?: "Lieu inconnu"
            } else {
                "Lieu inconnu"
            }


            marker = map.addMarker(MarkerOptions().position(latLng).title(address))


            val resultIntent = Intent().apply {
                putExtra("latitude", latLng.latitude)
                putExtra("longitude", latLng.longitude)
                putExtra("location_name", address)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
