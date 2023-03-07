package com.example.byowsigner.api

import com.example.byowsigner.BuildConfig
import com.example.byowsigner.ui.domain.Environments
import io.github.bitcoineducation.bitcoinjava.AddressConstants.*
import io.github.bitcoineducation.bitcoinjava.Script

object AddressPrefixFactory {
    private val addressMap: Map<String, Map<String, String>> = mapOf(
        Environments.MAINNET to mapOf(
            Script.P2PKH to MAINNET_P2PKH_ADDRESS_PREFIX,
            Script.P2SH to MAINNET_P2SH_ADDRESS_PREFIX,
            Script.P2WPKH to MAINNET_P2WPKH_ADDRESS_PREFIX,
        ),
        Environments.TESTNET to mapOf(
            Script.P2PKH to TESTNET_P2PKH_ADDRESS_PREFIX,
            Script.P2SH to TESTNET_P2SH_ADDRESS_PREFIX,
            Script.P2WPKH to TESTNET_P2WPKH_ADDRESS_PREFIX,
        ),
        Environments.REGTEST to mapOf(
            Script.P2PKH to TESTNET_P2PKH_ADDRESS_PREFIX,
            Script.P2SH to TESTNET_P2SH_ADDRESS_PREFIX,
            Script.P2WPKH to REGTEST_P2WPKH_ADDRESS_PREFIX,
        ),
    )

    fun getPrefix(scriptType: String) = addressMap[BuildConfig.BITCOIN_ENVIRONMENT]!![scriptType]
}