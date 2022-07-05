package uz.impulse.impulse.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.impulse.impulse.viewmodel.IllnessViewModel
import uz.impulse.impulse.viewmodel.repository.IllnessRepository

class IllnessViewModelFactory(private val repository: IllnessRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IllnessViewModel(repository) as T
    }
}