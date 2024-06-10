package com.example.test

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.test.service.createChannel
import com.example.test.service.requestNotifPermission
import com.example.test.service.showNotification
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        navHostFragment = supportFragmentManager.findFragmentById(R.id.main) as NavHostFragment
        navController = navHostFragment.navController

        requestNotifPermission(this)
        createChannel(this)
        // Sample to show notification
        showNotification(this, "App Book", "Hello World!")

        lifecycleScope.launch {
            // Get firebase messaging token
            Log.d("MainActivity", "My token: ${FirebaseMessaging.getInstance().token.await()}")
        }
    }
}