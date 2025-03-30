package com.example.coinsa.repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.coinsa.Api.ApiInterface
import com.example.coinsa.model.MarketModel
import com.example.coinsa.sealed.ApiResponse
import com.example.coinsa.util.MyError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MarketRepo(val apiInterface: ApiInterface, val context: Context)  {

    private val marketLiveData = MutableLiveData<ApiResponse<MarketModel>>()
    val marketData : MutableLiveData<ApiResponse<MarketModel>>
        get() = marketLiveData

    suspend fun marketApi() {
        marketLiveData.postValue(ApiResponse.Loading())
        try {
            val response = withContext(Dispatchers.IO) {
                apiInterface.getCryptoCurrencies()
            }
            if (response.isSuccessful && response.body() != null) {
                marketLiveData.postValue(ApiResponse.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    val errorResponse: MyError = Gson().fromJson(
                        errorBody, object : TypeToken<MyError>() {}.type
                    )
                    marketLiveData.postValue(ApiResponse.Error(errorResponse.message))
                } else {
                    marketLiveData.postValue(
                        ApiResponse.Error(
                            response.message() ?: "Something went wrong"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            marketLiveData.postValue(ApiResponse.Error(e.message ?: "Unknown error"))
        }
    }




}