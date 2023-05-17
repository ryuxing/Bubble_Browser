package com.ryuxing.bubblebrowser.bookmark

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(bookmark: Bookmark)

    @Update
    suspend fun update(bookmark: Bookmark)

    @Delete
    suspend fun delete(bookmark: Bookmark)
    @Query("Delete FROM Bookmark")
    suspend fun deleteAll()

    @Query("SELECT * FROM Bookmark")
    fun getAll():List<Bookmark>

    @Query("SELECT * FROM Bookmark")
    fun getAllVM(): Flow<List<Bookmark>>

    @Query("SELECT * FROM Bookmark WHERE id=:id")
    fun get(id:Int):List<Bookmark>
}