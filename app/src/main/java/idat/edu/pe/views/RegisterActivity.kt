package idat.edu.pe.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import idat.edu.pe.databinding.ActivityRegisterBinding
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        binding.btnRegister.setOnClickListener {
            if(binding.txtNombre.text.isBlank()&& binding.txtEmail.text.isBlank()&& binding.txtPassword.text.isBlank()){
                val datos = hashMapOf(
                   "nombre" to binding.txtNombre.text,
                   "email" to  binding.txtEmail.text,
                    "contraseña" to binding.txtPassword.text
                )
//                db.collection("usuarios")
//                    .add(datos)
//                    .addOnSuccessListener { documentReference ->
//                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                    }
//                    .addOnFailureListener { e ->
//                        Log.w(TAG, "Error adding document", e)
//                    }
            }

            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val repeatPassword = binding.txtRepitPassword.text.toString()

            // validar si la password cumple con los requisistos
            val passwordRegex = Pattern.compile(
                "^" +
                        "(?=.*[-@#$^&+=])" +  // al menos un caracter especial
                        ".{6,}" +             // al menos 6 caracteres
                        "$"
            )

            // validar si el correo existe
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(baseContext, "Email invalid.", Toast.LENGTH_SHORT).show()
                // analysand si cumple los requisistos el password
            } else if (password.isEmpty() || !passwordRegex.matcher(password).matches()) {
                Toast.makeText(baseContext, "Password slow", Toast.LENGTH_SHORT).show()
                // analyzer if the password is equal a repeat password
            } else if (password != repeatPassword) {
                Toast.makeText(baseContext, "Confirm to Password", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }

        binding.txtIrLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                reload();
            } else {
                val intent = Intent(this, CheckEmailActivity::class.java)
                this.startActivity(intent)
            }

        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, CheckEmailActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun reload() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }

}