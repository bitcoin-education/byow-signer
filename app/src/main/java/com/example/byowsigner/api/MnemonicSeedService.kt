package com.example.byowsigner.api

import io.github.bitcoineducation.bitcoinjava.MnemonicSeedGenerator

class MnemonicSeedService {
    fun generate(): String = MnemonicSeedGenerator.generateRandom(256).sentence
}