package com.example.pdm.controlador.controladorPrincipal.adaptador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.R
import com.example.pdm.modelos.AdminUsuario
import kotlinx.android.synthetic.main.componente_contacto_admin.view.*

class AdaptadorComponenteAdmin(
    private val contexto: Context,
    private val listaUsuarios: ArrayList<AdminUsuario> = ArrayList()

): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    // Nos permite poner listener a cada componente del recyclerView
    var onItemClick: ((AdminUsuario) -> Unit)? = null

    // Nos permite agregar un listener al boton de eliminar usuario.
    var onEliminarUsuario: ((AdminUsuario) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Infla el componente, de esta forma poder realizar operaciones en esta.
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.componente_contacto_admin,
            parent,
            false
        )

        // Instanciamos el holder.
        val holder = ViewHolderUsuario(view)

        return holder
    }

    // Enlaza los items en la lista con un modelo o valor.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Consulta el elemento en una posicion dada.
        val item = listaUsuarios[position]

        // Establecemos valores de los widgets de la vista.
    }

    // retorna el conteo de items en la vista.
    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

    // Subclase que mantiene el map de todos los elementos en la vista.
    inner class ViewHolderUsuario(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Instanciamos los widgets que se usaran en el viewHolder.
        val textViewMensaje: TextView = itemView.findViewById(R.id.textViewMensaje)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(listaUsuarios[adapterPosition])
            }

            itemView.botonBorrarContacto.setOnClickListener {
                onEliminarUsuario?.invoke(listaUsuarios[adapterPosition])
            }
        }
    }
}