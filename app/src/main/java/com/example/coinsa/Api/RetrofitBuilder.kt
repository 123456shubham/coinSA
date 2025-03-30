package com.example.coinsa.Api


import android.app.Application
import android.content.Intent
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

class RetrofitBuilder(application: Application) {
    private val retrofit: Retrofit

    init {
        val okHttpClient: OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val original = chain.request()
                    val requestBuilder: Request.Builder = original.newBuilder()
                        .addHeader("Access-Control-Allow-Origin", "*")
                        .addHeader("Access-Control-Allow-Methods", "GET,POST,PUT, OPTIONS")
//                        .addHeader("Authorization", "Bearer "+ LoginManager(application).gettoken())

                        .method(original.method, original.body)
                    val request: Request = requestBuilder.build()

                    val response: Response = chain.proceed(request)

                    if (response.code == 401) {
//                        val intent = Intent(
//                            application.applicationContext,
//                            SignupActivity::class.java
//                        ).apply {
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        }
//                        application.applicationContext.startActivity(intent)
                    }

                    // Ensure response is closed properly
//                    response.close()
                    return@Interceptor response
                })
                .addInterceptor(ChuckerInterceptor(application))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .hostnameVerifier(HostnameVerifier { hostname, session -> true })
                .build()

        val gson = GsonBuilder().setLenient().create()
        retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    val api: ApiInterface
        get() = retrofit.create(ApiInterface::class.java)

    companion object {

        const val BASEURL = "https://api.coinmarketcap.com/"
        //        const val BASEURL = "https://46ba-2405-201-4021-9153-29a0-4c69-f5de-b69e.ngrok-free.app/user/"
//        const val BASEURL = "https://803e-2405-201-4021-9153-14f7-3855-7ad6-85eb.ngrok-free.app/user/"
        @Volatile
        private var mInstance: RetrofitBuilder? = null

        @Synchronized
        fun getInstance(application: Application): RetrofitBuilder {
            return mInstance ?: synchronized(this) {
                mInstance ?: RetrofitBuilder(application).also { mInstance = it }
            }
        }
    }
}