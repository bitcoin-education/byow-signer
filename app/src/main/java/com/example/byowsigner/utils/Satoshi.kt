package com.example.byowsigner.utils

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object Satoshi {
    fun toBtc(amount: BigInteger): String {
        return format(BigDecimal(amount).divide(BigDecimal.valueOf(100000000), 8, RoundingMode.UNNECESSARY))
    }

    fun toSatoshis(amount: BigDecimal): BigInteger {
        return amount.multiply(BigDecimal.valueOf(100000000)).toBigInteger()
    }

    private fun format(number: BigDecimal): String {
        val symbols = DecimalFormatSymbols(Locale.ROOT)
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','
        val formatter = DecimalFormat("#.########", symbols)
        formatter.isGroupingUsed = false
        formatter.minimumFractionDigits = 8
        formatter.maximumFractionDigits = 8
        return formatter.format(number.toDouble())
    }

}