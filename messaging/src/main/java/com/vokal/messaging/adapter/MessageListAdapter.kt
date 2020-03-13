package com.vokal.messaging.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vokal.messaging.R
import com.vokal.messaging.data.SimpleMessage
import kotlinx.android.extensions.LayoutContainer


class MessageListAdapter(private val messageList: MutableList<SimpleMessage>,
                         private val onClick: (SimpleMessage) -> Unit)
    : RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(messageList[position])
    }

    override fun getItemCount(): Int = messageList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message, parent, false).let {
                    ViewHolder(it, onClick)
                }
    }

    class ViewHolder(override val containerView: View, private val onClick: (SimpleMessage) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindData(repository: SimpleMessage) {
            with(repository) {

            }
        }
    }


    fun addMessages(newRepositories: List<SimpleMessage>) {
        messageList.addAll(newRepositories)
    }
}