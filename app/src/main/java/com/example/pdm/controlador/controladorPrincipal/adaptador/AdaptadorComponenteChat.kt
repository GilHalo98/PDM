package com.example.pdm.controlador.controladorPrincipal.adaptador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.R
import com.example.pdm.modelos.MensajeChat

class AdaptadorComponenteChat(
    private val contexto: Context,
    private val listaMensajes: ArrayList<MensajeChat> = ArrayList()
): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    // Nos permite poner listener a cada componente del recyclerView
    var onItemClick: ((MensajeChat) -> Unit)? = null

    // Creamos la enumeracion para los tipos de mensajes que un cliente puede recibir.
    companion object {
        const val MENSAJE_RECIBIDO = 1
        const val MENSAJE_ENVIADO = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Infla el componente, de esta forma poder realizar operaciones en esta.
        val view = inflarComponenteMensaje(parent, viewType)

        // Instanciamos el ViewHolder.
        val holder = if (viewType == MENSAJE_RECIBIDO) {
            ViewHolderMensajeRecibido(view)
        } else {
            ViewHolderMensajeEnviado(view)
        }

        return holder
    }

    // Enlaza los items en la lista con un modelo o valor.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Consulta el elemento en una posicion dada.
        val item = listaMensajes[position]

        if (holder is ViewHolderMensajeEnviado) {
            // sets the text to the textview from our itemHolder class
            holder.textViewMensaje.text = item.mensaje

        } else if (holder is ViewHolderMensajeRecibido) {
            // sets the text to the textview from our itemHolder class
            holder.textViewMensaje.text = item.mensaje
        }
    }

    // retorna el conteo de items en la vista.
    override fun getItemCount(): Int {
        return listaMensajes.size
    }

    // Retorna el tipo de viewHolder.
    override fun getItemViewType(position: Int): Int {
        return listaMensajes[position].tipo
    }

    private fun inflarComponenteMensaje(parent: ViewGroup, tipo: Int): View {
        val vista = if (tipo == MENSAJE_RECIBIDO) {
            LayoutInflater.from(parent.context).inflate(
                R.layout.componente_item_chat_recibido,
                parent,
                false
            )
        } else {
            LayoutInflater.from(parent.context).inflate(
                R.layout.componente_item_chat_enviado,
                parent,
                false
            )
        }

        return vista
    }

    fun agregarMensajeEnviado(mensaje: String) {
        val nuevoMensaje = MensajeChat(mensaje, MENSAJE_ENVIADO)
        listaMensajes.add(nuevoMensaje)
    }

    fun agregarMensajeRecibido(mensaje: String) {
        val nuevoMensaje = MensajeChat(mensaje, MENSAJE_RECIBIDO)
        listaMensajes.add(nuevoMensaje)
    }

    // Subclase que mantiene el map de todos los elementos en la vista.
    inner class ViewHolderMensajeRecibido(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMensaje: TextView = itemView.findViewById(R.id.textViewMensaje)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(listaMensajes[adapterPosition])
            }
        }
    }

    // Subclase que mantiene el map de todos los elementos en la vista.
    inner class ViewHolderMensajeEnviado(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMensaje: TextView = itemView.findViewById(R.id.textViewMensaje)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(listaMensajes[adapterPosition])
            }
        }
    }
}