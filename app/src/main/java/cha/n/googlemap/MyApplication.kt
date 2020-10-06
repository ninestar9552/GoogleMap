package cha.n.googlemap

import android.app.Application
import cha.n.googlemap.data.retrofit.RetrofitClient
import cha.n.googlemap.data.retrofit.RetrofitService

class MyApplication : Application() {

    val service: RetrofitService
        get() = RetrofitClient.getInstnace().create(RetrofitService::class.java)
}