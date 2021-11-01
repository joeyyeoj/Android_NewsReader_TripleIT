package nl.joey.peschier

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private val utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppPreferences.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        var bundle = Bundle().apply {
            putInt("FEED_ID", 0)
        }
        val algemeenFragment = AlgemeenFragment()
        algemeenFragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, algemeenFragment).commit()
        supportActionBar?.setTitle(getString(R.string.laatste))

        val menu = navigationView.menu

        if(AppPreferences.isLogin){
            menu.findItem(R.id.login).setVisible(false)
            menu.findItem(R.id.register).setVisible(false)
        }
        else{
            menu.findItem(R.id.login).setVisible(true)
            menu.findItem(R.id.register).setVisible(true)
            menu.findItem(R.id.logout).setVisible(false)
        }
    }




    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var feedId = 0

        when(item.itemId){
            R.id.nav_laatste -> {
                feedId = 0
                openFeed(0)
                supportActionBar?.setTitle(getString(R.string.laatste))
            }
            R.id.nav_algemeen -> {
                feedId = 1
                openFeed(1)
                supportActionBar?.setTitle(getString(R.string.algemeen))
            }
            R.id.nav_internet -> {
                feedId = 2
                openFeed(2)
                supportActionBar?.setTitle(getString(R.string.internet))
            }
            R.id.nav_sport -> {
                feedId = 3
                openFeed(3)
                supportActionBar?.setTitle(getString(R.string.sport))
            }
            R.id.nav_opmerkelijk -> {
                feedId = 4
                openFeed(4)
                supportActionBar?.setTitle(getString(R.string.opmerkelijk))
            }
            R.id.nav_games -> {
                feedId = 5
                openFeed(5)
                supportActionBar?.setTitle(getString(R.string.games))
            }
            R.id.nav_wetenschap -> {
                feedId = 6
                openFeed(6)
                supportActionBar?.setTitle(getString(R.string.wetenschap))
            }
            R.id.nav_liked -> {
                feedId = 0
                openFeed(7)
                supportActionBar?.setTitle(getString(R.string.favorieten))
            }

            R.id.login -> {
                val bundle = Bundle().apply {
                    putInt("ACTION", 1)
                }
                val loginFragment = LoginFragment()
                loginFragment.arguments = bundle
                supportActionBar?.setTitle(getString(R.string.login))
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, loginFragment).commit()
                drawer.closeDrawer(GravityCompat.START)
            }

            R.id.register -> {
                val bundle = Bundle().apply {
                    putInt("ACTION", 2)
                }
                val loginFragment = LoginFragment()
                loginFragment.arguments = bundle
                supportActionBar?.setTitle(getString(R.string.register))
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, loginFragment).commit()
                drawer.closeDrawer(GravityCompat.START)
            }

            R.id.logout -> {
                val logoutFragment = LogoutFragment()
                supportActionBar?.setTitle(getString(R.string.logout))
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, logoutFragment).commit()
                drawer.closeDrawer(GravityCompat.START)
            }
        }



        return true
    }

    fun openFeed(feedId: Int){
        var bundle = Bundle().apply {
            putInt("FEED_ID", feedId)
        }
        val algemeenFragment = AlgemeenFragment()
        algemeenFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, algemeenFragment).commit()
        drawer.closeDrawer(GravityCompat.START)
    }

    fun setActionBarTitle(title: String){
        supportActionBar?.setTitle(title)
    }


    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }

    }
}