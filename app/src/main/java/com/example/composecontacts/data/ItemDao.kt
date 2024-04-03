package com.example.composecontacts.data

import androidx.room.*
import kotlinx.coroutines.flow.*

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>
	
    // getAllTimes
    @Query("SELECT * from items ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>
}