package com.jslee.jobscheduler_demo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class WeatherJobService: JobService() {
    override fun onStartJob(params: JobParameters): Boolean {
        Log.e(LOG_TAG, "onStartJob()")
        getCurrentWeather(params)

        return true
    }



    override fun onStopJob(params: JobParameters): Boolean {
        Log.e(LOG_TAG, "onStopJob()")
        return true
    }

    private fun getCurrentWeather(params: JobParameters) {
        Log.e(LOG_TAG, "getCurrentWeather()")

        Thread {
            WeatherClient.retrofitInstance.getWeatherData(LATITUDE, LONGTITUDE, APP_ID)
                .enqueue(object : Callback<WeatherResponseDTO> {
                    override fun onResponse(
                        call: Call<WeatherResponseDTO>,
                        response: Response<WeatherResponseDTO>
                    ) {
                        if (response.code() == 200) {
                            val weatherResponse = response.body()

                            var temp = weatherResponse?.main?.temp?.minus(273.15)
                            val minimumTemp = weatherResponse?.main?.minTemp?.minus(273.15)
                            val maximumTemp = weatherResponse?.main?.maxTemp?.minus(273.15)

                            var averageTemp = (minimumTemp?.plus(maximumTemp!!))?.div(2)

                            val tempText = temp?.roundToInt()
                            val averageTempText = averageTemp?.roundToInt()

                            val title = "Weather"
                            val message = "Temperature : $tempText, Average : $averageTempText"
                            val notificationId = 100
                            showNotification(applicationContext, title, message, notificationId)

                        }
                    }

                    override fun onFailure(call: Call<WeatherResponseDTO>, t: Throwable) {
                        Log.e(LOG_TAG, "getCurrentWeather Failed by ${t.message}")
                    }

                })

            jobFinished(params, false)
        }
    }

    private fun showNotification(context: Context, title: String, message: String, notificationId: Int) {
        val channelID = "CHANNEL_ID"
        val channelName = "JobScheduler Weather Channel"


        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val ringtoneSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, channelID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText(message)
            .setSound(ringtoneSound)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            notificationBuilder.setChannelId(channelID)
            notificationManagerCompat.createNotificationChannel(notificationChannel)
        }

        val notification = notificationBuilder.build()
        notificationManagerCompat.notify(notificationId, notification)
    }

    companion object {
        private const val LOG_TAG = "WeatherJobService"

        private const val LATITUDE = "37.445293"
        private const val LONGTITUDE = "126.785823"
        private const val APP_ID = "1cdf1f631d32bf81a63275b6486282f4"
    }
}