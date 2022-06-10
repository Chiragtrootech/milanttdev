package com.myapplication.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapplication.R
import com.myapplication.databinding.DeliveryRowBinding
import com.myapplication.model.DeliveryItem
import java.util.*

/**
 * This class is used to prepare a dynamic list of Deliveries
 */
class DeliveryAdapter(
    private val activity: Activity,
    private val deliveryList: MutableList<DeliveryItem>
) :
    RecyclerView.Adapter<DeliveryAdapter.MyViewHolder>(), Filterable {

    private var filterDeliveryList:MutableList<DeliveryItem> = deliveryList
    private var onDeliveryClickListener: OnDeliveryClickListener? = null

    fun setOnDeliveryClickListener(onDeliveryClickListener: OnDeliveryClickListener) {
        this.onDeliveryClickListener = onDeliveryClickListener
    }

    inner class MyViewHolder(private val binding: DeliveryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun build(deliveryItem: DeliveryItem) {
            binding.txtDelivery.text = deliveryItem.description

            Glide.with(activity)
                .load(deliveryItem.imageUrl) // image url
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.image_not_loading)// any placeholder to load at start
                .into(binding.ivDelivery)

            binding.constaraintMain.setOnClickListener {
                onDeliveryClickListener?.onDeliveryClick(deliveryItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DeliveryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val deliveryItem = filterDeliveryList[position]
        holder.build(deliveryItem)
    }

    override fun getItemCount(): Int = filterDeliveryList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val result = FilterResults()

                val arrayListFilter = ArrayList<DeliveryItem>()

                if (constraint == null || constraint.isEmpty()) {
                    result.count = deliveryList.size
                    result.values = deliveryList
                } else {
                    for (itemModel in filterDeliveryList) {
                        if (itemModel.description.lowercase(Locale.getDefault())
                                .contains(
                                    constraint.toString().lowercase(Locale.getDefault()))
                        ) {
                            arrayListFilter.add(itemModel)
                        }
                    }
                    result.count = arrayListFilter.size
                    result.values = arrayListFilter
                }
                return result
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterDeliveryList = results!!.values as MutableList<DeliveryItem>
                notifyDataSetChanged()
            }
        }
    }

    interface OnDeliveryClickListener {
        fun onDeliveryClick(deliveryItem: DeliveryItem)
    }
}