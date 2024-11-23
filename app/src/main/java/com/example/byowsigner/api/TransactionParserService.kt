package com.example.byowsigner.api

import com.example.byowsigner.ui.domain.TransactionDetailsUIState
import com.example.byowsigner.ui.domain.TransactionInputUIState
import com.example.byowsigner.ui.domain.TransactionOutputUIState
import com.example.byowsigner.ui.domain.UnsignedTransactionPayload
import com.example.byowsigner.utils.Satoshi.toBtc
import io.github.bitcoineducation.bitcoinjava.Script
import io.github.bitcoineducation.bitcoinjava.Transaction
import org.bouncycastle.util.encoders.Hex
import java.io.ByteArrayInputStream


class TransactionParserService {
    fun parse(unsignedTransactionPayload: UnsignedTransactionPayload?): TransactionDetailsUIState {
        val transaction: Transaction =
            Transaction.fromByteStream(ByteArrayInputStream(Hex.decode(unsignedTransactionPayload!!.transaction)))
        val utxos = unsignedTransactionPayload.utxos

        return TransactionDetailsUIState(
            inputs = transaction.inputs
                .mapIndexed { index, transactionInput ->
                    TransactionInputUIState(
                        transactionInput.previousTransactionId,
                        transactionInput.previousIndex,
                        utxos[index]
                    )
                },
            outputs = transaction.outputs
                .map {
                    TransactionOutputUIState(
                        address = when(it.scriptPubkey.type) {
                            Script.P2PKH -> it.scriptPubkey.p2pkhAddress(AddressPrefixFactory.getPrefix(it.scriptPubkey.type))
                            Script.P2SH -> it.scriptPubkey.nestedSegwitAddress(AddressPrefixFactory.getPrefix(it.scriptPubkey.type))
                            Script.P2WPKH -> it.scriptPubkey.p2wpkhAddress(AddressPrefixFactory.getPrefix(it.scriptPubkey.type))
                            else -> throw NotImplementedError("Script type not implemented: ${it.scriptPubkey.type}")
                        },
                        amount = toBtc(it.amount)
                    )
                }
        )
    }
}