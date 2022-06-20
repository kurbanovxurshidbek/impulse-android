package com.impulse.impulse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impulse.impulse.data.local.entity.Contact
import com.impulse.impulse.data.local.entity.Message
import com.impulse.impulse.utils.UIStateObject
import com.impulse.impulse.viewmodel.repository.ContactRepository
import com.impulse.impulse.viewmodel.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class ContactsViewModel(
    private val repository: ContactRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _newsState = MutableStateFlow<UIStateObject<Contact>>(UIStateObject.EMPTY)
    val newsState = _newsState

    /*  fun addContact(addContact: Contact) = viewModelScope.launch {
          _newsState.value = UIStateObject.LOADING
          try {
              val response = repository.addContact(addContact)
              _newsState.value = UIStateObject.SUCCESS(response)
          } catch (e: Exception) {
              _newsState.value = UIStateObject.ERROR(e.localizedMessage ?: "No Connection")
          }
      }*/


    fun addContact(addContact: Contact) = viewModelScope.launch {
        repository.addContact(addContact)
    }

    fun deleteContact(deleteContact: Contact) = viewModelScope.launch {
        repository.deleteContact(deleteContact)
    }

    fun saveMessage(message: Message) = viewModelScope.launch {
        messageRepository.addMessage(message)
    }

    fun deleteMessage(message: Message) = viewModelScope.launch {
        messageRepository.deleteMessage(message)
    }
}