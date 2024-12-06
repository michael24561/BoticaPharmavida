package com.tecsup.boticaphar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMap)
            as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

        override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true // zoom-botones
        map.uiSettings.isRotateGesturesEnabled = false

        val marker = LatLng(-8.094023264182493,-79.03693931820501)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(marker, 18f), 4000, null
        )
        map.setOnMapClickListener { latLong ->
            map.clear()
            map.addMarker(
                MarkerOptions().
            position(latLong).title("Seleccionado"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 18f))

            val lat = latLong.latitude
            val lng = latLong.longitude

            Toast.makeText(this, "Lat: $lat, Lng: $lng",
                Toast.LENGTH_SHORT).show()
        }

    }

}