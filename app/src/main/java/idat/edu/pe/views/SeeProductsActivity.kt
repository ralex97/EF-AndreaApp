package idat.edu.pe.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import idat.edu.pe.R
import idat.edu.pe.db.Conexion
import idat.edu.pe.db.DataBase
import idat.edu.pe.models.Product
import org.json.JSONObject

class SeeProductsActivity : AppCompatActivity() {
    lateinit var btnMas: Button
    lateinit var btnMenos: Button
    lateinit var txtCantidad: TextView
    lateinit var btnAgregar: Button
    lateinit var imgver: ImageView
    lateinit var nombrever: TextView
    lateinit var preciover: TextView
    lateinit var descripcionver: TextView
    lateinit var producto: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_products)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var id = intent.getStringExtra("Id")

        imgver = findViewById(R.id.imgVer)
        nombrever = findViewById(R.id.item_nombreVer)
        descripcionver = findViewById(R.id.item_descriptionVer)
        preciover = findViewById(R.id.item_precioVer)
        btnMas = findViewById(R.id.btnMas)
        btnMenos = findViewById(R.id.btnMenos)
        btnAgregar = findViewById(R.id.btnAgregar)
        txtCantidad = findViewById(R.id.txtCantidad)
        if (id != null) {
            recargar(this, id)
        }

        txtCantidad.text = "1"
        btnMas.setOnClickListener {
            txtCantidad.text = (txtCantidad.text.toString().toInt() + 1).toString()

        }
        btnMenos.setOnClickListener {
            if (txtCantidad.text.toString().toInt() > 1) {
                txtCantidad.text = (txtCantidad.text.toString().toInt() - 1).toString()
            }
        }
        var conexion = Conexion(this)
        var db = conexion.writableDatabase
        btnAgregar.setOnClickListener {
            db.execSQL("insert into card (id_producto, cantidad) values(" + producto.id + "," + txtCantidad.text.toString() + ")")
            startActivity(Intent(this, CardActivity::class.java))
        }

    }

    fun recargar(contexto: Context, id: String) {
        var db = DataBase()
        var url = db.ipServer + "productos/" + id

        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { item: JSONObject ->
                producto = Product(
                    item.getString("id"),
                    item.getString("name"),
                    item.getString("price"),
                    item.getString("imagen"),
                    item.getString("description"),
                    item.getString("id_category")
                )
                nombrever.text = producto.nombre
                preciover.text = "S/ " + producto.precio
                descripcionver.text = producto.descripcion
                cargarImagen(producto.imagen)
                findViewById<TextView>(R.id.toolbarTitle).text = producto.nombre
            }, Response.ErrorListener {

            });
        val queue = Volley.newRequestQueue(contexto)
        queue.add(jsonObjectRequest)
    }

    fun cargarImagen(nombre: String) {
        var db = DataBase()
        var url = db.ipImg + "productos/" + nombre;
        val queue: RequestQueue = Volley.newRequestQueue(this)
        var imageRequest = ImageRequest(url, Response.Listener { respuesta ->
            imgver.setImageBitmap(respuesta)
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888,
            Response.ErrorListener {

            })
        queue.add(imageRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)

    }
}