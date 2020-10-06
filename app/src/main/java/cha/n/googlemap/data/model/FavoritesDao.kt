package cha.n.googlemap.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorites WHERE id = :id")
    fun getFavorites(id: String): Favorites?

    @Insert
    fun insertFavorite(favorites: Favorites): Long
}