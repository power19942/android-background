package com.example.loadgallery

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import kotlin.concurrent.thread
import android.os.Looper



@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MyService : JobService() {
    private val TAG = "ExampleJobService"
    private var jobCancelled = false

    override fun onStartJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job started")
        doBackgroundWork(params)

        return true
    }

    private fun doBackgroundWork(params: JobParameters) {
        Thread(Runnable {
            while (true) {
                //Log.d(TAG, "run: $i")
                //Toast.makeText(applicationContext,"woooo",Toast.LENGTH_SHORT).show()
                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {
                    Toast.makeText(
                        this@MyService.applicationContext,
                        "My Awesome service toast...",
                        Toast.LENGTH_SHORT
                    ).show()
                })
                if (jobCancelled) {
                    return@Runnable
                }

                try {
                    Thread.sleep(10000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

            Log.d(TAG, "Job finished")
            jobFinished(params, false)
        }).start()
    }

    override fun onStopJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job cancelled before completion")
        jobCancelled = true
        return true
    }
}
