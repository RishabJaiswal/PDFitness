package com.pune.dance.fitness.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pune.dance.fitness.R
import com.pune.dance.fitness.ui.home.models.PaymentItem
import kotlinx.android.synthetic.main.item_payment_home.view.*
import java.text.SimpleDateFormat

class PaymentsAdapter(private val interaction: Interaction? = null) :
    ListAdapter<PaymentItem, PaymentsAdapter.PaymentViewHolder>(PaymentItemDiffCallback()) {

    private val sdf = SimpleDateFormat("MMM YYYY")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PaymentViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_payment_home, parent, false), interaction
    )

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) = holder.bind(getItem(position))

    fun update(data: List<PaymentItem>) {
        submitList(data.toMutableList())
    }

    inner class PaymentViewHolder(itemView: View, private val interaction: Interaction?) :
        RecyclerView.ViewHolder(itemView), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
            val clicked = getItem(adapterPosition)
        }

        fun bind(item: PaymentItem) = with(itemView) {
            tv_month_year.text = sdf.format(item.date)
            tv_month_payment.text = "₹ 1800 Pending"
        }
    }

    interface Interaction {}

    private class PaymentItemDiffCallback : DiffUtil.ItemCallback<PaymentItem>() {

        override fun areItemsTheSame(oldItem: PaymentItem, newItem: PaymentItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PaymentItem, newItem: PaymentItem): Boolean {
            return oldItem == newItem
        }
    }
}