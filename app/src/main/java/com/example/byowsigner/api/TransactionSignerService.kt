package com.example.byowsigner.api

import com.example.byowsigner.ui.domain.AddressType
import com.example.byowsigner.ui.domain.UtxoDto
import com.example.byowsigner.utils.Satoshi.toSatoshis
import io.github.bitcoineducation.bitcoinjava.*
import io.github.bitcoineducation.bitcoinjava.ExtendedKeyPrefixes.MAINNET_PREFIX
import org.bouncycastle.util.encoders.Hex
import java.io.ByteArrayInputStream
import java.util.stream.IntStream


class TransactionSignerService {
    fun signTransaction(
        transactionString: String,
        mnemonicSeedString: String,
        password: String,
        utxoDtos: List<UtxoDto>
    ): String {
        val transaction: Transaction = Transaction.fromByteStream(ByteArrayInputStream(Hex.decode(transactionString)))
        IntStream.range(0, utxoDtos.size)
            .forEachOrdered { i ->
                val utxoDto = utxoDtos[i]

                val mnemonicSeed = MnemonicSeed(mnemonicSeedString)
                val masterKey = mnemonicSeed.toMasterKey(password, MAINNET_PREFIX.privatePrefix)
                val extendedPrivateKey = masterKey.ckd(
                    utxoDto.derivationPath,
                    true,
                    MAINNET_PREFIX.privatePrefix
                ) as ExtendedPrivateKey
                val privateKey = extendedPrivateKey.toPrivateKey()

                when(utxoDto.addressType) {
                    AddressType.SEGWIT, AddressType.SEGWIT_CHANGE -> TransactionECDSASigner.sign(transaction, privateKey, i, toSatoshis(utxoDto.amount), true)
                    AddressType.NESTED_SEGWIT, AddressType.NESTED_SEGWIT_CHANGE -> {
                        val redeemScript = Script.p2wpkhScript(Hash160.hashToHex(privateKey.publicKey.compressedPublicKey))
                        P2SHTransactionECDSASigner.signNestedSegwit(transaction, privateKey, i, redeemScript, toSatoshis(utxoDto.amount))
                    }
                    else -> throw NotImplementedError("Address type not implemented: ${utxoDto.addressType}")
                }
            }
        return transaction.serialize()
    }

}