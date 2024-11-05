package com.tecsup.boticaphar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class PerfilActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        gestureDetector = GestureDetectorCompat(this, SwipeGestureListener())
        nameTextView = findViewById(R.id.username)
        emailTextView = findViewById(R.id.et_email)

        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        val email = sharedPreferences.getString("email", null)

        val editarPerfil = findViewById<ImageView>(R.id.ic_profile)
        val misPedidos = findViewById<ImageView>(R.id.pedidos)

        editarPerfil.setOnClickListener {
            startActivity(Intent(this, EditarPerfilActivity::class.java))
        }

        misPedidos.setOnClickListener {
            startActivity(Intent(this, HistorialPedidosActivity::class.java))
        }

        nameTextView.text = username ?: "Nombre no disponible"
        emailTextView.text = email ?: "Email no disponible"

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_profile

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this, CarritoActivity::class.java))
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }

        val btnCerrarSesion = findViewById<LinearLayout>(R.id.btn_cerrar_sesion)
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (e1 != null && e2 != null) {
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > SWIPE_THRESHOLD && diffX > 0) {
                    startActivity(Intent(this@PerfilActivity, CarritoActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    return true
                }
            }
            return false
        }
    }

    private fun cerrarSesion() {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
