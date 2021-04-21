package com.example.brightcity.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.brightcity.R
import com.example.brightcity.api.responses.LogListResponse
import com.example.brightcity.util.Constance
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycler_activity.view.*
import kotlinx.android.synthetic.main.item_search_more.view.*

class DashboardLogAdapter(private val interaction: Interaction? = null ,private val picasso: Picasso) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LogListResponse>() {

        override fun areItemsTheSame(
            oldItem: LogListResponse,
            newItem: LogListResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: LogListResponse,
            newItem: LogListResponse
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return DashboardActivityViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_recycler_activity,
                parent,
                false
            ),
            interaction,
            picasso
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DashboardActivityViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<LogListResponse>) {
        differ.submitList(list)
    }

    class DashboardActivityViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val picasso: Picasso
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: LogListResponse) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.apply {
                img_activity_icon.setImageResource(R.drawable.ic_purchased_credit)
                txt_activity_title.text = item.type
                txt_activity_price.text = item.value
                if (item.fileId != null)
                    picasso.load("${Constance.BASE_URL}file/download?id=${item.fileId}").fit().placeholder(R.drawable.ic_avatar).into(img_activity_icon)

            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: LogListResponse)
    }
}
