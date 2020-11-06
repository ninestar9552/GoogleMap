package cha.n.googlemap.ui

import androidx.lifecycle.*
import cha.n.googlemap.data.Result
import cha.n.googlemap.data.model.keyword.Document
import cha.n.googlemap.data.model.keyword.KeywordResults
import cha.n.googlemap.data.source.KeywordRepository
import cha.n.googlemap.util.Event

class MapsViewModel internal constructor(
    private val keywordRepository: KeywordRepository
) : ViewModel() {

    private val _keywordResults = MutableLiveData<KeywordResults>()
    val keywordResults: LiveData<KeywordResults> = _keywordResults

    private val _documents = MutableLiveData<List<Document>>()
    val documents: LiveData<List<Document>> = _documents

    private val _itemSelectedEvent = MutableLiveData<Event<Document>>()
    val itemSelctedEvent = _itemSelectedEvent

    private val _inputKeywordText = MutableLiveData<String>()
    val inputKeywordText = _inputKeywordText

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_documents) {
        it.isNullOrEmpty()
    }

    fun getKeywordResults(keyword: String) {
        keywordRepository.getKeywordResults(keyword, onResponse = {
            if (it is Result.Success) {
                _keywordResults.value = it.data
                _documents.value = it.data.documents
            } else {
                _keywordResults.value = null
                _documents.value = null
            }
        })
    }

    fun onItemSelected(document: Document) {
        _itemSelectedEvent.value = Event(document)
    }

    fun setInputKeywordText(text: String) {
        _inputKeywordText.value = text
    }
}

class MapsViewModelFactory constructor(
    private val keywordRepository: KeywordRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(
                keywordRepository = keywordRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}