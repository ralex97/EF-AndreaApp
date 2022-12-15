package idat.edu.pe.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import idat.edu.pe.R
import idat.edu.pe.databinding.ActivityLoginBinding
import idat.edu.pe.utility.AppMessage
import idat.edu.pe.utility.MessageType
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var iniciar: Button
    private lateinit var switch: Switch
    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var registro: TextView
    private lateinit var olvidasteCon: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        switch = findViewById(R.id.swCambio)
        email = findViewById(R.id.txtEmail)
        password = findViewById(R.id.txtPassword)
        iniciar = findViewById(R.id.btnSignIn)
        registro =  findViewById(R.id.txtIrRegister)
        olvidasteCon = findViewById(R.id.txtOContra)

        auth = Firebase.auth

        binding.txtOContra.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AccountRecoveryActivity::class.java
                )
            )
        }

        binding.txtIrRegister.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }

        binding.btnSignIn.setOnClickListener {
            val fEmail = binding.txtEmail.text.toString()
            val fPassword = binding.txtPassword.text.toString()

            when {
                fEmail.isEmpty() || fPassword.isEmpty() -> {
                    loginVAlidation()
                }
                else -> {
                    SignIn(fEmail, fPassword)
                }
            }
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this@LoginActivity, "CAMBIO A ", Toast.LENGTH_SHORT).show()
                actulizarResource("en")
            } else {
                 Toast.makeText(this@LoginActivity, "CHANGE TO ES", Toast.LENGTH_SHORT).show()
                actulizarResource("es")
            }
        }

    }

    fun actulizarResource(idioma: String) {
        val recursos = resources
        val displayMetrics = recursos.displayMetrics
        val configuration = resources.configuration
        configuration.setLocale(Locale(idioma))
        recursos.updateConfiguration(configuration, displayMetrics)
        configuration.locale = Locale(idioma)
        resources.updateConfiguration(configuration, displayMetrics)

        switch.text = recursos.getString(R.string.cambio)
        email.text = recursos.getString(R.string.textemail)
        password.text = recursos.getString(R.string.textpassword)
        registro.text = recursos.getString(R.string.textCuenta)
        olvidasteCon.text = recursos.getString(R.string.textOlvidasteContra)
        iniciar.text = recursos.getString(R.string.btnIniciarSesion)

    }

    // funcion para entrar al sistema  o iniciar session con el correo y la contraseña
    private fun SignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    AppMessage.SendMessage(binding.root, "Bienvenido", MessageType.ERROR)
                    reload()
                } else {
                    limpiarCampos()
                    AppMessage.SendMessage(
                        binding.root,
                        getString(R.string.alertLogin),
                        MessageType.ERROR
                    )
                }
            }
    }


    fun loginVAlidation(): Boolean {
        var respuesta = true
        if (binding.txtEmail.text.toString().trim().isEmpty()) {
            binding.txtEmail.isFocusableInTouchMode = true
            binding.txtEmail.requestFocus()
            respuesta = false
            binding.txtEmail.error = getString(R.string.errorEmail)
        } else if (!verificarFormatoCorreo()) {
            binding.txtEmail.error = getString(R.string.errorInvalidCorreo)
            binding.txtEmail.isFocusableInTouchMode = true
            binding.txtEmail.requestFocus()
            respuesta = false

        } else if (binding.txtPassword.text.toString().trim().isEmpty()) {
            binding.txtPassword.isFocusableInTouchMode = true
            binding.txtPassword.requestFocus()
            respuesta = false
            binding.txtPassword.error = getString(R.string.errorPassword)
        }

        return respuesta
    }

    private fun verificarFormatoCorreo(): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(binding.txtEmail.text.toString().trim()).matches()
    }

    // para cuando el usuario entra al sistema y pone el programa en segundo plano evita que  vuelva a entrar
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

    private fun limpiarCampos() {
        binding.txtEmail.setText("")
        binding.txtPassword.setText("")
        binding.txtEmail.isFocusableInTouchMode = true
        binding.txtEmail.requestFocus()

    }

    private fun reload() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }

}