package com.example.smsbytearray

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import android.widget.Toast


class SMSReceiver : BroadcastReceiver(){


        override fun onReceive(context: Context?, intent: Intent) {
            Log.d("m1m1m1", "onReceive")


            val message = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val content =  message[0].displayMessageBody
            Log.d("m1m1m1", "content: $content")
            Toast.makeText(context, content,
                Toast.LENGTH_LONG).show()

            if(MainActivity.instance != null){
                val binding= MainActivity.instance!!.binding
                binding.txtRecibe.text = content
            }

        }


}