package com.someshop.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.ktorm.database.Database
import org.ktorm.database.Transaction
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import java.util.logging.Logger

class DatabaseManager(appConfig: ApplicationConfig) {

    private var database: Database

    init {
        val dbConfig = HikariConfig().apply {
            dataSourceClassName = appConfig.property("database.dataSourceClassName").getString()
            username = appConfig.property("database.username").getString()
            password = appConfig.property("database.password").getString()
            addDataSourceProperty("databaseName", appConfig.property("database.databaseName").getString())
            addDataSourceProperty("serverName", appConfig.property("database.serverName").getString())
        }

        database = Database.connect(
            dataSource = HikariDataSource(dbConfig),
            logger = ConsoleLogger(threshold = LogLevel.WARN)
        )
    }

    //region Spot

    fun getAllSpots(): List<SpotEntity> {
        return database.sequenceOf(SpotTable)
            .toList()
    }

    fun getSpotsByUser(user: User): List<SpotEntity> {
        return database.sequenceOf(SpotTable)
            .filter { it.seller eq user.id }
            .toList()
    }

    fun getSpot(name: String): SpotEntity? {
        return database.sequenceOf(SpotTable)
            .firstOrNull { it.name like name }
    }

    fun getSpot(id: Int): SpotEntity? {
        return database.sequenceOf(SpotTable)
            .firstOrNull { it.id eq id }
    }

    fun addSpot(draft: SpotDraft, seller: User): SpotEntity? {
        val id = database.insertAndGenerateKey(SpotTable) {
            set(SpotTable.name, draft.name)
            set(SpotTable.cost, draft.cost)
            set(SpotTable.quantity, draft.quantity)
            set(SpotTable.seller, seller.id)
        } as Int

        return database.sequenceOf(SpotTable)
            .firstOrNull { it.id eq id }
    }

    fun updateSpot(id: Int, draft: SpotDraft): Boolean {
        return database.update(SpotTable) {
            set(SpotTable.name, draft.name)
            set(SpotTable.cost, draft.cost)
            set(SpotTable.quantity, draft.quantity)
            where { it.id eq id }
        } > 0
    }

    fun deleteSpot(id: Int): Boolean {
        return database.delete(SpotTable) { it.id eq id } > 0
    }

    //endregion

    //region Transaction

    fun getAllTransactions(): List<TransactionEntity> {
        return database.sequenceOf(TransactionTable)
            .toList()
    }

    fun getTransactions(customer: User): List<TransactionEntity> {
        return database.sequenceOf(TransactionTable)
            .filter { it.customer eq customer.id }
            .toList()
    }

    fun getTransactions(spot: Spot): List<TransactionEntity> {
        return database.sequenceOf(TransactionTable)
            .filter { it.spot eq spot.id }
            .toList()
    }

    fun getTransaction(spot: Spot, customer: User): TransactionEntity? {
        return database.sequenceOf(TransactionTable)
            .firstOrNull { (it.spot eq spot.id) and (it.customer eq customer.id) }
    }

    fun addTransaction(draft: TransactionDraft, customer: User): TransactionEntity? {
        database.insert(TransactionTable) {
            set(TransactionTable.customer, customer.id)
            set(TransactionTable.spot, draft.spotId)
            set(TransactionTable.quantity, draft.quantity)
        }

        return database.sequenceOf(TransactionTable)
            .firstOrNull { (it.spot eq draft.spotId) and (it.customer eq customer.id) }
    }

    fun updateTransaction(spot: Spot, customer: User, draft: TransactionDraft): Boolean {
        return database.update(TransactionTable) {
            set(TransactionTable.quantity, draft.quantity)
            where { (it.spot eq spot.id) and (it.customer eq customer.id) }
        } > 0
    }

    fun deleteTransaction(spot: Spot, customer: User): Boolean {
        return database.delete(TransactionTable) { (it.spot eq spot.id) and (it.customer eq customer.id) } > 0
    }

    //endregion

    //region User

    fun getAllUsers(): List<UserEntity> {
        return database.sequenceOf(UserTable)
            .toList()
    }

    fun getUser(id: Int): UserEntity? {
        return database.sequenceOf(UserTable)
            .firstOrNull { it.id eq id }
    }

    fun getUserByName(name: String): UserEntity? {
        return database.sequenceOf(UserTable)
            .firstOrNull { it.name eq name }
    }

    fun addUser(draft: UserDraft): UserEntity? {
        val id = database.insertAndGenerateKey(UserTable) {
            set(UserTable.name, draft.name)
            set(UserTable.password, draft.password)
        } as Int

        return database.sequenceOf(UserTable)
            .firstOrNull { it.id eq id }
    }

    fun updateUser(id: Int, draft: UserDraft): Boolean {
        return database.update(UserTable) {
            set(UserTable.name, draft.name)
            set(UserTable.password, draft.password)
            where { it.id eq id }
        } > 0
    }

    fun deleteUser(id: Int): Boolean {
        return database.delete(UserTable) { it.id eq id } > 0
    }

    //endregion
}