package com.example.smsbytearray

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButtonSendClicked(view: View){
        val destinationAddress: String = "5627392680"


        try {

            val pdu = "hola"
            val bytes = pdu.toByteArray()
            var i=0
            for (byte in bytes)
                Log.v("m1m1"," ${i++} | byte : ${byte}")

            Log.v("m1m1"," decode : ${bytes.decodeToString()}")


            SmsManager.getDefault().sendDataMessage(
                destinationAddress,
                null,
                2948,
                bytes,
                null,
                null
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}