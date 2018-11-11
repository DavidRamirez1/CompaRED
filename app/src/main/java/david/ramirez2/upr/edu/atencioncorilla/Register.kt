package david.ramirez2.upr.edu.atencioncorilla

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            performRegister()
            // Firebase Authentication to create a user with email and password
        }

        already_have_account_text_view.setOnClickListener {
            Log.d("Register", "Try to show login activity")
            finish()
        }

    }
    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in all fields", Toast.LENGTH_SHORT).show()
            return
        }


        Log.d("Register",  "Attempting to create user with email: $email")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                        saveUserToFirebaseDatabase()
                    // else if successful
                    Log.d("Register",  "Successfully created user with uid: ${it.result.user.uid}")
                }
                .addOnFailureListener{
                    Log.d("Register",  "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun saveUserToFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username_edittext_register.text.toString())

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("Register",  "Finally we saved the user to Firebase Database")
                }
                .addOnFailureListener {
                    Log.d("Register",  "Failed to set value to database: ${it.message}")
                }
    }

}

class User(val uid: String, val username: String)