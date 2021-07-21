package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private var objectToDownload: ObjectToDownload? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.customButton.setOnClickListener { download() }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val cursor = downloadManager.query(DownloadManager.Query())
                if (cursor.moveToFirst()) {
                    val title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    pushNotification(title, status == DownloadManager.STATUS_SUCCESSFUL)
                }
                binding.customButton.downloadComplete()
            }
        }
    }

    private fun download() {
        if (objectToDownload == null) {
            Toast.makeText(this, R.string.select_file_error, Toast.LENGTH_LONG).show()
        } else {
            binding.customButton.buttonClicked()
            val request =
                DownloadManager.Request(Uri.parse(objectToDownload?.url))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }
    }

    private fun pushNotification(title: String, success: Boolean) {
        Toast.makeText(this, "$title : $success", Toast.LENGTH_LONG).show()
    }

    fun onRadioButtonCLicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.id) {
                R.id.radio_glide ->
                    if (checked) {
                        objectToDownload = ObjectToDownload.GLIDE
                    }
                R.id.radio_project ->
                    if (checked) {
                        objectToDownload = ObjectToDownload.UDACITY_PROJECT
                    }
                R.id.radio_retrofit ->
                    if (checked) {
                        objectToDownload = ObjectToDownload.RETROFIT
                    }
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "channelId"
    }

    enum class ObjectToDownload(val url: String) {
        GLIDE("https://github.com/bumptech/glide/archive/refs/heads/master.zip"),
        UDACITY_PROJECT("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"),
        RETROFIT("https://github.com/square/retrofit/archive/refs/heads/master.zip");
    }
}
