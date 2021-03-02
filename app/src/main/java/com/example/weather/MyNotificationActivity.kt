package com.example.weather

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity

class MyNotificationActivity : AppCompatActivity() {
    lateinit var btnNot: Button
    lateinit var builder: Notification.Builder
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_notification)
        btnNot = findViewById(R.id.button)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        btnNot.setOnClickListener {
            MainActivity.loadWeather(this, sharedPreferences.getString("City", "Бугульма")!!, false)
        }
    }

    fun яУзналТемпературу(temper: String) {
        createNotification(temper)
    }

    private fun createNotification(temper: String) {

        val intent = Intent(this, MainActivity::class.java)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val contentView = RemoteViews(packageName, R.layout.activity_after_notification)

        contentView.setTextViewText(R.id.textViewWeather, "Ваш город: ${sharedPreferences.getString("City", "Бугульма")}")
        contentView.setTextViewText(R.id.textViewTemper, "Температура: $temper")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri: Uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.sound)

            builder = Notification.Builder(this, WeatherApplication.CHANNEL_ID)
                    .setCustomContentView(contentView)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                    .setContentIntent(pendingIntent)
        } else {
            builder = Notification.Builder(this)
                    .setContent(contentView)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                    .setContentIntent(pendingIntent)
        }
        val notification = builder.build()
        notificationManager.notify(1234, notification)
    }
}
