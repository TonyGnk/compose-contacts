package com.example.composecontacts.data

import androidx.room.*

// Entity data class represents a single row in the database.
@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val number: Int,
    val price: Double,
    val quantity: Int
)
