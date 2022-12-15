package idat.edu.pe.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import idat.edu.pe.databinding.ActivityAccountRecoveryBinding

class AccountRecoveryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountRecoveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRecovery.setOnClickListener {
            val emailAddress = binding.txtRecoveryPass.text.toString()
            Firebase.auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val intent = Intent(this, LoginActivity::class.java)
                    this.startActivity(intent)
                } else {
                    Toast.makeText(baseContext, "Ingrese el email de tu cuenta", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}