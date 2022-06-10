package com.myapplication.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "delivery")
data class DeliveryItem(
    @PrimaryKey(autoGenerate = true)
    var deliveryId:Int,
    val id: Int,
    val description: String,
    val imageUrl: String,
    @Embedded
    val location: Location
):Serializable {
    data class Location(
        val address: String,
        val lat: Double,
        val lng: Double
    ):Serializable
}