package com.vokal.messaging.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.vokal.messaging.R
import com.vokal.messaging.data.SimpleMessage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_message.*
import java.text.SimpleDateFormat
import java.util.Date


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

        fun bindData(simpleMessage: SimpleMessage) {
            with(simpleMessage) {
                message_sender.text = address
                message_body.text = messageBody
                firstChar.text = address?.take(1)
                date?.toLong()?.let {
                    time.text = TimeAgo.using(it).capitalize()
                }
                containerView.setOnClickListener { onClick(this) }
            }
        }
    }
}