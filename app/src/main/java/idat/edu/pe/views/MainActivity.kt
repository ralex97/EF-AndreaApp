package idat.edu.pe.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import idat.edu.pe.R
import idat.edu.pe.databinding.ActivityMainBinding
import idat.edu.pe.views.fragments.FavoritesFragment
import idat.edu.pe.views.fragments.HomeFragment
import idat.edu.pe.views.fragments.PerfilFragment
import idat.edu.pe.views.fragments.ShoppingFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var drawer_layout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var nav_view: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        toolbar = findViewById(R.id.toolbar)
        drawer_layout = findViewById(R.id.drawer_layout)
        nav_view = findViewById(R.id.nav_view)



        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportFragmentManager.beginTransaction().replace(R.id.mainContent, HomeFragment()).commit()

        nav_view.setNavigationItemSelectedListener(this)
        findViewById<TextView>(R.id.toolbarIndicator).text = "20"

    }

    private fun signOut() {
        Firebase.auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_first_fragment -> {
                supportFragmentManager.beginTransaction().replace(R.id.mainContent,HomeFragment()).commit()
                setTitle("Import")
            }
            R.id.nav_close_sesion -> {
                signOut()
            }
            R.id.nav_second_fragment -> {
                supportFragmentManager.beginTransaction().replace(R.id.mainContent,FavoritesFragment()).commit()
                setTitle("Gallery")
            }
            R.id.nav_third_fragment -> {
                supportFragmentManager.beginTransaction().replace(R.id.mainContent,ShoppingFragment()).commit()
                setTitle("Carrito")
            }
            R.id.nav_pefil -> {
                supportFragmentManager.beginTransaction().replace(R.id.mainContent,PerfilFragment()).commit()
                setTitle("Perfil")
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}