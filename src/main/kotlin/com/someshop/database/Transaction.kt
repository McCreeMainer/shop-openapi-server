package com.someshop.database

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

data class Transaction(
    val customerId: Int,
    val spotId: Int,
    val quantity: Int
)

data class TransactionDraft(
    val spotId: Int,
    val quantity: Int
)

interface TransactionEntity : Entity<TransactionEntity> {
    companion object : Entity.Factory<TransactionEntity>()

    val customer: UserEntity
    val spot: SpotEntity
    val quantity: Int
}

object TransactionTable : Table<TransactionEntity>("transaction") {
    val customer = int("user_id").primaryKey().references(UserTable) { it.customer }
    val spot = int("spot_id").primaryKey().references(SpotTable) { it.spot }
    val quantity = int("quantity").bindTo { it.quantity }
}
