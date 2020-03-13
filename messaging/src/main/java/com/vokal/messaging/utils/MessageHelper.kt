package com.vokal.messaging.utils

import android.content.ContentResolver
import android.net.Uri
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.vokal.messaging.data.SimpleMessage
import io.reactivex.Single
import java.util.Date

class MessageHelper {

    fun readMessagesFromInbox(contentResolver: ContentResolver, days: Long): Single<List<SimpleMessage>> {
        val messageList: MutableList<SimpleMessage> = mutableListOf()
        val smsInboxCursor = contentResolver
                .query(Uri.parse("content://sms/inbox"), null, "date" + ">?",
                        arrayOf("" + getDate(days)), "date DESC")
        smsInboxCursor?.let {
            val messageBody = smsInboxCursor.getColumnIndex("body")
            val address = smsInboxCursor.getColumnIndex("address")
            val date = smsInboxCursor.getColumnIndex("date")
            val person = smsInboxCursor.getColumnIndex("person")
            if (messageBody < 0 || !smsInboxCursor.moveToFirst()) return Single.just(mutableListOf()) // return empty list
            do {
                var str = "SMS from : " + smsInboxCursor.getString(address) + "\n"
                str += smsInboxCursor.getString(messageBody)
                messageList.add(SimpleMessage(messageBody = smsInboxCursor.getString(messageBody),
                        address = smsInboxCursor.getString(address),
                        time = TimeAgo.using(smsInboxCursor.getString(date).toLong()).capitalize(),
                        person = smsInboxCursor.getString(person)
                ))
            } while (smsInboxCursor.moveToNext())
            smsInboxCursor.close()
        }
        return Single.just(messageList.toList())
    }

    private fun getDate(days: Long): Long {
        // return only messages 1 day old
        return Date(System.currentTimeMillis() - days * 24 * 3600 * 1000).time
    }

}