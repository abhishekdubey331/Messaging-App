package com.vokal.messaging.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.vokal.messaging.R
import com.vokal.messaging.customviews.RecyclerSectionItemDecoration
import com.vokal.messaging.data.SimpleMessage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.message_row.*


class MessageListAdapter(private val messageList: MutableList<SimpleMessage>,
                         private val onClick: (SimpleMessage) -> Unit)
    : RecyclerView.Adapter<MessageListAdapter.ViewHolder>(), RecyclerSectionItemDecoration.SectionCallback {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(messageList[position])
    }

    override fun getItemCount(): Int = messageList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent.context)
                .inflate(R.layout.message_row, parent, false).let {
                    ViewHolder(it, onClick)
                }
    }

    class ViewHolder(override val containerView: View, private val onClick: (SimpleMessage) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindData(simpleMessage: SimpleMessage) {
            with(simpleMessage) {
                message_sender.text = address
                message_body.text = messageBody
                firstChar.text = address?.take(1)
                date_tv.text = time
                containerView.setOnClickListener { onClick(this) }
            }
        }
    }

    override fun isSection(position: Int): Boolean {
        return (position == 0 || messageList[position].time != messageList[position - 1].time)

    }

    override fun getSectionHeader(position: Int): CharSequence {
        return messageList[position].time.toString()
    }
}