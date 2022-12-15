package idat.edu.pe.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import idat.edu.pe.databinding.ActivityCheckEmailBinding

class CheckEmailActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCheckEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Initialize Firebase Auth
        auth = Firebase.auth

        val user = auth.currentUser
        binding.btnIrHome.setOnClickListener{
            val profileUpdates = userProfileChangeRequest {  }
            user!!.updateProfile(profileUpdates).addOnCompleteListener{ tast ->
                if (tast.isSuccessful){
                    if  (user.isEmailVerified){
                        val intent = Intent(this, MainActivity::class.java)
                        this.startActivity(intent)
                    } else {
                        Toast.makeText(baseContext, "Por favor, verifique su correo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.imgSalir.setOnClickListener {
            signOut()
        }

    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if (currentUser.isEmailVerified){
                val intent = Intent(this, MainActivity::class.java)
                this.startActivity(intent)
            } else {
                sendEmailVerification()
            }


        }
    }

    private fun sendEmailVerification(){
        val user = auth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener (this) { task ->
            if (task.isSuccessful){
                Toast.makeText(baseContext, "Se envio un correo de verificacion", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun signOut (){
        Firebase.auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}