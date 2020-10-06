package cha.n.googlemap.ui

import androidx.lifecycle.*
import cha.n.googlemap.data.Result
import cha.n.googlemap.data.model.Favorites
import cha.n.googlemap.data.model.FavoritesDao
import cha.n.googlemap.data.model.keyword.Document
import cha.n.googlemap.data.model.keyword.KeywordResults
import cha.n.googlemap.data.retrofit.RetrofitService
import cha.n.googlemap.data.source.KeywordRemoteDataSourceImpl
import cha.n.googlemap.data.source.KeywordRepository
import cha.n.googlemap.data.source.KeywordRepositoryImpl
import cha.n.googlemap.data.source.favoritesLocalDataSourceImpl
import cha.n.googlemap.util.Event

class MapsViewModel internal constructor(
    private val keywordRepository: KeywordRepository
) : ViewModel() {

    private val _items = MutableLiveData<KeywordResults>()
    val items: LiveData<KeywordResults> = _items

    private val _documents = MutableLiveData<List<Document>>()
    val documents: LiveData<List<Document>> = _documents

    private val _favorites = MutableLiveData<Favorites>()
    val favorites: LiveData<Favorites> = _favorites

    private val _abc = MutableLiveData<String>()
    val abc: LiveData<String> = _abc

    private val _itemSelectedEvent = MutableLiveData<Event<Document>>()
    val itemSelctedEvent = _itemSelectedEvent

    private val _favoritesCompleteEvent = MutableLiveData<Event<String>>()
    val favoritesCompleteEvent = _favoritesCompleteEvent

    private val _inputKeywordText = MutableLiveData<String>()
    val inputKeywordText = _inputKeywordText

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_documents) {
        it.isNullOrEmpty()
    }

    fun getKeywordResults(keyword: String) {
        keywordRepository.getKeywordResults(keyword, onResponse = {
            if (it is Result.Success) {
                _items.value = it.data
                _documents.value = it.data.documents
            } else {
                _items.value = null
                _documents.value = null
            }
        })
    }

    fun saveFavorites(document: Document) {
        val favoritesId = keywordRepository.saveFavorites(converteDocumentToFavorites(document))
        _favoritesCompleteEvent.value = Event(document.id)
//        addFavorites(favoritesId)
    }

    fun onItemSelected(document: Document) {
        _itemSelectedEvent.value = Event(document)
    }

    fun setInputKeywordText(text: String) {
        _inputKeywordText.value = text
    }

    private fun converteDocumentToFavorites(document: Document) : Favorites {
        return Favorites(document.id, document.place_name, document.place_url)
    }

    fun addFavorites(id: String) {
        _favorites.value = getFavorites(id)
    }

    private fun getFavorites(id: String) : Favorites {
        return keywordRepository.getFavorites(id)
    }
}

class MapsViewModelFactory constructor(
    private val service: RetrofitService,
    private val favoritesDao: FavoritesDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(
                keywordRepository = KeywordRepositoryImpl(
                    KeywordRemoteDataSourceImpl(service),
                    favoritesLocalDataSourceImpl(favoritesDao)
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}