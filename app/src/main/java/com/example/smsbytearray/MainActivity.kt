package com.example.smsbytearray

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.smsbytearray.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    companion object{
        var instance: MainActivity? = null
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) {
                permissions -> // Handle Permission granted/rejected
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted && permissionName == "android.permission.SEND_SMS") {
                    //sendMsm()
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("m1m1m1", "oncreate")
        //anterior commit, silent sms y Dual Sim
        instance=this

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        activityResultLauncher.launch(arrayOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
        ))

        clickButton()
        UtilsViews.setPassword(binding,this)
    }

    private fun clickButton(){
        binding.send.setOnClickListener {
            //send verification code to server
            val phne : String = binding.numeroEt.text.toString()
            val sms : String = binding.sms.text.toString()

            Log.d("m1m1m1", "phne: $phne | sms: $sms ")
            //UtilsSms.sendMsm(phne,sms)
            UtilsSms.sendTelcel(phne,sms)


            Toast.makeText(this, "sms ENVIADO | phone: $phne | sms: $sms ",
                Toast.LENGTH_LONG).show()
        }
    }


}