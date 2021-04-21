package com.example.brightcity.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.brightcity.R
import com.example.brightcity.api.responses.TransactionListResponse
import com.example.brightcity.models.SummeryCacheModel
import kotlinx.android.synthetic.main.item_cash_and_other_amounts.view.*

class CloseCashierAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SummeryCacheModel>() {

        override fun areItemsTheSame(
            oldItem: SummeryCacheModel,
            newItem: SummeryCacheModel
        ): Boolean {
            return oldItem.price == newItem.price
        }

        override fun areContentsTheSame(
            oldItem: SummeryCacheModel,
            newItem: SummeryCacheModel
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CloseCashierViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_cash_and_other_amounts,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CloseCashierViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<SummeryCacheModel>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class CloseCashierViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: SummeryCacheModel) = with(itemView) {

            if ((adapterPosition%2) == 0) itemView.setBackgroundColor(0)
            else itemView.setBackgroundColor(resources.getColor(R.color.audit_bg))

            itemView.img_item_close_cash_delete.setOnClickListener {
                interaction?.onDeleteSelected(item)
            }
            itemView.img_item_close_cash_edit.setOnClickListener {
                interaction?.onEditSelected(item)
            }
            itemView.apply {
                txt_item_close_cash_cart_name.text = item.cartName
                txt_item_close_cash_cart_type.text = typeName(item.type)
                txt_item_close_cash_description.text = item.description
                txt_item_close_cash_price.text = item.price
            }

        }
    }

    fun typeName(type: Int): String {
        return when(type){
            1 -> "نقدی"
            2 -> "کارخوان"
            3 -> "بن"
            else -> ""
        }
    }

    interface Interaction {
        fun onDeleteSelected( item: SummeryCacheModel)
        fun onEditSelected(item: SummeryCacheModel)
    }
}
