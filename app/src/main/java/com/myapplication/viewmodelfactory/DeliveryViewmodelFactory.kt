package com.myapplication.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapplication.repository.DeliveryRepository
import com.myapplication.viewmodels.DeliverViewModel

/**
 * This class is give us an object of viewmodel
 */
class DeliveryViewmodelFactory(private val repository: DeliveryRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DeliverViewModel(repository) as T
    }
}