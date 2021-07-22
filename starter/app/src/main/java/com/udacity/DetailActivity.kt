package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setSupportActionBar(binding.toolbar)

        val bundle: Bundle? = intent.extras
        bundle?.let {
            val fileTitle = it.getString(getString(R.string.intent_file_title), "No title")
            binding.fileNameValue.text = fileTitle

            val downloadSuccessful = it.getBoolean(getString(R.string.intent_success), false)
            if (downloadSuccessful) {
                binding.statusValue.text = getString(R.string.status_success)
                binding.statusValue.setTextColor(getColor(R.color.success))
            } else {
                binding.statusValue.text = getString(R.string.status_fail)
                binding.statusValue.setTextColor(getColor(R.color.failure))
            }

            val notificationId = intent.getIntExtra(getString(R.string.intent_notification_id), 0)
            getSystemService(NotificationManager::class.java).cancel(notificationId)
        }

        binding.buttonOk.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}