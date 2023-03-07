package com.example.byowsigner.ui.domain

import java.math.BigInteger

data class TransactionInputUIState(
    val txId: String = "",
    val index: BigInteger = BigInteger.ONE.negate(),
    val utxoDto: UtxoDto = UtxoDto()
)
