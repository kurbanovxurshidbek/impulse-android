package uz.impulse.impulse.data.remote.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import uz.impulse.impulse.data.remote.model.Illness

interface IllnessService {

    @GET("illness/{id}")
    suspend fun getIllnessById(
        @Path("id") id: Int
    ): Response<Illness>

    @GET("illness/all")
    suspend fun getAllIllness(): Response<List<Illness>>
}