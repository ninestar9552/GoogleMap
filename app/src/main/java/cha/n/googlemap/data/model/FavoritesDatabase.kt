package cha.n.googlemap.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorites::class], version = 1, exportSchema = false)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun favoritesDao() : FavoritesDao

    companion object{
        private var INSTANCE : FavoritesDatabase? = null

        fun getInstance(context : Context) : FavoritesDatabase? {
            if(INSTANCE == null) {
                synchronized(FavoritesDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoritesDatabase::class.java,
                        "book.db") .build()
                }
            }

            return INSTANCE
        }
    }
}