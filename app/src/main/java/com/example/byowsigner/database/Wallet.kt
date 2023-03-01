package com.example.byowsigner.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "wallets", indices = [Index(value = ["name"], unique = true)])
data class Wallet(
    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "mnemonic_seed")
    val mnemonicSeed: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: Date?,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}