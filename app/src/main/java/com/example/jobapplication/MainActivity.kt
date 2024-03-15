package com.example.jobapplication


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.jobapplication.databinding.ActivityMainBinding


import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView

import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    companion object {
        // you can put any unique id here, but because I am using Navigation Component I prefer to put it as
        // the fragment id.
        val HOME_ITEM = R.id.homeFragment
        val OFFERS_ITEM = R.id.applyFragment
        val MORE_ITEM = R.id.invitesFragment
        val CART_ITEM = R.id.profileFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            initNavHost()
            setUpBottomNavigation()
        }
    }

    private fun ActivityMainBinding.setUpBottomNavigation() {
        val bottomNavigationItems = mutableListOf(
            CurvedBottomNavigation.Model(HOME_ITEM, getString(R.string.home), R.drawable.home),
            CurvedBottomNavigation.Model(OFFERS_ITEM, getString(R.string.offers), R.drawable.send),
            CurvedBottomNavigation.Model(MORE_ITEM, getString(R.string.sections), R.drawable.invitation),
            CurvedBottomNavigation.Model(CART_ITEM, getString(R.string.sections), R.drawable.profile),



            )
        bottomNavigation.apply {
            bottomNavigationItems.forEach { add(it) }
            setOnClickMenuListener {
                navController.navigate(it.id)
            }
            // optional
            setupNavController(navController)
        }
    }

    private fun initNavHost() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }


    // if you need your backstack of your items always back to home please override this method
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (navController.currentDestination!!.id == HOME_ITEM)
            super.onBackPressed()
        else {
            when (navController.currentDestination!!.id) {
                OFFERS_ITEM -> {
                    navController.popBackStack(R.id.homeFragment, false)
                }
                MORE_ITEM -> {
                    navController.popBackStack(R.id.homeFragment, false)
                }
                CART_ITEM -> {
                    navController.popBackStack(R.id.homeFragment, false)
                }
                else -> {
                    navController.navigateUp()
                }
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rateUs ->{
                // Toast.makeText(this, "Rate Us", Toast.LENGTH_LONG).show()
                //startActivity(Intent(this@MainActivity, RateAppActivity::class.java))
//                val intent = Intent(this, RateUsActivity::class.java)
//                startActivity(intent)
//                overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            }
            R.id.aboutUs ->{
                // Toast.makeText(this, "About Us", Toast.LENGTH_LONG).show()
                //startActivity(Intent(this@MainActivity, AboutUsActivity::class.java))
//                val intent = Intent(this, AboutUsActivity::class.java)
//                startActivity(intent)
//                overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

                return true
            }
            R.id.share ->{
                //Toast.makeText(this, "Share", Toast.LENGTH_LONG).show()
                val shareIntent = Intent(Intent.ACTION_SEND)
//                shareIntent.type = "text/plain"
//                val appLink = "https://play.google.com/store/apps/details?id=${packageName}"
//                val shareMessage = "Check out timepass, the awesome social media app!\n$appLink"
//                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
//                startActivity(Intent.createChooser(shareIntent, "Share BuzzHub"))
                return true
            }


        }
        return false
    }




}