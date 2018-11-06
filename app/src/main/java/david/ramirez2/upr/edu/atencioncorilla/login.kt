package david.ramirez2.upr.edu.atencioncorilla

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            performLogin()

        }

        back_to_register_textview.setOnClickListener{

            // launch the login activity somehow
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
    private fun performLogin() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email anf password.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    val intent = Intent(this, NavigationActivity::class.java)
                    startActivity(intent)
                    Log.d("Login", "Successfully logged in: ${it.result.user.uid}")
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

}