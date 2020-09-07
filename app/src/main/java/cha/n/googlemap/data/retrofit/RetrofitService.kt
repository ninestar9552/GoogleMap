package cha.n.googlemap.data.retrofit

import cha.n.googlemap.data.model.address.AddressResults
import cha.n.googlemap.data.model.keyword.KeywordResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitService {

    /**
     * kakao local 주소(address) 검색
     */
    @Headers("Authorization: KakaoAK 85d86ec8c2566a81d1b93c185f897d4b")
    @GET("v2/local/search/address")
    fun getAddressSearch(
        @Query("query") keyword: String
    ): Call<AddressResults>

    /**
     * kakao local 장소(keyword) 검색
     */
    @Headers("Authorization: KakaoAK 85d86ec8c2566a81d1b93c185f897d4b")
    @GET("v2/local/search/keyword")
    fun getKeywordSearch(
        @Query("query") keyword: String
    ): Call<KeywordResults>

}