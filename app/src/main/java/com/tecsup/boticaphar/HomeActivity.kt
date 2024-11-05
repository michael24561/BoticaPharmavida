package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializa el GestureDetector
        gestureDetector = GestureDetector(this, this)

        // Obtén la referencia al BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_home

        // Establece el listener para los ítems seleccionados
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Estás en la página principal", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_cart -> {
                    // Navegar a la actividad de carrito
                    val intent = Intent(this, CarritoActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Navegar a la actividad de perfil
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Manejo de iconos de menú y notificaciones
        findViewById<ImageView>(R.id.menu_icon).setOnClickListener {
            // Redirigir a la actividad activity_perfil_completo_drawer
            val intent = Intent(this, MenuLateralActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.notification_icon).setOnClickListener {
            val intent = Intent(this, NotificacionActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Abriendo notificaciones", Toast.LENGTH_SHORT).show()
        }
    }

    // Configura el touch event para detectar gestos
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    // Implementa los métodos del GestureDetector.OnGestureListener
    override fun onDown(event: MotionEvent): Boolean = true

    override fun onFling(
        e1: MotionEvent?,
        p1: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val SWIPE_THRESHOLD = 100
        val SWIPE_VELOCITY_THRESHOLD = 100

        e1?.let { startEvent ->
            p1?.let { endEvent ->
                val diffX = endEvent.x - startEvent.x

                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // Deslizamiento hacia la derecha - ir a PerfilActivity
                        val intent = Intent(this, PerfilActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Deslizamiento hacia la izquierda - ir a CarritoActivity
                        val intent = Intent(this, CarritoActivity::class.java)
                        startActivity(intent)
                    }
                    return true
                }
            }
        }
        return false
    }

    override fun onShowPress(event: MotionEvent) {}
    override fun onSingleTapUp(event: MotionEvent): Boolean = true
    override fun onScroll(
        e1: MotionEvent?,
        p1: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean = false
    override fun onLongPress(event: MotionEvent) {}
}
