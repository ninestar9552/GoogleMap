package cha.n.googlemap.retrofit

import cha.n.googlemap.data.address.AddressResults
import cha.n.googlemap.data.keyword.KeywordResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitService {

    @Headers("Authorization: KakaoAK 85d86ec8c2566a81d1b93c185f897d4b")
    @GET("v2/local/search/address")
    fun getAddressSearch(
        @Query("query") keyword: String
    ): Call<AddressResults>

    @Headers("Authorization: KakaoAK 85d86ec8c2566a81d1b93c185f897d4b")
    @GET("v2/local/search/keyword")
    fun getKeywordSearch(
        @Query("query") keyword: String
    ): Call<KeywordResults>

}