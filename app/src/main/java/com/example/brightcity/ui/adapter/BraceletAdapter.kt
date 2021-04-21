package com.example.brightcity.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.brightcity.R
import com.example.brightcity.api.responses.UserListResponse
import com.example.brightcity.util.Constance
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_bracelet.view.*
import kotlinx.android.synthetic.main.item_recycler_search.view.*
import kotlinx.android.synthetic.main.item_recycler_search.view.image_item_search
import kotlinx.android.synthetic.main.item_search_more.view.*

class BraceletAdapter(private val interaction: Interaction? = null ,private val picasso: Picasso) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserListResponse>() {

        override fun areItemsTheSame(oldItem: UserListResponse, newItem: UserListResponse): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: UserListResponse, newItem: UserListResponse): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return BraceletViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_bracelet,
                parent,
                false
            ),
            interaction,
            picasso
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BraceletViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<UserListResponse>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    class BraceletViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val picasso: Picasso
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: UserListResponse) = with(itemView) {

            if ((adapterPosition%2) == 0) itemView.setBackgroundColor(resources.getColor(R.color.audit_bg))
            else itemView.setBackgroundColor(0)

            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.apply {
                txt_bracelet_item_name.text = item.name
                txt_bracelet_item_age.text = item.age.toString()
                txt_bracelet_item_phone.text = item.mobile
                if (item.fileId != null)
                picasso.load("${Constance.BASE_URL}file/download?id=${item.fileId}").fit().placeholder(R.drawable.ic_avatar).into(image_item_search)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: UserListResponse)
    }
}
