package com.vokal.messaging


import com.core.base.application.CoreApp
import com.vokal.messaging.di.DaggerListComponent
import com.vokal.messaging.di.ListComponent
import javax.inject.Singleton

@Singleton
object MessageDH {
    private var listComponent: ListComponent? = null


    fun listComponent(): ListComponent {
        if (listComponent == null)
            listComponent = DaggerListComponent.builder().coreComponent(CoreApp.coreComponent).build()
        return listComponent as ListComponent
    }

    fun destroyListComponent() {
        listComponent = null
    }
}