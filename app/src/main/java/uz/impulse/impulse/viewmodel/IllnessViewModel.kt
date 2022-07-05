package uz.impulse.impulse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import uz.impulse.impulse.data.remote.model.Illness
import uz.impulse.impulse.viewmodel.repository.IllnessRepository

class IllnessViewModel(private val repository: IllnessRepository) : ViewModel() {

    val responseIllness: MutableLiveData<Response<Illness>> = MutableLiveData()
    val responseAllIllness: MutableLiveData<Response<List<Illness>>> = MutableLiveData()

    fun getIllnessById(id: Int) {
        viewModelScope.launch {
            val response = repository.getIllnessById(id)
            responseIllness.value = response
        }
    }

    fun getAllIllness() {
        viewModelScope.launch {
            val response = repository.getAllIllness()
            responseAllIllness.value = response
        }
    }
}