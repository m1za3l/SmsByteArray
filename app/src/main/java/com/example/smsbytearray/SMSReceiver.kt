package com.example.smsbytearray

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import java.lang.Exception


class SMSReceiver : BroadcastReceiver(){

    private val SMS_RECEIVED = "android.intent.action.DATA_SMS_RECEIVE"

    override fun onReceive(context: Context?, intent: Intent) {
        receiveSmsData(intent)
    }

    fun receiveNormal(context: Context?, intent: Intent){
        Log.d("m1m1m1", "onReceive")
        if (intent.getAction().equals(SMS_RECEIVED)) {
        }

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
    private fun receiveSmsData(intent: Intent){
        Log.d("m1m1m1", "onReceive m1")
        try {
            val bundle = intent.extras
            val message = UtilsSms.readPduSplit(bundle)
            val byteArray = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            var celOrigin = ""

            for (sms in byteArray)
                celOrigin = sms.displayOriginatingAddress


            if (MainActivity.instance != null) {
                val binding = MainActivity.instance!!.binding
                binding.txtRecibe.text = message
                binding.txtCelOrigin.text = celOrigin
            }
        }catch (e:Exception) {e.printStackTrace()}
    }



}