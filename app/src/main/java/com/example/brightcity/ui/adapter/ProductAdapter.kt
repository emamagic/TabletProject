package com.example.brightcity.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.brightcity.R
import com.example.brightcity.api.responses.ProductListResponse
import com.example.brightcity.api.responses.TransactionListResponse
import com.example.brightcity.util.Constance
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_charge_right.view.*
import kotlinx.android.synthetic.main.item_recycler_audit.view.*

class ProductAdapter(private val interaction: Interaction? = null ,private val picasso: Picasso) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductListResponse>() {

        override fun areItemsTheSame(oldItem: ProductListResponse, newItem: ProductListResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductListResponse, newItem: ProductListResponse): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return AuditViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list_charge_right,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AuditViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ProductListResponse>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class AuditViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ProductListResponse) = with(itemView) {

            itemView.setOnClickListener {
                interaction?.onItemSelectedProduct(adapterPosition, item)
            }
            itemView.apply {

                if (item.id != -2L){
                    relative_more.visibility = View.GONE
                    relative_item.visibility = View.VISIBLE
//                    if (item.fileId != null)
//                        picasso.load("${Constance.BASE_URL}file/download?id=${item.fileId}").fit().into(img_chargeF_time)

                    txt_chargeF_cost.text = item.price
                    txt_chargeF_number.text = "${adapterPosition+1}"
                }else{
                    relative_more.visibility = View.VISIBLE
                    relative_item.visibility = View.GONE
                }


            }


        }
    }

    interface Interaction {
        fun onItemSelectedProduct(position: Int, item: ProductListResponse)
    }
}
