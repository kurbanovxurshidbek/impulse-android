package uz.impulse.impulse.data.remote.model

import com.google.gson.annotations.SerializedName

data class PhoneNumber(
    @SerializedName("phoneNumber")
    var phoneNumber: String
)

data class PhoneResponse(
    var message: String
)