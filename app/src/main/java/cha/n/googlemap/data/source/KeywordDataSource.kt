package cha.n.googlemap.data.source

import cha.n.googlemap.data.Result
import cha.n.googlemap.data.model.keyword.KeywordResults

interface KeywordDataSource {

    fun getKeywordResults(
        keyword: String,
        onResponse: (Result<KeywordResults>) -> Unit
    )

}