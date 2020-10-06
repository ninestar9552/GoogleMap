package cha.n.googlemap.data.source

import cha.n.googlemap.data.Result
import cha.n.googlemap.data.model.Favorites
import cha.n.googlemap.data.model.keyword.KeywordResults

class KeywordRepositoryImpl (
    private val keywordDataSource: KeywordDataSource,
    private val favoritesDataSource: favoritesDataSource
) : KeywordRepository {

    override fun getKeywordResults(
        keyword: String,
        onResponse: (Result<KeywordResults>) -> Unit
    ) = keywordDataSource.getKeywordResults(keyword, onResponse)

    override fun getFavorites(id: String): Favorites {
        var favorites: Favorites? = null

        Thread(Runnable {
            favorites = favoritesDataSource.getFavorites(id)
        }).start()

        return favorites!!
    }

    override fun saveFavorites(favorites: Favorites): Long {
        var returnId: Long = 0

        Thread(Runnable {
            returnId = favoritesDataSource.saveFavorites(favorites)
        }).start()

        return returnId
    }

}