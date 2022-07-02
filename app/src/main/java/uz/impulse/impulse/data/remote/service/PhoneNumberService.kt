package uz.impulse.impulse.data.remote.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import uz.impulse.impulse.data.remote.model.PhoneNumber
import uz.impulse.impulse.data.remote.model.PhoneResponse

interface PhoneNumberService {

    @POST("sms")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun requestConfirmationCode(
        @Body phoneNumber: PhoneNumber
    ): Call<PhoneResponse>
}