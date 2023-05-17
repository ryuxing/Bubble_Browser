package com.ryuxing.bubblebrowser.bookmark

import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ryuxing.bubblebrowser.App
import kotlinx.coroutines.flow.Flow

@Database(entities = [Bookmark::class], version = 1, exportSchema = false)
@TypeConverters(BitmapEncoder::class)
abstract class BookmarkDB :RoomDatabase(){
    companion object{
        private var database :BookmarkDB ? = null
        fun db(): BookmarkDB{
            return database ?:
                Room.databaseBuilder(
                    App.context,
                    BookmarkDB::class.java,
                    "bookmark-database"
                )
                .allowMainThreadQueries()
                .build()
        }
        fun getAll():List<Bookmark> = db().dao().getAll()
        fun getOne(id:Int):List<Bookmark> = db().dao().get(id)

    }
    abstract fun dao():BookmarkDao
}