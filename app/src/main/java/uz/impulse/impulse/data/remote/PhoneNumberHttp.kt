package uz.impulse.impulse.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.impulse.impulse.data.remote.service.PhoneNumberService
import java.io.IOException
import java.util.concurrent.TimeUnit

object PhoneNumberHttp {
    private val client = getClient()
    const val IS_TESTER = true
    private const val SERVER_DEVELOPMENT = "https://impulseserverdeploy.herokuapp.com"
    private const val SERVER_PRODUCTION = "https://impulseserverdeploy.herokuapp.com"


    private val retrofit = Retrofit.Builder().baseUrl(server())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun server(): String {
        if (IS_TESTER) return SERVER_DEVELOPMENT
        return SERVER_PRODUCTION
    }

    val phoneNumberService: PhoneNumberService = retrofit.create(PhoneNumberService::class.java)

    private fun getClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            chain.proceed(builder.build())
        })
        .build()


    fun <T> createServiceWithAuth(service: Class<T>?): T {
        val newClient =
            client.newBuilder().addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request = chain.request()
                    val builder = request.newBuilder()
                    request = builder.build()
                    return chain.proceed(request)
                }
            }).build()
        val newRetrofit = retrofit.newBuilder().client(newClient).build()
        return newRetrofit.create(service)
    }
}