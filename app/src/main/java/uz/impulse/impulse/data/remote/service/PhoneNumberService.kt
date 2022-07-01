package uz.impulse.impulse.data.remote.service

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import uz.impulse.impulse.model.PhoneResponse

interface PhoneNumberService {

    @POST("/api/sms")
    suspend fun requestConfirmationCode(
        @Body phoneNumber: String
    ): Response<PhoneResponse>
}