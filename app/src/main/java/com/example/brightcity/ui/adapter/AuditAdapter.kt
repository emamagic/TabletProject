package com.example.brightcity.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.brightcity.R
import com.example.brightcity.api.responses.TransactionListResponse
import kotlinx.android.synthetic.main.item_recycler_audit.view.*

class AuditAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransactionListResponse>() {

        override fun areItemsTheSame(oldItem: TransactionListResponse, newItem: TransactionListResponse): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: TransactionListResponse, newItem: TransactionListResponse): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return AuditViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_recycler_audit,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AuditViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<TransactionListResponse>) {
        differ.submitList(list)
    }

    class AuditViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: TransactionListResponse) = with(itemView) {

            if ((adapterPosition%2) == 0) itemView.setBackgroundColor(resources.getColor(R.color.audit_bg))
            else itemView.setBackgroundColor(0)

            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.apply {
                txt_item_audit_date.text = item.date
                txt_item_audit_bon.text = item.bon.toString()
                txt_item_audit_bracelet.text = ""
                txt_item_audit_cache.text = item.cash.toString()
                txt_item_audit_discount.text = item.off.toString()
                txt_item_audit_name.text = item.title
                txt_item_audit_payment.text = item.price.toString()
                txt_item_audit_reward.text = item.award.toString()
                txt_item_audit_cart.text = item.cart
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: TransactionListResponse)
    }
}
