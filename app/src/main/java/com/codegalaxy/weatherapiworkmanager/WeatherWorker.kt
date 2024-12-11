package com.codegalaxy.weatherapiworkmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.codegalaxy.weatherapiworkmanager.network.ApiClient
import com.codegalaxy.weatherapiworkmanager.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WeatherWorker(
    context : Context,
    params : WorkerParameters,
): Worker(context,params)
{

    private val repository=WeatherRepository(ApiClient.weatherApiService)

    override fun doWork(): Result {

        var finalResult:Result=Result.retry()

        CoroutineScope(Dispatchers.Default).launch {
            try {

                val response = repository.fetchWeather("kolkata")

                val outputData = Data.Builder()
                    .putString("city", response.location.name)
                    .putDouble("temp", response.current.temp_c)
                    .build()

                showNotification(response.location.name, response.current.temp_c)
                finalResult=Result.success(outputData)
            }
            catch (e:Exception) {
                e.printStackTrace()
                finalResult=Result.retry()
            }
        }
        return finalResult
    }

    private fun showNotification(city:String,temp:Double)
    {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId="weather_channel"

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "Weather Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification=NotificationCompat.Builder(applicationContext,channelId)
            .setContentTitle("Weather Updates")
            .setContentText("City: $city and Temp: $temp C")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        notificationManager.notify(1,notification)
    }
}