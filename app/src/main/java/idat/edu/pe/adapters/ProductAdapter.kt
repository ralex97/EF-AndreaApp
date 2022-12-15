package idat.edu.pe.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import idat.edu.pe.R
import idat.edu.pe.db.DataBase
import idat.edu.pe.models.Product
import idat.edu.pe.views.SeeProductsActivity

class ProductAdapter(val productos: ArrayList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    var position: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        var vista =
            LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        this.position = position
        holder.bindItems(productos[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

        }

        fun bindItems(producto: Product) {
            itemView.setOnClickListener {
                var intent= Intent(itemView.context, SeeProductsActivity::class.java)
                intent.putExtra("Id",producto.id)
                itemView.context.startActivity(intent)
            }
            val itemNombre: TextView = itemView.findViewById<TextView>(R.id.item_nombre)
            itemNombre.text = producto.nombre

            val itemprecio: TextView = itemView.findViewById<TextView>(R.id.item_precio)
            itemprecio.text ="S/ "+producto.precio

            val itemdescription: TextView = itemView.findViewById<TextView>(R.id.item_description)
            itemdescription.text = producto.descripcion

            val imagen: ImageView = itemView.findViewById<ImageView>(R.id.item_img)
            var db = DataBase()
            var url = db.ipImg + "productos/" + producto.imagen;
            val queue:RequestQueue = Volley.newRequestQueue(itemView.context)
            var imageRequest = ImageRequest(url, Response.Listener { respuesta ->
                imagen.setImageBitmap(respuesta)
            },0,0,ImageView.ScaleType.FIT_XY,Bitmap.Config.ARGB_8888,
                Response.ErrorListener {

            })
            queue.add(imageRequest)
        }
    }

}