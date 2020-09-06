package cha.n.googlemap.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cha.n.googlemap.data.Result
import cha.n.googlemap.data.model.keyword.KeywordResults
import cha.n.googlemap.data.source.KeywordRepository

class MapsViewModel(
    private val keywordRepository: KeywordRepository
) : ViewModel() {

    private val _items = MutableLiveData<KeywordResults>()
    val items: LiveData<KeywordResults> = _items

    fun getKeywordResults(keyword: String) {
        keywordRepository.getKeywordResults(keyword, onResponse = {
            if (it is Result.Success) {
                _items.postValue(it.data)
            } else {
                _items.postValue(null)
            }
        })
    }

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.documents.isNullOrEmpty()
    }
}