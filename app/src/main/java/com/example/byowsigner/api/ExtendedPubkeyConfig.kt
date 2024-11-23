package com.example.byowsigner.api

import com.example.byowsigner.ui.domain.AddressType

interface ExtendedPubkeyConfig {
    val addressType: AddressType
    val derivationPath: String
    val prefix: String
}

object SegwitExtendedPubkeyConfig : ExtendedPubkeyConfig {
    override val addressType: AddressType = AddressType.SEGWIT
    override val derivationPath: String = "84'/0'/0'/0"
    override val prefix: String = "0488B21E"
}

object SegwitChangeExtendedPubkeyConfig : ExtendedPubkeyConfig {
    override val addressType: AddressType = AddressType.SEGWIT_CHANGE
    override val derivationPath: String = "84'/0'/0'/1"
    override val prefix: String = "0488B21E"
}

object NestedSegwitExtendedPubkeyConfig : ExtendedPubkeyConfig {
    override val addressType: AddressType = AddressType.NESTED_SEGWIT
    override val derivationPath: String = "49'/0'/0'/0"
    override val prefix: String = "0488B21E"
}

object NestedSegwitChangeExtendedPubkeyConfig : ExtendedPubkeyConfig {
    override val addressType: AddressType = AddressType.NESTED_SEGWIT_CHANGE
    override val derivationPath: String = "49'/0'/0'/1"
    override val prefix: String = "0488B21E"
}

val extendedPubkeyConfigs = listOf(SegwitExtendedPubkeyConfig, SegwitChangeExtendedPubkeyConfig, NestedSegwitExtendedPubkeyConfig, NestedSegwitChangeExtendedPubkeyConfig)