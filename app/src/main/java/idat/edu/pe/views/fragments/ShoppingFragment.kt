package idat.edu.pe.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import idat.edu.pe.R
import idat.edu.pe.adapters.ProductAdapter
import idat.edu.pe.db.DataBase
import idat.edu.pe.models.Product
import org.json.JSONArray


class ShoppingFragment : Fragment() {
    var productos = ArrayList<Product>()
    private var recyclerView: RecyclerView? = null;



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_shopping, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.listaCarrito)
        recyclerView?.layoutManager = GridLayoutManager(view.context, 2)
        recargar(view.context)

        return view
    }
    fun recargar(contexto: Context) {
        var db = DataBase()
        var url = db.ipServer + "productos"

        var jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { respuesta: JSONArray ->
                for (i in 0 until respuesta.length()) {
                    val item = respuesta.getJSONObject(i)
                    productos.add(Product(
                        item.getString("id"),
                        item.getString("name"),
                        item.getString("price"),
                        item.getString("imagen"),
                        item.getString("description"),
                        item.getString("id_category")
                    )
                    )
                }
                val adapter = ProductAdapter(productos)
                recyclerView?.adapter = adapter
            }, Response.ErrorListener {

            });
        val queue = Volley.newRequestQueue(contexto)
        queue.add(jsonObjectRequest)
    }



}