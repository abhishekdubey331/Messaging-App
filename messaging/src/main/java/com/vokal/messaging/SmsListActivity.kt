package com.vokal.messaging

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.core.base.application.BaseActivity
import com.core.base.extensions.checkSmsReadPermission
import com.core.base.extensions.toast

class SmsListActivity : BaseActivity() {


    companion object {
        private const val REQUEST_CODE_PERMISSION_READ_SMS = 456
    }

    private val smsList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_list)
        if (checkSmsReadPermission()) {
            readSmsFromInbox()
        } else {
            askSmsPermission()
        }

        toast(smsList.size.toString())
    }

    private fun askSmsPermission() {
        ActivityCompat.requestPermissions(this@SmsListActivity, arrayOf(
                Manifest.permission.READ_SMS), REQUEST_CODE_PERMISSION_READ_SMS)
    }

    private fun readSmsFromInbox() {
        val smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)
        smsInboxCursor?.let {
            val indexBody = smsInboxCursor.getColumnIndex("body")
            val indexAddress = smsInboxCursor.getColumnIndex("address")
            if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return
            do {
                var str = "SMS from : " + smsInboxCursor.getString(indexAddress) + "\n"
                str += smsInboxCursor.getString(indexBody)
                if (smsList.size < 10)
                    smsList.add(str)
            } while (smsInboxCursor.moveToNext())
            smsInboxCursor.close()
        }
    }
}
