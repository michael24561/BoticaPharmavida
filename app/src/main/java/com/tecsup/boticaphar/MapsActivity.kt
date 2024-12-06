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

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

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

        // Obtener las coordenadas del Intent
        val coordinates = intent.getParcelableArrayListExtra<LatLng>("coordinates")

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isRotateGesturesEnabled = false

        // Agregar cada coordenada como marcador en el mapa
        coordinates?.forEach { coordinate ->
            map.addMarker(MarkerOptions().position(coordinate).title("Ubicación"))
        }

        // Puedes centrar el mapa en la primera coordenada si lo deseas
        if (coordinates != null && coordinates.isNotEmpty()) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates[0], 16f))
        }
    }
}
