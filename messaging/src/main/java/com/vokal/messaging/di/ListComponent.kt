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
import javax.inject.Singleton

@ListScope
@Component(dependencies = [CoreComponent::class], modules = [ListModule::class])
interface ListComponent {

    fun scheduler(): Scheduler

    fun inject(listActivity: MessageListActivity)
}

@Module
@ListScope
class ListModule {

    @Provides
    @ListScope
    fun listAdapter(): MessageListAdapter = MessageListAdapter(mutableListOf())

    @Provides
    @ListScope
    fun messageHelper(): MessageHelper = MessageHelper()

    /*ViewModel*/
    @Provides
    @ListScope
    fun listViewModelFactory(messageHelper: MessageHelper,
                             compositeDisposable: CompositeDisposable,
                             scheduler: Scheduler): ListViewModelFactory =
            ListViewModelFactory(messageHelper, compositeDisposable, scheduler)


    @Provides
    @ListScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
}