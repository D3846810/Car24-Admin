package uk1.ac.tees.mad.d3846810.utils

import android.content.Context
import com.google.gson.Gson
import uk1.ac.tees.mad.d3846810.model.LoginModel

object SharedPref {



    private fun convertUserModelToJson(loginModel: LoginModel): String {
        val gson = Gson()
        return gson.toJson(loginModel)
    }

    fun saveUserData(context: Context, loginModel: LoginModel) {
        val sharedPreferences = context.getSharedPreferences(Constants.LOGIN_REF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val jsonString = convertUserModelToJson(loginModel)
        editor.putString("user_model", jsonString)
        editor.apply()
    }



    fun getUserData(context: Context): LoginModel?{
        val sharedPreferences = context.getSharedPreferences(Constants.LOGIN_REF, Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("user_model", null)

        if (jsonString != null) {
            val gson = Gson()
            return gson.fromJson(jsonString, LoginModel::class.java)
        }

        return null
    }


    fun clearData(context: Context){
        val sp = context.getSharedPreferences(Constants.LOGIN_REF, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        editor.apply()
    }
}