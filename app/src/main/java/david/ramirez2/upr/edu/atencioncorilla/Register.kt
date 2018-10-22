package david.ramirez2.upr.edu.atencioncorilla

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()

            Log.d("MainActivity", "Email is: " + email)
            Log.d("MainActivity", "Password: $password")

            // Firebase Authentication to create a user with email and password
        }

        already_have_account_text_view.setOnClickListener {
            Log.d("MainActivity", "Try to show login activity")

            // launch the login activity somehow
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
}
