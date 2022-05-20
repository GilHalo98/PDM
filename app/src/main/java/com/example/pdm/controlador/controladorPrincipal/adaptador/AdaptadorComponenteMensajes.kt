package com.example.pdm.controlador.controladorPrincipal.adaptador

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.R
import com.example.pdm.modelos.Contacto
import kotlinx.android.synthetic.main.componente_usuario_no_agregado.view.*

class AdaptadorComponenteMensajes(
    private val contexto: Context,
    private var listaContactos: ArrayList<Contacto> = ArrayList()

): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // Nos permite poner listener a cada componente del recyclerView
    var onItemClick: ((Contacto) -> Unit)? = null

    // Nos permite poner un listener a usuario que no se encuentre en la lista de contactos del
    // recyclerView.
    var onAddContacto: ((Contacto) -> Unit)? = null

    // Creamos la enumeracion para los tipos de contactos, agregados y no agregados.
    companion object {
        const val CONTACTO_AGREGADO = 1
        const val CONTACTO_NO_AGREGADO = 2
    }

    // Al crear una nueva vista.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Infla el componente, de esta forma poder realizar operaciones en esta.
        val view = inflarComponenteContacto(parent, viewType)

        // Instanciamos el ViewHolder.
        val holder = if (viewType == CONTACTO_AGREGADO) {
            ViewHolderContacto(view)
        } else {
            ViewHolderNoAgregado(view)
        }

        return holder
    }

    // Enlaza los items en la lista con un modelo.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Consulta el elemento en una posicion dada.
        val contacto = listaContactos[position]

        if (holder is ViewHolderContacto) {
            // sets the text to the textview from our itemHolder class
            holder.textViewUsername.text = contacto.nombreUsuario

        } else if (holder is ViewHolderNoAgregado) {
            // sets the text to the textview from our itemHolder class
            holder.textViewUsername.text = contacto.nombreUsuario
        }
    }

    // retorna el conteo de items en la vista.
    override fun getItemCount(): Int {
        return listaContactos.size
    }

    // Retorna el tipo de viewHolder.
    override fun getItemViewType(position: Int): Int {
        return listaContactos[position].tipo
    }

    private fun inflarComponenteContacto(parent: ViewGroup, tipo: Int): View {
        val vista = if (tipo == CONTACTO_AGREGADO) {
            LayoutInflater.from(parent.context).inflate(
                R.layout.componente_contacto,
                parent,
                false
            )
        } else {
            LayoutInflater.from(parent.context).inflate(
                R.layout.componente_usuario_no_agregado,
                parent,
                false
            )
        }

        return vista
    }

    // Agrega un contacto listado del usuario a la vista.
    fun establcerListaContactos(lista: List<Contacto>) {
        Log.d("lista", lista.toString())
        listaContactos = lista as ArrayList<Contacto>
    }

    // Subclase que mantiene el mapa de los contactos que el usuario tiene en la lista.
    inner class ViewHolderContacto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUsername: TextView = itemView.findViewById(R.id.textViewUsername)
        val textViewUltimoMensaje: TextView = itemView.findViewById(R.id.textViewUltimoMensaje)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(listaContactos[adapterPosition])
            }
        }
    }

    // Subclase que mantiene el mapa de los contactos que el usuario no tiene en la lista.
    inner class ViewHolderNoAgregado(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUsername: TextView = itemView.findViewById(R.id.textViewUsername)
        val textViewUltimoMensaje: TextView = itemView.findViewById(R.id.textViewUltimoMensaje)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(listaContactos[adapterPosition])
            }

            itemView.botonAgregarContacto.setOnClickListener {
                onAddContacto?.invoke(listaContactos[adapterPosition])
            }
        }
    }
}