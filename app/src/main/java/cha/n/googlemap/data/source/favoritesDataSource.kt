package cha.n.googlemap.data.source

import cha.n.googlemap.data.model.Favorites

interface favoritesDataSource {

    fun getFavorites(id: String) : Favorites

    fun saveFavorites(favorites: Favorites) : Long

}