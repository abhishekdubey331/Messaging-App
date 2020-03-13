package com.vokal.messaging.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.base.extensions.addTo
import com.core.base.extensions.performOnBackOutOnMain
import com.core.base.networking.Scheduler
import com.vokal.messaging.di.MessageDH
import com.vokal.messaging.data.SimpleMessage
import com.vokal.messaging.utils.MessageHelper
import io.reactivex.disposables.CompositeDisposable

class MessageListViewModel(private val messageHelper: MessageHelper,
                           private val compositeDisposable: CompositeDisposable,
                           private val scheduler: Scheduler) : ViewModel() {

    private val errorMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private val messageListLiveData: MutableLiveData<Set<SimpleMessage>> = MutableLiveData()

    fun messagesListLiveData(): LiveData<Set<SimpleMessage>> = messageListLiveData
    fun errorLiveData(): LiveData<String> = errorMutableLiveData


    fun loadMessages(pageNo: Int, context: Context) {
        messageHelper.readMessagesFromInbox(context.contentResolver, pageNo)
                .performOnBackOutOnMain(scheduler)
                .subscribe({
                    messageListLiveData.value = it
                }, { error -> handleError(error) })
                .addTo(compositeDisposable)
    }

    private fun handleError(error: Throwable?) {
        errorMutableLiveData.value = error?.localizedMessage
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        MessageDH.destroyMessageComponent()
    }
}