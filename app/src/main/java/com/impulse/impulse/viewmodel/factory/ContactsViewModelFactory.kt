package com.impulse.impulse.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.impulse.impulse.viewmodel.ContactsViewModel
import com.impulse.impulse.viewmodel.repository.ContactRepository
import com.impulse.impulse.viewmodel.repository.MessageRepository

class ContactsViewModelFactory(
    private val repository: ContactRepository,
    private val messageRepository: MessageRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            return ContactsViewModel(repository, messageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}