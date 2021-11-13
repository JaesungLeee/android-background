package com.jslee.jobscheduler_demo

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.getSystemService
import com.jslee.jobscheduler_demo.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindViews()
    }

    private fun bindViews() {
        binding.startButton.setOnClickListener { startJob() }
        binding.stopButton.setOnClickListener { stopJob() }
    }

    private fun startJob() {
        if (isJobRunning(this)) {
            Toast.makeText(this, "Already Scheduled", Toast.LENGTH_SHORT).show()
            return
        }

        val jobComponentName = ComponentName(this, WeatherJobService::class.java)
        val job = JobInfo.Builder(JOB_ID, jobComponentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setRequiresDeviceIdle(false)
            .setRequiresCharging(false)
            .setPeriodic(TimeUnit.MINUTES.toMillis(60))

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(job.build())

        Toast.makeText(this, "Job Service Started", Toast.LENGTH_SHORT).show()
    }

    private fun stopJob() {
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(JOB_ID)

        Toast.makeText(this, "Job Service Stopped", Toast.LENGTH_SHORT).show()
    }


    private fun isJobRunning(context: Context): Boolean {
        var isJobScheduled = false
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        for (job in jobScheduler.allPendingJobs) {
            if (job.id == JOB_ID) {
                isJobScheduled = true
                break
            }
        }

        return isJobScheduled
    }
    companion object  {
        private const val JOB_ID = 10
    }
}