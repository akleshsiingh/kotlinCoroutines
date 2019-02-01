package com.example.koltincorutines.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.example.koltincorutines.R
import com.example.koltincorutines.data.RetrofitFactory
import com.example.koltincorutines.utils.Logger
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var job: Job

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val executerService = Executors.newFixedThreadPool(2)
    private val coroutineDispatcher = executerService.asCoroutineDispatcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job = Job()

        val api = RetrofitFactory.api
        GlobalScope
            .launch(Dispatchers.Main) {
                val resp = api.getPopularMovie().await()
                if (resp.isSuccessful && resp.body() != null) {
                    Logger.e(TAG, " ${resp.body()}")

                    val result = resp.body()!!.results

                    tvStatus.text = result
                        .filter { m -> !TextUtils.isEmpty(m.title) }.joinToString("\n") { it.title }
                } else
                    Logger.e(TAG, " ${resp.errorBody()}")

                Logger.e(TAG, "STARTING withContext(IO)  THREAD ${Thread.currentThread().name} ")
                val i = withContext(IO) {
                    showCurrentThread("Method 1 ")
                }
                Logger.e(TAG, "ASYNC RESULT ${i}")

                launch {  }
            }


        //    tvStatus
    }

    private fun showCurrentThread(s: String): String {
        Logger.e(TAG, "STARTED for $s THREAD ${Thread.currentThread().name} ")
        Thread.sleep(4000)
        Logger.e(TAG, "FINISHED for $s THREAD ${Thread.currentThread().name} ")
        return "$s DONE"
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
