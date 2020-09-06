package cha.n.googlemap.data.source

import cha.n.googlemap.data.Result
import cha.n.googlemap.data.model.keyword.KeywordResults

class KeywordRepositoryImplinternal (
    private val keywordDataSource: KeywordDataSource
) : KeywordRepository {

    override fun getKeywordResults(
        keyword: String,
        onResponse: (Result<KeywordResults>) -> Unit
    ) = keywordDataSource.getKeywordResults(keyword, onResponse)

}