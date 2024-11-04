package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import android.widget.ImageView

class MenuLateralActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private var isSubmenuVisible = false // Control de visibilidad del submenú

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_completo_drawer)

        // Inicializar el DrawerLayout
        drawerLayout = findViewById(R.id.drawer_menu_perfil_completo)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val menuIcon: ImageView = findViewById(R.id.menu_icon)
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val navigationView: NavigationView = findViewById(R.id.nav_menu_completo_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_categoria -> {
                    // Alternar visibilidad de las subcategorías cuando se presiona "Categoría"
                    toggleSubmenu(navigationView)
                    false // No cerrar el menú al presionar "Categoría"
                }
                R.id.nav_categoria1 -> {
                    // Acción para Subcategoría 1
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_categoria2 -> {
                    // Acción para Subcategoría 2
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_categoria3 -> {
                    // Acción para Subcategoría 3
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_ofertas -> {
                    // Acción para Ofertas
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_contactanos -> {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://www.youtube.com/watch?v=uWfbR_juSdY")
                    }
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_sobre_nosotros -> {
                    val intent = Intent(this, SobrenosotrosActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
    }

    // Función para expandir y contraer las subcategorías
    private fun toggleSubmenu(navigationView: NavigationView) {
        isSubmenuVisible = !isSubmenuVisible // Alternar visibilidad

        // Cambiar visibilidad de las subcategorías
        navigationView.menu.findItem(R.id.nav_categoria1).isVisible = isSubmenuVisible
        navigationView.menu.findItem(R.id.nav_categoria2).isVisible = isSubmenuVisible
        navigationView.menu.findItem(R.id.nav_categoria3).isVisible = isSubmenuVisible
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
