package com.example.pdm.controlador.controladorPrincipal.adaptador

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.R
import com.example.pdm.modelos.AdminUsuario
import com.example.pdm.modelos.Contacto
import kotlinx.android.synthetic.main.componente_contacto_admin.view.*

class AdaptadorComponenteAdmin(
    private val contexto: Context,
    private var listaUsuarios: ArrayList<AdminUsuario> = ArrayList()

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

        if (holder is ViewHolderUsuario) {
            // Establecemos valores de los widgets de la vista.
            holder.textViewEmail.text = item.correo
            holder.textViewUsername.text = item.nombreUsuario

        }
    }

    // retorna el conteo de items en la vista.
    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

    // Cambia la lista de usuarios.
    fun establcerListaUsuarios(lista: List<AdminUsuario>) {
        listaUsuarios = lista as ArrayList<AdminUsuario>
    }

    // Subclase que mantiene el map de todos los elementos en la vista.
    inner class ViewHolderUsuario(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Instanciamos los widgets que se usaran en el viewHolder.
        val textViewUsername: TextView = itemView.findViewById(R.id.textViewUsername)
        val textViewEmail: TextView = itemView.findViewById(R.id.textViewEmail)

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