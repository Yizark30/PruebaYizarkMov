package ni.edu.uca.listadoprod

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import ni.edu.uca.listadoprod.dataadapter.ProductoAdapter
import ni.edu.uca.listadoprod.databinding.ActivityMainBinding
import ni.edu.uca.listadoprod.dataclass.Producto
import java.lang.Exception
import java.lang.StringBuilder



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var listaProd = ArrayList<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniciar()
  }

    private fun limpiar() {
        with(binding) {
            etID.setText("")
            etNombreProd.setText("")
            etPrecio.setText("")
            etID.requestFocus()
        }
    }


    private fun agregarProd() {
        with(binding) {
            try {
                val id: Int = etID.text.toString().toInt()
                val nombre: String = etNombreProd.text.toString()
                val precio: Double = etPrecio.text.toString().toDouble()
                val prod = Producto(id, nombre, precio)
                listaProd.add(prod)
            } catch (ex: Exception) {
                Toast.makeText(
                    this@MainActivity, "Error: Llenar todos los campos",
                    Toast.LENGTH_LONG
                ).show()
            }
            rcvLista.layoutManager = LinearLayoutManager(this@MainActivity)
            rcvLista.adapter = ProductoAdapter(listaProd,
                {producto -> onItemSelected(producto) },
                {position -> onDeleteItem(position)},
                {position -> onUpdateItem(position)})
            limpiar()
        }
    }

    private fun iniciar() {
        binding.btnAgregar.setOnClickListener {
            agregarProd()
        }
        binding.btnLimpiar.setOnClickListener {
            limpiar()
        }
    }

    fun onItemSelected(producto: Producto) {
        with(binding) {
            etID.text = producto.id.toString().toEditable()
            etNombreProd.text = producto.nombre.toEditable()
            etPrecio.text = producto.precio.toString().toEditable()
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    fun onDeleteItem(position: Int){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage("¿Desea Eliminar El Producto?")
            .setCancelable(false)
            .setPositiveButton("Si"){dialog, id ->
                with(binding){
                    listaProd.removeAt(position)
                    rcvLista.adapter?.notifyItemRemoved(position)
                    limpiar()
                }

            }.setNegativeButton("No"){dialog,id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()

    }
    fun onUpdateItem(position: Int){
        try {

            with(binding){
            val id: Int = etID.text.toString().toInt()
            val nombre: String = etNombreProd.text.toString()
            val precio: Double = etPrecio.text.toString().toDouble()
            val prod = Producto(id, nombre, precio)
            listaProd.set(position, prod)
            rcvLista.adapter?.notifyItemChanged(position)
            limpiar()
            }
        }catch(ex: Exception){
            Toast.makeText(
                this@MainActivity, "Error: Seleccione un Dato",
                Toast.LENGTH_LONG
            ).show()
        }
    }


}
