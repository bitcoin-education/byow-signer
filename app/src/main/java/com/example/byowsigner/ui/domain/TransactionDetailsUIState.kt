package com.example.byowsigner.ui.domain

data class TransactionDetailsUIState(
    val inputs: List<TransactionInputUIState> = emptyList(),
    val outputs: List<TransactionOutputUIState> = emptyList(),
)
