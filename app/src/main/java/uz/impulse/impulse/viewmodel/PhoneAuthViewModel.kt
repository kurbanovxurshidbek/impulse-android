package uz.impulse.impulse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.FieldMap
import uz.impulse.impulse.model.PhoneResponse
import uz.impulse.impulse.viewmodel.repository.PhoneAuthRepository

class PhoneAuthViewModel(
    private val repository: PhoneAuthRepository
) : ViewModel() {

    var myResponse: MutableLiveData<Response<PhoneResponse>> = MutableLiveData()

    fun requestConfirmationCode(phoneNumber: String) =
        viewModelScope.launch {
            val response = repository.requestConfirmationCode(phoneNumber)
            myResponse.value = response
        }
}