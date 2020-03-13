package com.vokal.messaging.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.core.base.extensions.toast
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
                date?.toLong()?.let {
                    time.text = TimeAgo.using(it).capitalize()
                }
                containerView.setOnClickListener { onClick(this) }
            }
        }
    }

    override fun isSection(position: Int): Boolean {
        return (position == 0 ||
                TimeAgo.using(messageList[position].date!!.toLong())
                        .capitalize() != TimeAgo
                .using(messageList[position - 1].date!!.toLong()).capitalize())

    }

    override fun getSectionHeader(position: Int): CharSequence {
        return TimeAgo.using(messageList[position].date!!.toLong()).capitalize()
    }
}