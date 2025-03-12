package com.bookiron.itpark.utils

import android.content.Context


class MyPref() {
    companion object{


        fun setUser(ctx:Context,user:String,mobile_number:String,full_name:String,email_id:String)
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
            editor.putString("user_id",user)
            editor.putString("mobile_number",mobile_number)
            editor.putString("full_name",full_name)
            editor.putString("email_id",email_id)
            editor.commit()

        }

        fun setProfileData(ctx:Context,profile:String)
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("profile",profile)
            editor.commit()
        }

        fun getProfileData(ctx:Context): String?
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            return sharedPreference.getString("profile","")
        }

        fun getUser(ctx:Context): String?
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            return sharedPreference.getString("user_id","")
        }
        fun getMobile(ctx:Context): String?
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            return sharedPreference.getString("mobile_number","")
        } fun getEmail(ctx:Context): String?
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            return sharedPreference.getString("email_id","")
        }
        fun getName(ctx:Context): String?
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            return sharedPreference.getString("full_name","")
        }
        fun setName(ctx:Context,full_name:String)
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()

            editor.putString("full_name",full_name)
            editor.commit()
        }

        fun getOccupation(ctx:Context): String?
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            return sharedPreference.getString("occupation","")
        }
        fun setOccupation(ctx:Context,occupation:String)
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()

            editor.putString("occupation",occupation)
            editor.commit()
        }

        fun getAddress(ctx:Context): String?
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            return sharedPreference.getString("address","")
        }
        fun setAddress(ctx:Context,address:String)
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()

            editor.putString("address",address)
            editor.commit()
        }


        fun setEmail(ctx:Context,email_id:String)
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("email_id",email_id)
            editor.commit()
        }


        fun setUserActiveStatus(ctx:Context,status:Int)
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putInt("active_status",status)

            editor.commit()

        }
        fun getUserActiveStatus(ctx:Context): Int
        {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            return sharedPreference.getInt("active_status",1)
        }

        fun clear(ctx: Context) {
            val sharedPreference =  ctx.getSharedPreferences("Gaadiwala_Driver", Context.MODE_PRIVATE)
            sharedPreference.edit().clear().commit()

        }
    }
}