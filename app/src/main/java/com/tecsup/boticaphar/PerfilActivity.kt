package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class PerfilActivity : AppCompatActivity() {
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Configurar el DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_menu_perfil_completo)

        // Configurar ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Mostrar el botón de menú en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurar la navegación del menú lateral
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this, CarritoActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }

        // Configurar BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
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
                R.id.nav_profile -> {
                    true
                }
                else -> false
            }
        }

        // Cargar datos de SharedPreferences
        nameTextView = findViewById(R.id.username)
        emailTextView = findViewById(R.id.et_email)
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        val email = sharedPreferences.getString("email", null)
        val editarPerfil = findViewById<ImageView>(R.id.ic_profile)
        val misPedidos = findViewById<ImageView>(R.id.pedidos)

        editarPerfil.setOnClickListener {
            val intent = Intent(this@PerfilActivity, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        misPedidos.setOnClickListener {
            val intent = Intent(this@PerfilActivity, HistorialPedidosActivity::class.java)
            startActivity(intent)
        }

        // Muestra los detalles en los TextViews
        nameTextView.text = username ?: "Nombre no disponible"
        emailTextView.text = email ?: "Email no disponible"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
