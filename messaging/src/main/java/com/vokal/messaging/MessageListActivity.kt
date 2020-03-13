package com.vokal.messaging

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.base.application.BaseActivity
import com.core.base.extensions.checkSmsReadPermission
import com.core.base.extensions.toast
import com.vokal.messaging.adapter.MessageListAdapter
import com.vokal.messaging.customviews.RecyclerSectionItemDecoration
import com.vokal.messaging.utils.MessageHelper
import kotlinx.android.synthetic.main.activity_message_list.*


class MessageListActivity : BaseActivity() {

    private val messageHelper = MessageHelper()
    private lateinit var mAdapter: MessageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        if (checkSmsReadPermission()) {
            messageHelper.readMessagesFromInbox(contentResolver) {
                toast(it.size.toString())
                permissionGrantedReadSms()
            }
        } else {
            askMessagePermission()
        }
    }

    override fun permissionGrantedReadSms() {
        messageHelper.readMessagesFromInbox(contentResolver) {
            mAdapter = MessageListAdapter(it) {

            }
            val sectionItemDecoration = RecyclerSectionItemDecoration(resources.getDimensionPixelSize(R.dimen.activity_side_margin),
                    true,
                    mAdapter)
            recyclerViewMessages.addItemDecoration(sectionItemDecoration)
            recyclerViewMessages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recyclerViewMessages.adapter = mAdapter
        }
    }
}
