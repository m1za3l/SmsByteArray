package com.example.smsbytearray

import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log

class UtilsSms {

    companion object{

        fun sendMsm(phone :String, text: String){

            Log.d("m1m1m1", "sendMsm : phone: $phone, msm: $text")
            val smsManager: SmsManager =
                MainActivity.instance!!.getSystemService(SmsManager::class.java)
            val intentSMS = Intent(Intent.ACTION_SENDTO)
            intentSMS.putExtra("sms_body", "The SMS text")
            smsManager.sendTextMessage(phone, null, text, null, null)
        }

        fun sendSilent(phone :String, text: String){

            try{
                val pdu = text
                val bytes = pdu.toByteArray()
                var i=0
                for (byte in bytes)
                    Log.v("m1m1"," ${i++} | byte : ${byte}")

                Log.v("m1m1"," decode : ${bytes.decodeToString()}")

                SmsManager.getDefault().sendDataMessage(phone, null, 2948, bytes, null, null)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }


    fun readSms(){
        val cur = MainActivity.instance!!.contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)

        if (cur!!.moveToFirst()) { /* false = no sms */
            do {

                Log.d("m1m1m1", "----------------------------------------------------------------------------")

                var msgInfo = ""
                for (i in 0 until cur!!.columnCount) {
                    msgInfo += " " + cur!!.getColumnName(i) + ":" + cur.getString(i)

                    Log.d("m1m1m1", cur!!.getColumnName(i) + ":" + cur.getString(i))
                }
            } while (cur.moveToNext())
        }
    }


}