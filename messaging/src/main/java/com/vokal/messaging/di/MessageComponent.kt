package com.vokal.messaging.di

import com.core.base.di.CoreComponent
import com.core.base.networking.Scheduler
import com.vokal.messaging.MessageListActivity
import com.vokal.messaging.adapter.MessageListAdapter
import com.vokal.messaging.utils.MessageHelper
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@MessageScope
@Component(dependencies = [CoreComponent::class], modules = [MessageModule::class])
interface MessageComponent {

    fun scheduler(): Scheduler

    fun inject(listActivity: MessageListActivity)
}

@Module
@MessageScope
class MessageModule {

    @Provides
    @MessageScope
    fun listAdapter(): MessageListAdapter = MessageListAdapter(mutableListOf())

    @Provides
    @MessageScope
    fun messageHelper(): MessageHelper = MessageHelper()

    /*ViewModel*/
    @Provides
    @MessageScope
    fun listViewModelFactory(messageHelper: MessageHelper,
                             compositeDisposable: CompositeDisposable,
                             scheduler: Scheduler): MessageListViewModelFactory =
            MessageListViewModelFactory(messageHelper, compositeDisposable, scheduler)


    @Provides
    @MessageScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
}