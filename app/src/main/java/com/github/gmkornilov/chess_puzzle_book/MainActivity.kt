package com.github.gmkornilov.chess_puzzle_book

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.gmkornilov.chess_puzzle_book.data.providers.RemoteTaskProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation)

        val baseUrl = resources.getString(R.string.base_url)
        val argUrl = NavArgument.Builder().setDefaultValue(baseUrl).build()
        val argProvider = NavArgument.Builder().setDefaultValue(RemoteTaskProvider(baseUrl)).build()
        graph.addArgument("baseUrl", argUrl)
        graph.addArgument("taskProvider", argProvider)

        navController.graph = graph
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_puzzle -> {
                    val argRemoteProvider = NavArgument.Builder().setDefaultValue(RemoteTaskProvider(baseUrl)).build()
                    destination.addArgument("taskProvider", argRemoteProvider)
                }
            }
        }

        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.nav_puzzle, R.id.nav_info))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}