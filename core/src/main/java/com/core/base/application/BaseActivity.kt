package com.core.base.application

import android.Manifest
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.core.base.extensions.checkSmsReadPermission

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_PERMISSION_READ_SMS = 456
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()
    }

    private fun setUpToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isUserCheckNeverAskAgain() =
            !ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_SMS
            )

    fun askMessagePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_SMS), REQUEST_CODE_PERMISSION_READ_SMS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, thePermissions: Array<String>, theGrantResults: IntArray) {
        // It's not our expect permission
        if (requestCode != REQUEST_CODE_PERMISSION_READ_SMS) return

        if (checkSmsReadPermission()) {
            // Success case: Get the permission
            // Do something and return
            permissionGrantedReadSms()
            return
        }

        if (isUserCheckNeverAskAgain()) {
            // NeverAskAgain case - Never Ask Again has been checked
            // Do something and return
            return
        }

        // Failure case: Not getting permission
        // Do something here
    }

    abstract fun permissionGrantedReadSms()
}