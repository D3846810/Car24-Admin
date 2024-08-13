package uk1.ac.tees.mad.d3846810.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk1.ac.tees.mad.d3846810.model.LoginModel
import uk1.ac.tees.mad.d3846810.screens.LoginScreen
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.SharedPref
import uk1.ac.tees.mad.d3846810.utils.Utils

class LoginActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var progress: AlertDialog
    private var login = LoginModel()
    private var buttonLoginTxt by mutableStateOf("Login")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
        progress = Utils.showLoading(this@LoginActivity)

        checkLoginRegister()

        setContent {
            LoginScreen(
                buttonLoginTxt = buttonLoginTxt
            ) { email, pass ->
                if (buttonLoginTxt == "Add New") {
                    checkRegisterInput(email, pass)
                } else if (login.email == email && login.password == pass) {
                    progress.dismiss()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Utils.showMessage(this@LoginActivity, "Something went wrong")
                }
            }
        }
    }

    private fun checkRegisterInput(email: String, pass: String) {
        createLogin(email, pass)
    }

    private fun checkLoginRegister() {
        progress.show()
        database.reference.child(Constants.LOGIN_REF)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        login = dataSnapshot.getValue(LoginModel::class.java)!!
                        SharedPref.saveUserData(this@LoginActivity, login)
                        progress.dismiss()
                    } else {
                        buttonLoginTxt = "Add New"
                        progress.dismiss()
                        Utils.showMessage(this@LoginActivity, "Please Register to Login")

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                    progress.dismiss()
                    Utils.showMessage(this@LoginActivity, "Database Error")

                }
            })
    }

    private fun createLogin(email: String, pass: String) {
        progress.show()
        val login = LoginModel(
            Constants.LOGIN_REF, email, pass
        )
        database.reference.child(Constants.LOGIN_REF).setValue(login)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progress.dismiss()
//                    binding.btLogin.text = "Login"
                    buttonLoginTxt = "Login"
                    checkLoginRegister()
                    Utils.showMessage(this, "Login Registered")

                } else {
                    progress.dismiss()
                    Utils.showMessage(this, "Something went wrong")
                }
            }
    }
}