package com.vokal.messaging.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.core.base.extensions.gone
import com.core.base.extensions.makeVisible
import com.core.base.utils.AppPreferences
import com.orhanobut.logger.Logger
import com.vokal.messaging.R
import com.vokal.messaging.customviews.RecyclerSectionItemDecoration
import com.vokal.messaging.data.SimpleMessage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.message_row.*


class MessageListAdapter(private val messageList: MutableList<SimpleMessage>, private val appPrefs: AppPreferences)
    : RecyclerView.Adapter<MessageListAdapter.ViewHolder>(), RecyclerSectionItemDecoration.SectionCallback {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(messageList[position], appPrefs)
    }

    override fun getItemCount(): Int = messageList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent.context)
                .inflate(R.layout.message_row, parent, false).let {
                    ViewHolder(it)
                }
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindData(simpleMessage: SimpleMessage, appPrefs: AppPreferences) {
            with(simpleMessage) {
                message_sender.text = address
                message_body.text = messageBody
                Logger.d(messageBody)
                firstChar.text = address?.take(1)
                date_tv.text = time
                messageBody?.let {
                    if (appPrefs.notificationMessageBody.contentEquals(it)) {
                        status_imv.makeVisible()
                    } else {
                        status_imv.gone()
                    }
                    appPrefs.notificationMessageBody = ""
                }
                if (time.contentEquals(itemView.context.getString(R.string.just_now))) {
                    status_imv.makeVisible()
                }
            }
        }
    }

    override fun isSection(position: Int): Boolean {
        return (position == 0 || messageList[position].time != messageList[position - 1].time)
    }

    override fun getSectionHeader(position: Int): CharSequence {
        return messageList[position].time
    }

    fun addItems(it: Set<SimpleMessage>) {
        messageList.addAll(it)
        notifyDataSetChanged()
    }

    fun clearAllItems() {
        if (messageList.isNotEmpty()) {
            messageList.clear()
            notifyDataSetChanged()
        }
    }

    fun addPaginatedData(listItems: Set<SimpleMessage>) {
        val size = listItems.size
        this.messageList.addAll(listItems)
        val sizeNew = this.messageList.size
        notifyItemRangeChanged(size, sizeNew)
    }
}