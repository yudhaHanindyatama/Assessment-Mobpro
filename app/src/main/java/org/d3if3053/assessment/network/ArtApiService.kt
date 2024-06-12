package org.d3if3053.assessment.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if3053.assessment.model.Art
import org.d3if3053.assessment.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://unspoken.my.id/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()
interface ArtApiService {
    @GET("api_yudha.php")
    suspend fun getArt(
        @Header("Authorization") userId: String
    ): List<Art>

    @Multipart
    @POST("api_yudha.php")
    suspend fun postArt(
        @Header("Authorization") userId: String,
        @Part("judul") judul: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("api_yudha.php")
    suspend fun delete(
        @Header("Authorization") userId: String,
        @Query("id") id: String
    ): OpStatus
}

object ArtApi {
    val service: ArtApiService by lazy {
        retrofit.create(ArtApiService::class.java)
    }

    fun getArtUrl(imageId: String): String {
        return "${BASE_URL}image.php?id=$imageId"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }