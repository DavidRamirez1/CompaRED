package david.ramirez2.upr.edu.atencioncorilla

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.FirebaseUser





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
        val username = username_edittext_register.text.toString()
        var existence = false

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Por favor llenar toda la informacion requerida.", Toast.LENGTH_SHORT).show()
            return
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users")


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                existence = p0.child("$username").exists()
                Log.d("Register", existence.toString())




                Log.d("Register", existence.toString())

                if (!existence) {

                    Log.d("Register", "Attempting to create user with email: $email")

                    // Firebase Authentication to create a user with email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if (!it.isSuccessful)return@addOnCompleteListener

                                    val user = FirebaseAuth.getInstance().currentUser
                                    if (user != null) {
                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                            .setDisplayName(username).build()
                                        user.updateProfile(profileUpdates)
                                    }
                                saveUserToFirebaseDatabase()
                                // else if successful
                                Log.d("Register", "Successfully created user with uid: ${it.result.user.uid}")
                            }
                            .addOnFailureListener {
                                Log.d("Register", "Failed to create user: ${it.message}")
                                Toast.makeText(applicationContext, "Error al crear usuario: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                } else {
                    Toast.makeText(applicationContext, "Nombre de usuario ya esta en uso.", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun saveUserToFirebaseDatabase() {
        val id = FirebaseAuth.getInstance().uid ?: ""
        val username = username_edittext_register.text.toString()
        val email = email_edittext_register.text.toString()

        val ref = FirebaseDatabase.getInstance().getReference("/users/$username")

            val user = User(id, email)
            ref.setValue(user)
                    .addOnSuccessListener {

                        Log.d("Register", "Finally we saved the user to Firebase Database")
                        finish()
                    }
                    .addOnFailureListener {
                        Log.d("Register", "Failed to set value to database: ${it.message}")
                    }
    }

}

class User(val uid: String, val email: String)