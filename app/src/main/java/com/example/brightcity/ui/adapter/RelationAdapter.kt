package com.example.brightcity.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.brightcity.R
import com.example.brightcity.api.responses.GetRelationResponse
import com.example.brightcity.api.responses.UserListResponse
import kotlinx.android.synthetic.main.item_start_personal_profile.view.*

class RelationAdapter(private val interaction: Interaction? = null) :
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

        return RelationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_start_personal_profile,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RelationViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: ArrayList<UserListResponse>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class RelationViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: UserListResponse) = with(itemView) {

            if ((adapterPosition%2) == 0) itemView.setBackgroundColor(0)
            else itemView.setBackgroundColor(resources.getColor(R.color.audit_bg))

            img_personalF_getRel.setOnClickListener {
                interaction?.onDeleteRelationSelected(adapterPosition, item)
            }

            txt_personalF_getname.text = item.name
            txt_personalF_getRel.text = findType(item.type!!)

        }
    }

    fun findType(type: Int): String{
        return when(type){
            1 -> "فرزند"
            2 -> "پدر_مادر"
            3 -> "برادر_خواهر"
            4 -> "پدربزرگ_مادربزرگ"
            5 -> "نوه"
            else -> "دیگر"
        }
    }

    interface Interaction {
        fun onDeleteRelationSelected(position: Int, item: UserListResponse)
    }
}
