package com.example.hello_world_test_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    // Declare FirebaseAnalytics Object
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialized an instance of FirebaseAnalytics
        firebaseAnalytics = Firebase.analytics

        // Initialize a variable for the button
        val btnPurchase = findViewById<Button>(R.id.btnPurchase)

        // Create a Bundle to represent an item
        val itemJeggings = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_123")
            putString(FirebaseAnalytics.Param.ITEM_NAME, "jeggings")
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "pants")
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, "black")
            putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
            putDouble(FirebaseAnalytics.Param.PRICE, 9.99)
        }

        // Fire a Firebase Analytics Event based on a click listener
        btnPurchase.setOnClickListener {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, itemJeggings)
        }

    }
}
