package com.vokal.messaging.utils

import android.content.ContentResolver
import android.net.Uri
import com.vokal.messaging.data.SimpleMessage
import java.util.Date

class MessageHelper {


    fun readMessagesFromInbox(contentResolver: ContentResolver, callback: (result: MutableList<SimpleMessage>) -> Unit) {
        val messageList: MutableList<SimpleMessage> = mutableListOf()
        val smsInboxCursor = contentResolver
                .query(Uri.parse("content://sms/inbox"), null, "date" + ">?",
                        arrayOf("" + getDate()), "date DESC")
        smsInboxCursor?.let {
            val messageBody = smsInboxCursor.getColumnIndex("body")
            val address = smsInboxCursor.getColumnIndex("address")
            val date = smsInboxCursor.getColumnIndex("date")
            val person = smsInboxCursor.getColumnIndex("person")
            if (messageBody < 0 || !smsInboxCursor.moveToFirst()) return
            do {
                var str = "SMS from : " + smsInboxCursor.getString(address) + "\n"
                str += smsInboxCursor.getString(messageBody)
                messageList.add(SimpleMessage(messageBody = smsInboxCursor.getString(messageBody),
                        address = smsInboxCursor.getString(address),
                        date = smsInboxCursor.getString(date),
                        person = smsInboxCursor.getString(person)
                ))
            } while (smsInboxCursor.moveToNext())
            callback.invoke(messageList)
            smsInboxCursor.close()
        }
    }

    private fun getDate(): Long {
        return Date(System.currentTimeMillis() - 1L * 24 * 3600 * 1000).time
    }

}