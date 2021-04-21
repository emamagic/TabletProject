package com.example.brightcity.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.brightcity.R
import com.example.brightcity.models.HomeSideMenuModel
import com.example.brightcity.util.Constance.DASHBOARD_FRAGMENT
import kotlinx.android.synthetic.main.item_list_content.view.*

class HomeSideMenuAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedPosition = 0

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HomeSideMenuModel>() {

        override fun areItemsTheSame(oldItem: HomeSideMenuModel, newItem: HomeSideMenuModel): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: HomeSideMenuModel, newItem: HomeSideMenuModel): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MenuViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list_content,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MenuViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<HomeSideMenuModel>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class MenuViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: HomeSideMenuModel) = with(itemView) {

            itemView.setOnClickListener {
                selectedPosition = adapterPosition
                notifyDataSetChanged()
                interaction?.onItemSelected(item.type)
            }

            itemView.apply {
                txt_item_list_content.text = item.title
                if (position == selectedPosition){
                    txt_item_list_content.setTextColor(context.resources.getColor(R.color.selected_text))
                    itemView.background = context.resources.getDrawable(R.drawable.bg_home_menu)
                    setImageItem(item ,true ,img_item_list_content)
                }else{
                    txt_item_list_content.setTextColor(context.resources.getColor(R.color.text))
                    itemView.setBackgroundResource(0)
                    setImageItem(item ,false ,img_item_list_content)
                }

            }
        }

    }

    private fun setImageItem(item: HomeSideMenuModel ,isSelected: Boolean ,img: ImageView){
        if (item.type == DASHBOARD_FRAGMENT){
            if (isSelected) img.setImageResource(R.drawable.ic_dashboard_fill)
            else img.setImageResource(R.drawable.ic_dashboard_empty)
        }else{
            if (isSelected) img.setImageResource(R.drawable.ic_counter_fill)
            else img.setImageResource(R.drawable.ic_counter_empty)
        }
    }


    interface Interaction {
        fun onItemSelected(type: Int)
    }
}
