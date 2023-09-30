package com.mrizkisaputra.tiptime

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.mrizkisaputra.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

open class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var costOfService: String
    private var tipPercentage: Double = 0.0
    private var tipAmount: Double = 0.0
    private var totalAmount: Double = 0.0

    private fun onClick(view: View) {
        binding.apply {
            if (view.id == R.id.calculate_tip_button) {
                val systemService =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                systemService.apply {
                    hideSoftInputFromWindow(view.windowToken, 0)
                }
                calculateTip()
            }
        }
    }

    private fun calculateTip() {
        binding.apply {
            costOfService =
                costOfServiceTextInputEditText.text.toString().replace("""[Rp,.]""".toRegex(), "")

            onRadioButtonClicked()

            if (costOfService.isNotBlank()) {
                tipAmount = costOfService.toDouble() * tipPercentage
                if (roundTipSwitch.isChecked) {
                    tipAmount = ceil(tipAmount)
                    totalAmount = costOfService.toDouble() + tipAmount
                } else {
                    totalAmount = costOfService.toDouble() + tipAmount
                }
            } else {
                costOfServiceTextInputEditText.error = "field is not empty"

                tipAmountTextView.text = null
                totalAmountTextView.text = null
                return
            }

            display(tipAmount, totalAmount)
        }
    }

    // perlu di perbaiki
    private fun display(tipAmount: Double, totalAmount: Double) {
        val tipAmountFormatted = NumberFormat.getCurrencyInstance().format(tipAmount)
            .replace("""[a-zA-Z]""".toRegex(), "")
        val totalAmountFormatted = NumberFormat.getCurrencyInstance().format(totalAmount)
            .replace("""[a-zA-Z]""".toRegex(), "")


        binding.tipAmountTextView.text = getString(R.string.tip_amount, tipAmountFormatted)
        binding.totalAmountTextView.text = getString(R.string.total_amount, totalAmountFormatted)
    }

    private fun onRadioButtonClicked() {
        if (binding.tipOptionsRadioGroup.checkedRadioButtonId != -1) {
            when (binding.tipOptionsRadioGroup.checkedRadioButtonId) {
                R.id.tip_amazing_radioButton -> tipPercentage = 0.2
                R.id.tip_good_radioButton -> tipPercentage = 0.18
                R.id.tip_okey_radioButton -> tipPercentage = 0.15
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            costOfServiceTextInputEditText.addTextChangedListener(CurrencyTextWatcher(binding.costOfServiceTextInputEditText))
            calculateTipButton.setOnClickListener(::onClick)
        }

    }


    companion object {
//        private val TAG: String = MainActivity::class.java.simpleName
    }
}