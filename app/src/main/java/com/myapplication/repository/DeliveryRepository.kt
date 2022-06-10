package com.myapplication.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myapplication.db.DeliveryDatabase
import com.myapplication.model.DeliveryItem
import com.myapplication.utils.Resource
import java.io.InputStream

/**
 * Repository class is used to communicate with data sources it can be any webAPI, file or any database
 */
class DeliveryRepository(
    private val activity: Activity,
    private val fileName: String,
    private val deliveryDatabase: DeliveryDatabase
) {

    //Create a live data to hold the data of type Resource which gives us a possible result
    private val deliveryLiveData = MutableLiveData<Resource<MutableList<DeliveryItem>>>()

    val deliveryList: LiveData<Resource<MutableList<DeliveryItem>>> get() = deliveryLiveData

    /**
     *  Used to Read JSON file from the assets because API is not working which i have received.
     *  Make is suspend function to work with coroutine and execute other task parallaly if we have
     */
    suspend fun readFile() {
        try {
            val inputStream: InputStream = activity.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)

            //Check the condition where delivery table has rows or not if it has not then it's insert due to
            // avoid the duplication of the records
            if (!deliveryDatabase.deliveryDao().isExists()) {
                val deliveryItem: MutableList<DeliveryItem> = Gson().fromJson(
                    String(buffer),
                    object : TypeToken<MutableList<DeliveryItem>>() {}.type
                )
                deliveryDatabase.deliveryDao().addDelivery(deliveryItem)
            } else {
                deliveryDatabase.clearAllTables()
                val deliveryItem: MutableList<DeliveryItem> = Gson().fromJson(
                    String(buffer),
                    object : TypeToken<MutableList<DeliveryItem>>() {}.type
                )
                deliveryDatabase.deliveryDao().addDelivery(deliveryItem)
            }
            deliveryLiveData.postValue(
                Resource.Success(
                    deliveryDatabase.deliveryDao().getDeliveries()
                )
            )
        } catch (e: Exception) {
            deliveryLiveData.postValue(Resource.Error(e.message.toString()))
        }
    }
}