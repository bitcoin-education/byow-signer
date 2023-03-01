package com.example.byowsigner

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.byowsigner.database.AppDatabase
import com.example.byowsigner.database.Wallet
import com.example.byowsigner.database.WalletDao
import junit.framework.TestCase
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseTest : TestCase() {
    private lateinit var db: AppDatabase
    private lateinit var dao: WalletDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.walletDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadWallet() = runBlocking {
        val wallet1 = Wallet("test-name", "test-mnemonic-seed", Date())
        val wallet2 = Wallet("test-name 2", "test-mnemonic-seed 2", Date())

        dao.insertAll(wallet1, wallet2)

        val wallets = dao.getAll()

        wallets.take(1).toList().forEach {
            assertThat(it[0], equalTo(wallet1.copy(mnemonicSeed = null)))
            assertThat(it[1], equalTo(wallet2.copy(mnemonicSeed = null)))
        }
    }
}