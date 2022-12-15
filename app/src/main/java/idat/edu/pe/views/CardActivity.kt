package idat.edu.pe.views

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import idat.edu.pe.R
import idat.edu.pe.adapters.CardAdpater
import idat.edu.pe.databinding.ActivityCardBinding
import idat.edu.pe.databinding.ActivityLoginBinding
import idat.edu.pe.databinding.ActivityMainBinding
import idat.edu.pe.db.Conexion
import idat.edu.pe.models.ItemCard

class CardActivity : AppCompatActivity() {

lateinit var binding: ActivityCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var lista = findViewById<RecyclerView>(R.id.listaCarrito)
        var listCard = ArrayList<ItemCard>()
        var conexion = Conexion(this)
        var db = conexion.writableDatabase
        var sql = "select * from card"
        var respuesta = db.rawQuery(sql,null)
        if(respuesta.moveToFirst()){
            do{
                listCard.add(ItemCard(respuesta.getString(1),"","","","","",respuesta.getString(2)))
            }while(respuesta.moveToNext())
        }

        var adapter = CardAdpater(listCard)
        lista.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lista.adapter= adapter

//        binding.btnEnviar.setOnClickListener(){
//enviarPedido()
//        }
    }
//private fun enviarPedido(m:String) {
//    val intent = Intent(Intent.ACTION_VIEW)
//    Uri.parse(kotlin.String.format("https://api.whatsapp.com/send?phone=%s&text=%s","+5192449893",m))
//}



}