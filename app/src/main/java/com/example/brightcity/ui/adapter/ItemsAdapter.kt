package com.example.brightcity.ui.adapter

import android.os.SystemClock
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.brightcity.R
import com.example.brightcity.api.responses.ItemsListResponse
import com.example.brightcity.util.Constance
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_charge.view.*
import kotlinx.android.synthetic.main.item_recycler_audit.view.*

class ItemsAdapter(private val interaction: Interaction? = null ,private val picasso: Picasso) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsListResponse>() {

        override fun areItemsTheSame(
            oldItem: ItemsListResponse,
            newItem: ItemsListResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ItemsListResponse,
            newItem: ItemsListResponse
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return AuditViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list_charge,
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

    fun submitList(list: List<ItemsListResponse>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class AuditViewHolder
    constructor(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ItemsListResponse) = with(itemView) {

            itemView.apply {

                if (item.fileId != null)
                    picasso.load("${Constance.BASE_URL}file/download?id=${item.fileId}").fit().placeholder(R.drawable.ic_avatar).into(img_chargeF_item_list)
                txt_chargeF_item_list_Credit.text = item.title
                img_chargeF_right.setImageResource(setImage(item.status ,item.type))
                txt_chargeF_item_list_Cost.text = item.price
                img_chargeF_right.setOnClickListener { interaction?.onItemSelectedItem(adapterPosition, item) }
                if (item.count == 2) img_chargeF_item_list_Credit.visibility = View.VISIBLE
                else img_chargeF_item_list_Credit.visibility = View.GONE
                Log.e("TAG", "bind: ${item.remain}")
                if (item.remain != null){
                    txt_chargeF_item_list_time.visibility = View.VISIBLE
                    txt_chargeF_item_list_time.base = item.remain.toLong()
                    txt_chargeF_item_list_time.start()
                }else{
                  //  txt_chargeF_item_list_time.visibility = View.GONE
                    txt_chargeF_item_list_time.visibility = View.VISIBLE
                    txt_chargeF_item_list_time.base = SystemClock.elapsedRealtime() + (1* 3600000 + 2* 60000 + 4 * 1000)
                    txt_chargeF_item_list_time.start()
                }


            }


        }
    }


    fun setImage(status: Int ,type: Int): Int {
        return when (status) {
            // ثبت شده
            0 -> R.drawable.ic_remove_item
            // پرداخت شده
            1 -> R.drawable.ic_play
            // استفاده نشده
            2 -> R.drawable.ic_play
            // درحال استفاده
            3 -> {
                when(type){
                    // بن اعتباری (کد هدیه)
                   0 -> R.drawable.ic_non_pause
                   else -> R.drawable.ic_pause
                }
            }
            // 4 -> R.drawable.ic_remove_item تکمیل شده
            else -> R.drawable.ic_remove_item
        }
    }

/*    private fun countDownTimer(textView: TextView, totalTime: Long, step: Long) {
        // user lifecycle owner to react to dismiss if ui destroied
        countDown = object : CountDownTimer(totalTime, step) {
            override fun onTick(millisUntilFinished: Long) {
                textView.text = "${millisUntilFinished / 1000} ثانیه "
            }
            override fun onFinish() {
                countDown?.cancel()
            }
        }.start()
    }*/

    interface Interaction {
        fun onItemSelectedItem(position: Int, item: ItemsListResponse)
    }
}
