package com.example.hello_world_test_1

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.appsflyer.AppsFlyerLib
import android.util.Log
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.appsflyer.AFInAppEventType // Predefined event names
import com.appsflyer.AFInAppEventParameterName // Predefined parameter names

class MainActivity : AppCompatActivity() {
    // Declare FirebaseAnalytics Object
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    // AppsFlyer Developer Key
    private val afDevKey = "oDEi68sJYE8YAaPCzTbZGm"

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

        // Initialize AppsFlyer SDK
        AppsFlyerLib.getInstance().init(afDevKey, null, this)

        // Start AppsFlyer AND SDK
        AppsFlyerLib.getInstance().start(this)

        // Enable AppsFlyer Debug Logs - This should be REMOVED for Production
        AppsFlyerLib.getInstance().setDebugLog(true)

        // Fire a Firebase Analytics Event based on a click listener
        btnPurchase.setOnClickListener {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, itemJeggings)

            val eventValues = HashMap<String, Any>()
            eventValues.put(AFInAppEventParameterName.PRICE, 123.45)
            eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, "hello world")
            eventValues.put(AFInAppEventParameterName.CONTENT_ID, "133T")

            AppsFlyerLib.getInstance().logEvent(
                applicationContext,
                AFInAppEventType.PURCHASE,
                eventValues,
                object : AppsFlyerRequestListener {
                    override fun onSuccess() {
                        Log.d("AppsFlyer", "Event sent successfully")
                    }
                    override fun onError(errorCode: Int, errorDesc: String) {
                        Log.d("AppsFlyer", "Event failed to be sent:\n" +
                                "Error code: " + errorCode + "\n"
                                + "Error description: " + errorDesc)
                    }
                })
        }
    }
}
