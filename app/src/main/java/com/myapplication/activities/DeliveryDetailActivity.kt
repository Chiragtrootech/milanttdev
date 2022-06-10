package com.myapplication.activities

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.myapplication.R
import com.myapplication.databinding.ActivityDeliveryDetailBinding
import com.myapplication.databinding.ToolbarBinding
import com.myapplication.model.DeliveryItem

class DeliveryDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private val TAG = DeliveryDetailActivity::class.java.simpleName
    }

    private lateinit var activity: Activity
    private lateinit var binding: ActivityDeliveryDetailBinding
    private lateinit var toolbarBinding: ToolbarBinding

    private lateinit var mMap: GoogleMap

    private var deliveryItem: DeliveryItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this

        toolbarBinding = binding.toolbar
        setSupportActionBar(toolbarBinding.toolbar)
        supportActionBar?.let {
            it.title = getString(R.string.title_delivery_detail)
            it.setDisplayHomeAsUpEnabled(true)
        }

        if (intent != null) {
            deliveryItem = intent.getSerializableExtra("delivery") as DeliveryItem
        }

        //Delivery object set on the view before set check using the scope function
        //If deliveritem is not null then and then only it will execute the code reside in block
        deliveryItem?.let {
            binding.txtDelivery.text = it.description
            Glide.with(activity)
                .load(it.imageUrl) // image url
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.image_not_loading)// any placeholder to load at start
                .into(binding.ivDelivery)
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * used to set the marker on the map to when map is ready
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLng = LatLng(deliveryItem!!.location.lat, deliveryItem!!.location.lat)
        mMap.addMarker(
            MarkerOptions().position(latLng).title(deliveryItem!!.description)
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f))
    }

    /**
     * when press back button of the toolbar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}