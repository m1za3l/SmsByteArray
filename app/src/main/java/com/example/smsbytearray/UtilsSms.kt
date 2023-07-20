package com.example.smsbytearray

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import android.util.Log
import androidx.core.app.ActivityCompat

class UtilsSms {

    companion object{

        const val clave_string="pdu-"
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

        fun sendTelcel(phone :String, text: String){
            try{
                val subId =  1
                val port = 8901
                var sms = clave_string
                sms+=text
                val phone = phone
                val bytes = sms.toByteArray()

                SmsManager.getDefault().sendDataMessage(
                    phone, null, port.toShort(), bytes, null, null)
                Log.v("m1m1"," sendDataMessage  | port: $port | phone : $phone | sms $text | subId : Default()")

                //doble chip
                //SmsManager.getSmsManagerForSubscriptionId(subId).sendDataMessage(
                //    phone, null, port.toShort(), bytes, null, null)


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        fun readPdu26bits(bundle: Bundle?) {
            try {
                if (bundle != null) {
                    val pdusObj = bundle["pdus"] as Array<Any>?

                    for (i in pdusObj!!.indices) {
                        //todo : la magia empieza
                        val array: ByteArray = pdusObj[i] as ByteArray
                        val size = array.size
                        val array1 = ArrayList<Byte>()
                        val array2 = ArrayList<Byte>()

                        //apartir del indice 26 pero indice 25 puede q ya venga el sms

                        for ((i, byte) in array.withIndex()) {
                            if (i > (25)) {//7bits o un bit menos , calado
                                array1.add(byte)
                            }
                            if (i > (33)) {//8bits
                                array2.add(byte)
                            }
                        }

                        Log.d("m1m1m1", "onReceive size: $size")
                        val result = ByteArray(array1.size)
                        for (i in 0 until array1.size)
                            result[i] = array1[i]

                        Log.d("m1m1m1", "onReceive 16bits: ${result.decodeToString()}")

                        val result2 = ByteArray(array2.size)
                        for (i in 0 until array2.size)
                            result2[i] = array2[i]

                        Log.d("m1m1m1", "onReceive 14bits: ${result2.decodeToString()}")
                        //todo: la magia termina
                    }
                }
            } catch (e: Exception) {
                Log.e("SmsReceiver", "Exception smsReceiver$e")
            }

        }
        fun readPduSplit(bundle: Bundle?):String {
            var sms = "no se puede procesar el sms"
            try {
                if (bundle != null) {
                    val pdusObj = bundle["pdus"] as Array<*>?

                    for (i in pdusObj!!.indices) {
                        val array: ByteArray = pdusObj[i] as ByteArray

                        Log.d("m1m1m1", " pdusObj[i]: ${array.decodeToString()}")

                        val str = array.decodeToString()
                        val delim = clave_string

                        val list = str.split(delim)
                        Log.d("m1m1m1", "list: $list")

                        if(list.size == 2) {
                            sms = list[1]
                            Log.d("m1m1m1", "sms: $sms")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SmsReceiver", "Exception smsReceiver$e")
            }
        return sms
        }
        fun idsDualSim(context:Context, phone :String, text: String){
            val listSubscription = listOf('1', '2')
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS)
                    != PackageManager.PERMISSION_GRANTED ) {
                    Log.v("m1m1", "no hay persmiso, envio de sms por default")
                    sendTelcel(phone , text)
                } else {
                    Log.v("m1m1", " si hay permsiso")

                    val simCardList = ArrayList<Int>()
                    val subscriptionManager: SubscriptionManager = SubscriptionManager.from(context)
                    val subscriptionInfoList = subscriptionManager.activeSubscriptionInfoList
                    for (subscriptionInfo in subscriptionInfoList) {
                        val subscriptionId = subscriptionInfo.subscriptionId
                        simCardList.add(subscriptionId)
                        Log.v("m1m1", " subsInfoList | subInfo.subscriptionId : $subscriptionId")
                    }

                    for (idSimCard in simCardList) {
                        val port = 8901
                        var sms = clave_string
                        sms+=text
                        val phone = phone
                        val bytes = sms.toByteArray()

                        SmsManager.getSmsManagerForSubscriptionId(idSimCard).sendDataMessage(
                            phone, null, port.toShort(), bytes, null, null)
                        Log.v("m1m1", "idSimCard: $idSimCard |sendDataMessage| port: $port | phone : $phone | sms $sms subId : $idSimCard")

                    }
                }
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