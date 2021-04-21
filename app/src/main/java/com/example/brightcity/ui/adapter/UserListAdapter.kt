package com.example.brightcity.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.brightcity.R
import com.example.brightcity.api.responses.UserListResponse
import com.example.brightcity.util.Constance
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycler_search.view.*
import kotlinx.android.synthetic.main.item_search_more.view.*
import kotlinx.android.synthetic.main.item_search_more.view.image_item_search

class UserListAdapter(private val interaction: Interaction? = null, private val picasso: Picasso) :
    PagingDataAdapter<UserListResponse, UserListAdapter.DoctorListViewHolder>(DIFF_CALLBACK) {


    companion object {
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
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorListViewHolder {
        return DoctorListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_more, parent, false), interaction)
    }

    override fun onBindViewHolder(holder: DoctorListViewHolder, position: Int) {
        holder.bind(getItem(position)!! ,picasso)
    }

    class DoctorListViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: UserListResponse ,picasso: Picasso) = with(itemView) {

            if ((adapterPosition%2) == 0) itemView.setBackgroundColor(resources.getColor(R.color.audit_bg))
            else itemView.setBackgroundColor(0)

            itemView.setOnClickListener { interaction?.onItemSelected(adapterPosition, item) }
            itemView.apply {
                txt_searchF_name.text = item.name
                txt_searchF_phone.text = item.mobile
                txt_searchF_age.text = item.age.toString()
                if (item.fileId != null)
                picasso.load("${Constance.BASE_URL}file/download?id=${item.fileId}").fit().placeholder(R.drawable.ic_avatar).into(image_item_search)
                else image_item_search.setImageResource(R.drawable.ic_avatar)

            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: UserListResponse)
    }


}
