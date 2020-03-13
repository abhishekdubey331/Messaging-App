package com.vokal.messaging.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.core.base.networking.Scheduler
import com.vokal.messaging.utils.MessageHelper
import com.vokal.messaging.viewmodel.MessageListViewModel
import io.reactivex.disposables.CompositeDisposable


@Suppress("UNCHECKED_CAST")
class ListViewModelFactory(private val messageHelper: MessageHelper, private val compositeDisposable: CompositeDisposable, private val scheduler: Scheduler) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MessageListViewModel(messageHelper, compositeDisposable, scheduler) as T
    }
}