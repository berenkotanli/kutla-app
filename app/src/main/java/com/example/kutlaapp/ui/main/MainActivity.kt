package com.example.kutlaapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kutlaapp.BuildConfig
import com.example.kutlaapp.R
import com.example.kutlaapp.databinding.ActivityMainBinding
import com.example.kutlaapp.services.CheckBirthdaysFunction
import com.example.kutlaapp.ui.friends.FriendsFragment
import com.example.kutlaapp.ui.main.home.HomeFragment
import com.example.kutlaapp.ui.profile.ForcedProfileActivity
import com.example.kutlaapp.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_friends -> replaceFragment(HomeFragment())
                R.id.nav_gift -> replaceFragment(FriendsFragment().apply {
                    arguments = Bundle().apply { putString("destination", "gift") }
                })
                R.id.nav_message -> replaceFragment(FriendsFragment().apply {
                    arguments = Bundle().apply { putString("destination", "message") }
                })
                R.id.nav_profile -> replaceFragment(ProfileFragment())
            }
            true
        }

    }

    override fun onStart() {
        super.onStart()
        checkUserProfile()
        val birthdayChecker = CheckBirthdaysFunction()
        birthdayChecker.checkAndSendBirthdayNotifications {
            Log.d("BirthdayCheck", "Doğum günü bildirimi kontrol edildi.")
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


    private fun checkUserProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val name = document.getString("name")
                val phone = document.getString("phone")

                if (name.isNullOrEmpty() || phone.isNullOrEmpty()) {
                    startActivity(Intent(this, ForcedProfileActivity::class.java))
                    finish()
                }
            }
    }

}