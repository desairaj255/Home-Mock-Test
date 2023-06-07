package com.home.mock.test.ui

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.home.mock.test.R
import com.home.mock.test.databinding.ActivityMainBinding
import com.home.mock.test.utils.extensions.getLong
import com.home.mock.test.utils.extensions.gone
import com.home.mock.test.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Declare lateinit variables
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    // Define the set of top-level destinations
    private val topLevelDestinations = setOf(
        R.id.usersFragment,
        R.id.favoriteUsersFragment,
        R.id.toDoFragment,
        R.id.profileFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup the navigation controller
        setupNavController()

        // Setup the bottom navigation
        setupBottomNavigation()
    }

    private fun setupNavController() {
        // Find the NavHostFragment and get its NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupBottomNavigation() = binding.bottomNavigation.run bottomNav@{
        // Setup the BottomNavigationView with the NavController
        setupWithNavController(navController)

        // Hide BottomNavigationView in screens that don't require it
        lifecycleScope.launchWhenResumed {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    in topLevelDestinations -> this@bottomNav
                        .animate()
                        .setInterpolator(LinearOutSlowInInterpolator())
                        .alpha(1.0f).withStartAction { visible() }
                        .duration = resources.getLong(R.integer.bottom_navigation_fade_in)
                    else -> this@bottomNav
                        .animate()
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .alpha(0.0f)
                        .withEndAction { gone() }
                        .duration = resources.getLong(R.integer.bottom_navigation_fade_out)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()
}
