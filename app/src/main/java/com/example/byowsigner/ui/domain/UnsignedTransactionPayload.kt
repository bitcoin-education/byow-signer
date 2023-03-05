package com.example.byowsigner.ui.domain

data class UnsignedTransactionPayload(
    val utxos: List<UtxoDto>,
    val transaction: String
)