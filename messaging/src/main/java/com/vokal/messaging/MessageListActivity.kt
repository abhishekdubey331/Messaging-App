package com.vokal.messaging

import android.net.Uri
import android.os.Bundle
import com.core.base.application.BaseActivity
import com.core.base.extensions.checkSmsReadPermission
import com.core.base.extensions.toast
import com.vokal.messaging.data.SimpleMessage

class MessageListActivity : BaseActivity() {


    private val smsList: MutableList<SimpleMessage> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        if (checkSmsReadPermission()) {
            readMessagesFromInbox()
        } else {
            askMessagePermission()
        }
    }

    override fun permissionGrantedReadSms() {
        readMessagesFromInbox()
        toast(smsList.size.toString())
    }


    private fun readMessagesFromInbox() {
        val smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)
        smsInboxCursor?.let {
            val messageBody = smsInboxCursor.getColumnIndex("body")
            val address = smsInboxCursor.getColumnIndex("address")
            val date = smsInboxCursor.getColumnIndex("date")
            val person = smsInboxCursor.getColumnIndex("person")
            if (messageBody < 0 || !smsInboxCursor.moveToFirst()) return
            do {
                var str = "SMS from : " + smsInboxCursor.getString(address) + "\n"
                str += smsInboxCursor.getString(messageBody)
                if (smsList.size < 10) {
                    smsList.add(SimpleMessage(messageBody = smsInboxCursor.getString(messageBody),
                            address = smsInboxCursor.getString(address),
                            date = smsInboxCursor.getString(date),
                            person = smsInboxCursor.getString(person)
                    ))
                }
            } while (smsInboxCursor.moveToNext())
            smsInboxCursor.close()
        }
    }


}
