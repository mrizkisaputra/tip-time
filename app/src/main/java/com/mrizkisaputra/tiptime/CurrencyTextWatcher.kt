package com.mrizkisaputra.tiptime

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class CurrencyTextWatcher(private val textInputEditText: TextInputEditText) : TextWatcher {
    private val decimalFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
        currency = Currency.getInstance("IDR")
        maximumFractionDigits = 0
    } as DecimalFormat

    init {
        decimalFormat.isParseBigDecimal = true
    }
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        s?.let {
            var parsed = 0.0
            var formatted = ""
            if (it.toString().isNotEmpty()) {
                textInputEditText.removeTextChangedListener(this)

                val inputValue = it.toString().replace("""[Rp,.]""".toRegex(), "")

                if (inputValue.isNotBlank()) {
                    parsed = inputValue.toDouble()
                    formatted = decimalFormat.format(parsed)
                }

                textInputEditText.setText(formatted)
                textInputEditText.setSelection(formatted.length)
                textInputEditText.addTextChangedListener(this)
            }
        }
    }
}