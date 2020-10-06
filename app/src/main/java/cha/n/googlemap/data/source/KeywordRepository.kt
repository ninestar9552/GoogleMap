package cha.n.googlemap.data.source

import cha.n.googlemap.data.Result
import cha.n.googlemap.data.model.Favorites
import cha.n.googlemap.data.model.keyword.KeywordResults

interface KeywordRepository {

    fun getKeywordResults(
        keyword: String,
        onResponse: (Result<KeywordResults>) -> Unit
    )

    fun getFavorites(id: String) : Favorites

    fun saveFavorites(favorites: Favorites) : Long

}