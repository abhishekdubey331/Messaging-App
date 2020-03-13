package com.vokal.messaging

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.base.application.BaseActivity
import com.core.base.extensions.checkSmsReadPermission
import com.core.base.extensions.makeInVisible
import com.core.base.extensions.makeVisible
import com.core.base.extensions.orElse
import com.core.base.extensions.toast
import com.core.base.utils.PaginationScrollListener
import com.vokal.messaging.adapter.MessageListAdapter
import com.vokal.messaging.customviews.RecyclerSectionItemDecoration
import com.vokal.messaging.di.MessageDH
import com.vokal.messaging.di.MessageListViewModelFactory
import com.vokal.messaging.viewmodel.MessageListViewModel
import kotlinx.android.synthetic.main.activity_message_list.*
import javax.inject.Inject


class MessageListActivity : BaseActivity() {

    private val component by lazy { MessageDH.messageComponent() }

    @Inject
    lateinit var viewModelFactoryMessage: MessageListViewModelFactory

    @Inject
    lateinit var messageListAdapter: MessageListAdapter
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage = 0

    private val messageListViewModel: MessageListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactoryMessage).get(MessageListViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        component.inject(this)
        if (checkSmsReadPermission()) {
            messageListViewModel.loadMessages(currentPage++, applicationContext)
        } else {
            askMessagePermission()
        }
        setUpRecyclerView()

        messageListViewModel.messagesListLiveData().observe(this, Observer {
            it?.let {
                if (currentPage < 1) messageListAdapter.addPaginatedData(it) else messageListAdapter.addItems(it)
            }.orElse {
                toast(getString(R.string.no_messages_found))
            }
            progressBar.makeInVisible()
            isLoading = false
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
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewMessages.layoutManager = layoutManager
        recyclerViewMessages.adapter = messageListAdapter
        recyclerViewMessages?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                progressBar.makeVisible()
                /***
                 *   Simulate data loading delay
                 */
                Handler().postDelayed({
                    messageListViewModel.loadMessages(currentPage++, context = applicationContext)
                }, 1000)
            }
        })

    }

    override fun permissionGrantedReadSms() {
        messageListViewModel.loadMessages(currentPage++, applicationContext)
    }
}
