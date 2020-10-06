package cha.n.googlemap.data.source

import cha.n.googlemap.data.model.Favorites
import cha.n.googlemap.data.model.FavoritesDao

class favoritesLocalDataSourceImpl internal constructor(
    private val favoritesDao: FavoritesDao
) : favoritesDataSource {

    override fun getFavorites(id: String): Favorites {
        return favoritesDao.getFavorites(id)!!
    }

    override fun saveFavorites(favorites: Favorites): Long {
        return favoritesDao.insertFavorite(favorites)
    }
}