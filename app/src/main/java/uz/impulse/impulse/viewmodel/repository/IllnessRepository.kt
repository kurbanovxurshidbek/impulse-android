package uz.impulse.impulse.viewmodel.repository

import retrofit2.Response
import uz.impulse.impulse.data.remote.model.Illness
import uz.impulse.impulse.data.remote.services.IllnessService

class IllnessRepository(private val service: IllnessService) {

    suspend fun getIllnessById(id: Int): Response<Illness> {
        return service.getIllnessById(id)
    }

    suspend fun getAllIllness(): Response<List<Illness>> {
        return service.getAllIllness()
    }
}