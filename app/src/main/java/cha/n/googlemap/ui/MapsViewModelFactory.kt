package cha.n.googlemap.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cha.n.googlemap.data.source.KeywordDataSourceImpl
import cha.n.googlemap.data.source.KeywordRepositoryImplinternal
import cha.n.googlemap.util.getRetrofitService

class MapsViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(
                keywordRepository = KeywordRepositoryImplinternal(
                    keywordDataSource = KeywordDataSourceImpl(
                        getRetrofitService()
                    )
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}