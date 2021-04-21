package com.example.brightcity.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.brightcity.R
import com.example.brightcity.api.responses.LastMessageResponse
import kotlinx.android.synthetic.main.item_last_messages.view.*

class DashboardMessageAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LastMessageResponse>() {

        override fun areItemsTheSame(
            oldItem: LastMessageResponse,
            newItem: LastMessageResponse
        ): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(
            oldItem: LastMessageResponse,
            newItem: LastMessageResponse
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return LastMessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_last_messages,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LastMessageViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<LastMessageResponse>) {
        differ.submitList(list)
    }

    class LastMessageViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: LastMessageResponse) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.apply {
                last_sender.text = item.username
                last_message.text = item.text
                last_date.text = item.updatedAt
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: LastMessageResponse)
    }
}
