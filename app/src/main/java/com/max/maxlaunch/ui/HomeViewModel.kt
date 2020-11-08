package com.max.maxlaunch.ui

import android.app.Application
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.BatteryManager
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var dateListner: BroadcastReceiver
    lateinit var mBatInfoReceiver: BroadcastReceiver
    val app: Application = application
    var date: MutableLiveData<String> = MutableLiveData()
    var batteryInfo: MutableLiveData<Int> = MutableLiveData()
    var chargingOrNot: MutableLiveData<Int> = MutableLiveData()

    init {
        setDate()
        initializeBatteryBR()
        initializeDateBR()
    }


    private fun initializeBatteryBR() {
        mBatInfoReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context?, intent: Intent) {
                batteryInfo.value = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                chargingOrNot.value = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)

            }
        }
    }

    private fun initializeDateBR() {
        dateListner = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, i: Intent?) {
                setDate()
            }
        }

    }

    fun setDate() {
        val dayFormat = SimpleDateFormat("EE-d-m-YYYY", Locale.US)
        val calendar: Calendar = Calendar.getInstance()
        date.value = dayFormat.format(calendar.getTime())
    }


}