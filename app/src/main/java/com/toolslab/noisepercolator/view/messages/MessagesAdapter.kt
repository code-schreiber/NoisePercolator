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
        internal val address: TextView = view.findViewById(R.id.item_message_address)
        internal val date: TextView = view.findViewById(R.id.item_message_date)
        internal val body: TextView = view.findViewById(R.id.item_message_body)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val message = messages[position]
        viewHolder.address.text = message.address
        viewHolder.date.text = message.getFormattedDate()
        viewHolder.body.text = message.body
    }

    override fun getItemCount(): Int {
        return messages.size
    }

}
