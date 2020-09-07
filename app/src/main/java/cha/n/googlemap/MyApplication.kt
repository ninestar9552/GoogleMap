package cha.n.googlemap

import android.app.Application
import cha.n.googlemap.data.retrofit.RetrofitClient
import cha.n.googlemap.data.retrofit.RetrofitService
import cha.n.googlemap.data.source.KeywordDataSourceImpl
import cha.n.googlemap.data.source.KeywordRepository
import cha.n.googlemap.data.source.KeywordRepositoryImpl

class MyApplication : Application() {

    val taskRepository: KeywordRepository
        get() = KeywordRepositoryImpl(KeywordDataSourceImpl(service))

    val service: RetrofitService
        get() = RetrofitClient.getInstnace().create(RetrofitService::class.java)
}