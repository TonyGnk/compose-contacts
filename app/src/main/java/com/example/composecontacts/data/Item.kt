package com.example.composecontacts.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity data class represents a single row in the database.
@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val number: Long,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var imageData: ByteArray? = null
)
