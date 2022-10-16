package com.example.alarmmanager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.alarmmanager.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding
        private lateinit var Picker : MaterialTimePicker
        private lateinit var calendar: Calendar
        private lateinit var alarmManager: AlarmManager
        private lateinit var pendingIntent: PendingIntent



        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            createNotificationChannel()

            binding.selecttimeBtn.setOnClickListener{
                showtimePicker()
            }
            binding.setAlarmBtn.setOnClickListener{

                setAlarm()
            }
            binding.cancelAlarmBtn.setOnClickListener{

                cancelAlarm()
            }

        }

        private fun cancelAlarm() {

            alarmManager= getSystemService(ALARM_SERVICE) as AlarmManager
            val intent= Intent(this, AlarmReceiver ::class.java)

            pendingIntent = PendingIntent.getBroadcast(this,0,intent,0)


            alarmManager.cancel(pendingIntent)

            Toast.makeText(this,"Alarm Cancelled", Toast.LENGTH_LONG).show()

        }

        private fun setAlarm() {

            alarmManager= getSystemService(ALARM_SERVICE) as AlarmManager
            val intent= Intent(this, AlarmReceiver ::class.java)

            pendingIntent = PendingIntent.getBroadcast(this,0,intent,0)

            alarmManager.setRepeating(

                AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,pendingIntent
            )

            Toast.makeText(this,"Alarm Set Successfully", Toast.LENGTH_SHORT).show()
        }


        private fun showtimePicker() {

            Picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("select alarm Time")
                .build()

            Picker.show(supportFragmentManager, "omkar")

            Picker.addOnPositiveButtonClickListener{
                if(Picker.hour > 12){

                    binding.selectedtime.text=
                        String.format("%02d",Picker.hour - 12) + " : " + String.format(
                            "%02d",
                            Picker.minute
                        ) + "PM"
                }else{

                    String.format("%02d",Picker.hour) + " : " + String.format(
                        "%02d",
                        Picker.minute
                    ) + "AM"


                }

                calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = Picker.hour
                calendar[Calendar.MINUTE] = Picker.minute
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0




            }

        }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: CharSequence = "omkarReminderChannel"
                val description = "channel for alarm manager"
                val impotance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel("omkar",name,impotance)
                channel.description= description
                val notificationManager= getSystemService(
                    NotificationManager::class.java
                )

                notificationManager.createNotificationChannel(channel)


            }
        }
}
