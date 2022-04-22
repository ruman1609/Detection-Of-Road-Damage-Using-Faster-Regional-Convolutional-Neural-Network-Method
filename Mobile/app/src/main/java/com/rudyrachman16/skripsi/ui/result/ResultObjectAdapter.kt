package com.rudyrachman16.skripsi.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rudyrachman16.skripsi.R
import com.rudyrachman16.skripsi.databinding.ItemResultObjectBinding

class ResultObjectAdapter : ListAdapter<String, ResultObjectAdapter.ViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

        }
    }

    inner class ViewHolder(private val bind: ItemResultObjectBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun binding(item: String) {
            bind.indexObjectResult.text =
                itemView.context.getString(R.string.numbering, (adapterPosition + 1))
            bind.objectResult.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemResultObjectBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(getItem(position))
    }
}