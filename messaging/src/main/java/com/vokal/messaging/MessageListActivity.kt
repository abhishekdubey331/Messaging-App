package com.vokal.messaging

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.base.application.BaseActivity
import com.core.base.extensions.checkSmsReadPermission
import com.core.base.extensions.orElse
import com.core.base.extensions.toast
import com.vokal.messaging.adapter.MessageListAdapter
import com.vokal.messaging.customviews.RecyclerSectionItemDecoration
import com.vokal.messaging.di.ListViewModelFactory
import com.vokal.messaging.viewmodel.MessageListViewModel
import kotlinx.android.synthetic.main.activity_message_list.*
import javax.inject.Inject


class MessageListActivity : BaseActivity() {

    private val component by lazy { MessageDH.listComponent() }

    @Inject
    lateinit var viewModelFactory: ListViewModelFactory

    @Inject
    lateinit var messageListAdapter: MessageListAdapter

    private val messageListViewModel: MessageListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MessageListViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        component.inject(this)
        if (checkSmsReadPermission()) {
            messageListViewModel.loadMessages(1, applicationContext)
        } else {
            askMessagePermission()
        }
        setUpRecyclerView()

        messageListViewModel.messagesListLiveData().observe(this, Observer {
            it?.let {
                messageListAdapter.updateItems(it)
            }.orElse {
                toast(getString(R.string.no_messages_found))
            }
        })

        messageListViewModel.errorLiveData().observe(this, Observer {
            it?.let { toast(it) }.orElse { toast(getString(R.string.no_messages_found)) }
        })
    }

    private fun setUpRecyclerView() {
        val sectionItemDecoration = RecyclerSectionItemDecoration(resources.getDimensionPixelSize(R.dimen.activity_side_margin),
                true,
                messageListAdapter)
        recyclerViewMessages.addItemDecoration(sectionItemDecoration)
        recyclerViewMessages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewMessages.adapter = messageListAdapter
    }

    override fun permissionGrantedReadSms() {
        messageListViewModel.loadMessages(1, applicationContext)
    }
}
