package com.myapplication.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapplication.R
import com.myapplication.adapter.DeliveryAdapter
import com.myapplication.databinding.ActivityMainBinding
import com.myapplication.databinding.ToolbarBinding
import com.myapplication.db.DeliveryDatabase
import com.myapplication.model.DeliveryItem
import com.myapplication.repository.DeliveryRepository
import com.myapplication.utils.Status
import com.myapplication.viewmodelfactory.DeliveryViewmodelFactory
import com.myapplication.viewmodels.DeliverViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var activity: Activity
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbarBinding: ToolbarBinding
    private lateinit var deliverViewModel: DeliverViewModel

    private var deliveryAdapter: DeliveryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this

        toolbarBinding = binding.toolbar
        setSupportActionBar(toolbarBinding.toolbar)
        supportActionBar?.let {
            it.title = getString(R.string.title_things)
        }

        val database = DeliveryDatabase.getDatabase(activity)
        val repository = DeliveryRepository(activity, "delivery.json", database)
        deliverViewModel = ViewModelProvider(
            this,
            DeliveryViewmodelFactory(repository)
        )[DeliverViewModel::class.java]

        /**
         * Observing the data changes from the viewmodel once the data change in livedata it will appear on recyclerview.
         */
        deliverViewModel.deliveryList.observe(this) { it ->
            when (it.status) {
                Status.LOADING -> {
                    //PRE LOADING FOR THE ASYNC TASK
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    //IF FILE READ SUCCESSFULLY
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerDelivery.layoutManager = LinearLayoutManager(activity)
                    binding.recyclerDelivery.itemAnimator = DefaultItemAnimator()
                    it.data?.let { deliveryList ->
                        deliveryAdapter = DeliveryAdapter(activity, deliveryList)
                        binding.recyclerDelivery.adapter = deliveryAdapter

                        deliveryAdapter!!.setOnDeliveryClickListener(object :
                            DeliveryAdapter.OnDeliveryClickListener {
                            override fun onDeliveryClick(deliveryItem: DeliveryItem) {
                                Intent(activity, DeliveryDetailActivity::class.java).also {
                                    it.putExtra("delivery", deliveryItem)
                                    startActivity(it)
                                }
                            }
                        })
                    }
                }
                Status.ERROR -> {
                    //IF ANY EXCEPTION OCCURS IT WILL SHOW HERE
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                deliveryAdapter?.filter?.filter(p0?.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
}