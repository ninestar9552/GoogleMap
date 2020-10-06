package cha.n.googlemap.data.source

import cha.n.googlemap.data.Result
import cha.n.googlemap.data.model.keyword.KeywordResults
import cha.n.googlemap.data.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KeywordRemoteDataSourceImpl internal constructor(
    private val service: RetrofitService
) : KeywordDataSource {

    override fun getKeywordResults(
        keyword: String,
        onResponse: (Result<KeywordResults>) -> Unit
    ) {
        service.getKeywordSearch(keyword).enqueue(object : Callback<KeywordResults> {
            override fun onFailure(call: Call<KeywordResults>, t: Throwable) {
                onResponse(Result.Error(Exception(t.message.toString())))
            }

            override fun onResponse(
                call: Call<KeywordResults>,
                response: Response<KeywordResults>
            ) {
                if (response.isSuccessful) {
                    onResponse(Result.Success(response.body()!!))
                } else {
                    onResponse(Result.Error(Exception(response.errorBody().toString())))
                }
            }
        })
    }

}