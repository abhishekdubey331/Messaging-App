package com.vokal.messaging.data

data class SimpleMessage(val messageBody: String?,
                         val person: String?,
                         val time: String?,
                         val address: String?,
                         var expanded: Boolean = false)