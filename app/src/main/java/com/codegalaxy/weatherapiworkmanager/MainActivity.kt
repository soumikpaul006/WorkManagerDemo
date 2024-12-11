package com.codegalaxy.weatherapiworkmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.codegalaxy.weatherapiworkmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnFetchPeriodic.setOnClickListener{

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()


            val periodicWorkRequest= PeriodicWorkRequestBuilder<WeatherWorker>(30,TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "WetherPeriodicWork",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest
            )

            observerPeriodicWork()
        }

        binding.btnFetchNow.setOnClickListener{

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest= OneTimeWorkRequestBuilder<WeatherWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(this).enqueue(workRequest)

            WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id)
                .observe(this){ workInfo->
                    if (workInfo != null) {
                        when(workInfo.state) {

                            WorkInfo.State.ENQUEUED ->
                            {
                                println("WorkManager || ENQUEUED")
                            }
                            WorkInfo.State.RUNNING -> {
                                println("WorkManager || RUNNING")
                            }
                            WorkInfo.State.SUCCEEDED -> {

                                val city=workInfo.outputData.getString("city")?:"unknown"
                                val temp=workInfo.outputData.getDouble("temp",0.0)

                                binding.tvCityAndTemp.text="City: $city, Temp: $temp C"

                                println("WorkManager || SUCCEEDED")
                            }
                            WorkInfo.State.FAILED -> {
                                println("WorkManager || FAILED")
                            }
                            WorkInfo.State.BLOCKED -> {
                                println("WorkManager || BLOCKED")
                            }
                            WorkInfo.State.CANCELLED -> {
                                println("WorkManager || CANCELLED")
                            }
                        }
                    }
                }
        }
    }

    private fun observerPeriodicWork()
    {
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("WetherPeriodicWork")
            .observe(this){ workInfos->
                val workInfo=workInfos[0]
                when(workInfo.state)
                {
                    WorkInfo.State.ENQUEUED ->
                    {
                        println("WorkManager || ENQUEUED || Periodic")
                    }
                    WorkInfo.State.RUNNING -> {
                        println("WorkManager || RUNNING || Periodic")
                    }
                    WorkInfo.State.SUCCEEDED -> {

                        val city=workInfo.outputData.getString("city")?:"unknown"
                        val temp=workInfo.outputData.getDouble("temp",0.0)

                        binding.tvCityAndTemp.text="City: $city, Temp: $temp C"

                        println("WorkManager || SUCCEEDED || Periodic")
                    }
                    WorkInfo.State.FAILED -> {
                        println("WorkManager || FAILED || Periodic")
                    }
                    WorkInfo.State.BLOCKED -> {
                        println("WorkManager || BLOCKED || Periodic")
                    }
                    WorkInfo.State.CANCELLED -> {
                        println("WorkManager || CANCELLED || Periodic")
                    }
                }
            }
    }
}


