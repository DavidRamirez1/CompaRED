package david.ramirez2.upr.edu.atencioncorilla

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login: AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        verifyUserIsLoggedIn()

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

        // Ask for user's location permissions

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

    }
    private fun performLogin() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor llenar toda la informacion requerida.", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "Error al iniciar sesión: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }
    private fun verifyUserIsLoggedIn() {
        val userid = FirebaseAuth.getInstance().uid
        Log.d("Login", "$userid")

        if (userid != null) {
            val intent = Intent(this, NavigationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
