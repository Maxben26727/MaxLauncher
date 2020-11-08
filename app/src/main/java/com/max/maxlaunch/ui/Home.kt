package com.max.maxlaunch.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.max.maxlaunch.R
import com.max.maxlaunch.databinding.ActivityHomeBinding
import com.max.maxlaunch.ui.adapter.PackageAdapter


class Home : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if( getApplicationContext().checkSelfPermission( Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED )
            requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    999)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModelProviderFactory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)
       viewModel.date.observe(this, androidx.lifecycle.Observer {
           binding.date.text = it
       })

        viewModel.chargingOrNot.observe(this, Observer {
            when(it)
            {
                BatteryManager.BATTERY_STATUS_CHARGING->
                    binding.batteryindicator.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.noun_battery_charge, theme))
            }

        })
        viewModel.batteryInfo.observe(this, androidx.lifecycle.Observer { level ->
            when (level) {
                in 1..15 -> {
                    binding.battery.text = String.format("%s%%", level)
                    binding.battery.setTextColor(Color.RED)
                    binding.batteryindicator.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.noun_battery_1015, theme))
                }
                in 15..40 -> {
                    binding.battery.text = String.format("%s%%", level)
                    binding.batteryindicator.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.noun_battery_2040, theme))

                }
                in 40..60 -> {
                    binding.battery.text = String.format("%s%%", level)
                    binding.batteryindicator.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.noun_battery_4060, theme))

                }
                in 60..80 -> {
                    binding.battery.text = String.format("%s%%", level)
                    binding.batteryindicator.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.noun_battery_6080, theme))

                }
                in 80..100 -> {
                    binding.battery.text = String.format("%s%%", level)
                    binding.batteryindicator.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.noun_battery_full, theme))

                }
            }
        })

        binding.appDrawer.setHasFixedSize(true)
        binding.appDrawer.layoutManager = GridLayoutManager(this,4)
       binding.appDrawer.adapter = PackageAdapter(this)


        }

    override fun onPause() {
        super.onPause()
        this.unregisterReceiver(viewModel.dateListner)
        this.unregisterReceiver(viewModel.mBatInfoReceiver)

    }

    override fun onResume() {
        super.onResume()
        this.registerReceiver(viewModel.mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        this.registerReceiver(viewModel.dateListner, IntentFilter(Intent.ACTION_TIME_CHANGED))

    }

    override fun onBackPressed() {

    }


}




