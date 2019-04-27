package com.example.loadgallery

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val componentName = ComponentName(this, MyService::class.java!!)
        val info = JobInfo.Builder(123, componentName)
            .setRequiresCharging(true)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .setPersisted(true)
            .setPeriodic((15 * 60 * 1000).toLong())
            .build()

        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled")
        } else {
            Log.d(TAG, "Job scheduling failed")
        }
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this,"start",Toast.LENGTH_LONG).show()
        val list = getExternalImagesPath(applicationContext)
        Toast.makeText(this,list.count().toString(),Toast.LENGTH_LONG).show()
        list.forEach {
            print(it)
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        }
    }

    private fun getExternalImagesPath(context: Context): ArrayList<String> {
        return getImagesPathFromUri(context, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    private fun getInternalImagesPath(context: Context): ArrayList<String> {
        return getImagesPathFromUri(context, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
    }

    private fun getImagesPathFromUri(context: Context, uri: Uri): ArrayList<String> {
        val cursor: Cursor?
        val column_index_data: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data)
                listOfAllImages.add(absolutePathOfImage)
            }
            cursor.close()
        }
        return listOfAllImages
    }
}
