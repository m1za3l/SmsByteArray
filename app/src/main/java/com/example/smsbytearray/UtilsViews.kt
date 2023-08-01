package com.example.smsbytearray

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.InputType
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import com.example.smsbytearray.databinding.ActivityMainBinding

class UtilsViews {

    companion object {
        fun setPassword(binding: ActivityMainBinding,context:Context){
            val passwordIcon = binding.passwordLayout
            var iconPassVisible = true
            passwordIcon.setOnClickListener {
                val textEdit=binding.numeroEt.text
                Log.d("m1m1m1", "textEdit : $textEdit ")
                if(iconPassVisible && textEdit.isNotEmpty()) {
                    val colorBlack = Color.BLACK
                    val btnMore = AppCompatResources.getDrawable(context, com.google.android.material.R.drawable.design_ic_visibility)
                    setColorFilter(btnMore, colorBlack, false)
                    passwordIcon.setImageDrawable(btnMore)
                    iconPassVisible=false
                    val type = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
                    binding.numeroEt.inputType = type

                }
                else {
                    val colorBlack = Color.BLACK
                    val btnMore = AppCompatResources.getDrawable(context,com.google.android.material.R.drawable.design_ic_visibility_off)
                    setColorFilter(btnMore, colorBlack, false)
                    passwordIcon.setImageDrawable(btnMore)
                    iconPassVisible=true
                    val type =   InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                    binding.numeroEt.inputType = type
                }

            }
        }

        private fun setColorFilter(drawable: Drawable?, color: Int, atopOrIn: Boolean){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                drawable?.colorFilter = BlendModeColorFilter(color, if (atopOrIn) BlendMode.SRC_ATOP else BlendMode.SRC_IN)
            } else {
                setColorFilterPrevious29(drawable, color, atopOrIn)
            }
        }

        @Suppress("deprecation")
        private fun setColorFilterPrevious29(drawable: Drawable?, color: Int, atopOrIn: Boolean){
            drawable?.setColorFilter(color, if (atopOrIn) PorterDuff.Mode.SRC_ATOP else PorterDuff.Mode.SRC_IN)
        }

    }
}