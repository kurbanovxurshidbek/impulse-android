package uz.impulse.impulse.viewmodel.repository

import uz.impulse.impulse.data.remote.service.PhoneNumberService

class PhoneAuthRepository(
    private val phoneNumberService: PhoneNumberService
) {
    suspend fun requestConfirmationCode(phoneNumber: String) =
        phoneNumberService.requestConfirmationCode(phoneNumber)
}