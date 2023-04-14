package com.example.hello_world_test

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity.apply
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat.apply
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"
private const val INITIAL_TAX_PERCENT = 8

class MainActivity : AppCompatActivity() {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTax: SeekBar
    private lateinit var tvTaxPercentLabel: TextView
    private lateinit var tvTaxAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTaxDescription: TextView
    private lateinit var btnPurchase: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTax = findViewById(R.id.seekBarTax)
        tvTaxPercentLabel = findViewById(R.id.tvTaxPercentLabel)
        tvTaxAmount = findViewById(R.id.tvTaxAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTaxDescription = findViewById(R.id.tvTaxDescription)
        btnPurchase = findViewById(R.id.btnPurchase)

        seekBarTax.progress = INITIAL_TAX_PERCENT
        tvTaxPercentLabel.text = "$INITIAL_TAX_PERCENT%"
        updateTaxDescription(INITIAL_TAX_PERCENT)

        seekBarTax.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged: $progress")
                tvTaxPercentLabel.text = "$progress%"
                computeTaxAndTotal()
                updateTaxDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged: $s")
                computeTaxAndTotal()
            }

        })

        btnPurchase.setOnClickListener {
            println("Buy Now!")
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "123")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "Test Item")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
            }

            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
    }

    private fun updateTaxDescription(taxPercent: Int) {
        val taxDescription = when (taxPercent) {
            in 0..9 -> "Amazing"
            in 10..19 -> "Great"
            in 20..29 -> "Good"
            in 30..39 -> "Acceptable"
            else -> "Poor"
        }
        tvTaxDescription.text = taxDescription
        // Update color based on taxPercent
        val color = ArgbEvaluator().evaluate(
            taxPercent.toFloat() / seekBarTax.max,
            ContextCompat.getColor(this, R.color.color_best_tax),
            ContextCompat.getColor(this, R.color.color_worst_tax)
        ) as Int
        tvTaxDescription.setTextColor(color)
    }

    private fun computeTaxAndTotal() {
        if (etBaseAmount.text.isEmpty()) {
            tvTaxAmount.text = ""
            tvTotalAmount.text = ""
            return
            }

        // 1. Get value of base and tax percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val taxPercent = seekBarTax.progress

        // 2. Compute tax and total
        val taxAmount = baseAmount * taxPercent / 100
        val taxAmountRounded = String.format("%.2f", taxAmount)
        val totalAmount = baseAmount + taxAmount
        val totalAmountRounded = String.format("%.2f", totalAmount)

        // 3. Update UI
        tvTaxAmount.text = taxAmountRounded
        tvTotalAmount.text = totalAmountRounded
    }
}