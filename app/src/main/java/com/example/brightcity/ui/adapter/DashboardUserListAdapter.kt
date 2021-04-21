package com.example.brightcity.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.brightcity.R
import com.example.brightcity.api.responses.UserListResponse
import com.example.brightcity.util.Constance.BASE_URL
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycler_search.view.*

class DashboardUserListAdapter(private val interaction: Interaction? = null, private val isDynamicBackground: Boolean = false ,private val picasso: Picasso) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserListResponse>() {

        override fun areItemsTheSame(
            oldItem: UserListResponse,
            newItem: UserListResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserListResponse,
            newItem: UserListResponse
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return DashboardSearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_recycler_search,
                parent,
                false
            ),
            interaction,
            isDynamicBackground,
            picasso
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DashboardSearchViewHolder -> {
                holder.bind(differ.currentList[position])
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

    class DashboardSearchViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val isDynamicBackground: Boolean = false,
        private val picasso: Picasso
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: UserListResponse) = with(itemView) {


            if (isDynamicBackground){
                if ((adapterPosition%2) == 0) itemView.setBackgroundColor(resources.getColor(R.color.audit_bg))
                else itemView.setBackgroundColor(0)
            }
            val param = container_item_search.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(6,6,6,6)
            container_item_search.layoutParams = param //

            itemView.setOnClickListener {
                interaction?.onUserItemSelected(adapterPosition, item)
            }

            image_item_search.setImageResource(R.drawable.ic_avatar)
            txt_item_search.text = item.name
            if (item.fileId != null)
            picasso.load("${BASE_URL}file/download?id=${item.fileId}").fit().placeholder(R.drawable.ic_avatar).into(image_item_search)


        }
    }

    interface Interaction {
        fun onUserItemSelected(position: Int, item: UserListResponse)
    }
}
