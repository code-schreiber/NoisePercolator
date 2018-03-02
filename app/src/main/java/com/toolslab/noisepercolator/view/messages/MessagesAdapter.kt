package com.toolslab.noisepercolator.view.messages


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.model.Message

class MessagesAdapter(private val messages: List<Message>) : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val address: TextView = view.findViewById(R.id.item_message_address)
        val date: TextView = view.findViewById(R.id.item_message_date)
        val body: TextView = view.findViewById(R.id.item_message_body)
        val spam: View = view.findViewById(R.id.item_message_spam)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.address.text = message.address
        holder.date.text = message.date
        holder.body.text = message.body + "\n" + "Debuginfo\n\t" + message.debugInfo.replace(";", "\n\t")
        if (message.spam) {
            holder.spam.visibility = View.VISIBLE
        } else {
            holder.spam.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

}
