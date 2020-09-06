package cha.n.googlemap.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cha.n.googlemap.data.Result
import cha.n.googlemap.data.model.keyword.Document
import cha.n.googlemap.data.model.keyword.KeywordResults
import cha.n.googlemap.data.source.KeywordRepository

class MapsViewModel(
    private val keywordRepository: KeywordRepository
) : ViewModel() {

    private val _items = MutableLiveData<KeywordResults>()
    val items: LiveData<KeywordResults>
            get() = _items

    private val _documents = MutableLiveData<List<Document>>()
    val documents: LiveData<List<Document>>
        get() = _documents

    fun getKeywordResults(keyword: String) {
        keywordRepository.getKeywordResults(keyword, onResponse = {
            if (it is Result.Success) {
                _items.postValue(it.data)
                _documents.postValue(it.data.documents)
            } else {
                _items.postValue(null)
                _documents.postValue(null)
            }
        })
    }

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_documents) {
        it.isNullOrEmpty()
    }
}