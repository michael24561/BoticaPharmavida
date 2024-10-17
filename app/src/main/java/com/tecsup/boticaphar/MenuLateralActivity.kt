package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.widget.TextView

class MenuLateralActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_completo_drawer)

        // Inicialización del DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_menu_perfil_completo)
        navigationView = findViewById(R.id.nav_menu_completo_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Obtener los datos del usuario de SharedPreferences
        val sharedPreferences = getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE)
        val nombreUsuario = sharedPreferences.getString("usuario_nombre", "Usuario")
        val gmailUsuario = sharedPreferences.getString("usuario_gmail", "usuario@gmail.com")

        // Establecer el nombre en el TextView del header
        val headerView = navigationView.getHeaderView(0)
        val txtNombre = headerView.findViewById<TextView>(R.id.username)
        val txtGmail = headerView.findViewById<TextView>(R.id.et_email)

        txtNombre.text = nombreUsuario
        txtGmail.text = gmailUsuario
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_cerrar_sesion -> {
                // Eliminar los datos de sesión
                val sharedPreferences = getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                // Redirigir al login
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        closeDrawer()
        return true
    }

    private fun openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    // Método para cerrar el drawer
    private fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    // Manejar el botón de "Back" en el dispositivo para cerrar el Drawer si está abierto
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
