package com.myapplication.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.myapplication.model.DeliveryItem

/**
 * This is an interface where you can place your database operations function
 */
@Dao
interface DeliveryDAO {

    /**
     * Insert deliveries
     * @param List of delivery items
     *
     */
    @Insert
    suspend fun addDelivery(deliveryItemList: List<DeliveryItem>)

    /**
     * used to check the table has records or not
     */
    @Query("SELECT EXISTS(SELECT * FROM delivery)")
    fun isExists(): Boolean

    /**
     * Get the list of deliveries from the database
     */
    @Query("SELECT * FROM delivery")
    suspend fun getDeliveries(): MutableList<DeliveryItem>
}