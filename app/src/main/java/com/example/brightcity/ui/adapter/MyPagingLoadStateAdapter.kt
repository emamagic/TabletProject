package com.example.brightcity.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brightcity.R
import kotlinx.android.synthetic.main.paging_load_state.view.*

class MyPagingLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<MyPagingLoadStateAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {


        return Holder(   LayoutInflater.from(parent.context)
            .inflate(R.layout.paging_load_state, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
              holder.itemView.btnRetryFooter.setOnClickListener {

                  retry.invoke()
              }


        holder.itemView.progressBarRetry.isVisible = loadState is LoadState.Loading
        holder.itemView.btnRetryFooter.isVisible = loadState !is LoadState.Loading
        holder.itemView.txtRetryError.isVisible = loadState !is LoadState.Loading
    }

    class Holder(itemView: View):RecyclerView.ViewHolder(itemView)


}