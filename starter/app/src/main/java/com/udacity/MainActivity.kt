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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private var objectToDownload: ObjectToDownload? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download() {
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
