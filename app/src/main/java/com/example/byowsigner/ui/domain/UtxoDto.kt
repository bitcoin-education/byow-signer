package com.example.byowsigner.ui.domain

import java.math.BigDecimal

data class UtxoDto(
    val derivationPath: String,
    val amount: BigDecimal,
    val addressType: AddressType
)
