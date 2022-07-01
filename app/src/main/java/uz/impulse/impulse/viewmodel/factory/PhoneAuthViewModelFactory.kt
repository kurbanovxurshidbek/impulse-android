package uz.impulse.impulse.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.impulse.impulse.viewmodel.PhoneAuthViewModel
import uz.impulse.impulse.viewmodel.repository.PhoneAuthRepository

class PhoneAuthViewModelFactory(
    private val repository: PhoneAuthRepository,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhoneAuthViewModel::class.java)) {
            return PhoneAuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}