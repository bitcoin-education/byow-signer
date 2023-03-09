package com.example.byowsigner.api

import com.example.byowsigner.ui.domain.ExtendedPubkey
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.bitcoineducation.bitcoinjava.ExtendedKeyPrefixes.MAINNET_PREFIX
import io.github.bitcoineducation.bitcoinjava.MnemonicSeed


class ExtendedPubkeyService {

    fun create(mnemonicSeedString: String, password: String): String {
        val mnemonicSeed = MnemonicSeed(mnemonicSeedString)
        val masterKey = mnemonicSeed.toMasterKey(password, MAINNET_PREFIX.privatePrefix)
        return extendedPubkeyConfigs.map {
            ExtendedPubkey(
                masterKey.ckd(it.derivationPath, false, it.prefix).serialize(),
                it.addressType.name
            )
        }.associate {
            it.type to it.key
        }.let {
            jacksonObjectMapper().writeValueAsString(it)
        }
    }
}