package idat.edu.pe.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import idat.edu.pe.R
import idat.edu.pe.db.DataBase
import idat.edu.pe.models.ItemCard
import idat.edu.pe.models.Product
import org.json.JSONObject

class CardAdpater(val productos: ArrayList<ItemCard>) : RecyclerView.Adapter<CardAdpater.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdpater.ViewHolder {
        var vista = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    override fun onBindViewHolder(holder: CardAdpater.ViewHolder, position: Int) {
        holder.bindItems(productos[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

        }

        fun bindItems(producto: ItemCard) {

            val itemNombre: TextView = itemView.findViewById<TextView>(R.id.txtNombreCard)
            val itemprecio: TextView = itemView.findViewById<TextView>(R.id.txtPrecioCard)
            val itemdescription: TextView = itemView.findViewById<TextView>(R.id.txtDesCard)
            val imagen: ImageView = itemView.findViewById<ImageView>(R.id.imgCard)
            val cantidad = itemView.findViewById<EditText>(R.id.txtCantidadCard)
           cantidad.setText(producto.cantidad)
            recargar(itemView.context, producto.id, itemNombre,itemprecio, itemdescription,imagen)
        }

        fun recargar(contexto: Context, id:String, nombrever:TextView, preciover:TextView, descripcionver:TextView , imagen: ImageView) {
            var db = DataBase()
            var url = db.ipServer + "productos/" + id

            var jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { item: JSONObject ->
                   var producto = Product(
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
                    cargarImagen(producto.imagen, imagen,contexto)
                }, Response.ErrorListener {

                });
            val queue = Volley.newRequestQueue(contexto)
            queue.add(jsonObjectRequest)
        }

        fun cargarImagen(nombre: String, imgver: ImageView, contexto: Context) {
            var db = DataBase()
            var url = db.ipImg + "productos/" + nombre;
            val queue: RequestQueue = Volley.newRequestQueue(contexto)
            var imageRequest = ImageRequest(url, Response.Listener { respuesta ->
                imgver.setImageBitmap(respuesta)
            }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888,
                Response.ErrorListener {

                })
            queue.add(imageRequest)
        }
    }
}