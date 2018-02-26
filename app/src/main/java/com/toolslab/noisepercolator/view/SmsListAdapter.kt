package com.toolslab.noisepercolator.view


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toolslab.noisepercolator.R

class SmsListAdapter(private val messages: List<Message>) : RecyclerView.Adapter<SmsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_of_list_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_of_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.textView.text =
                "From " + message.address + "\n" +
                "Body " + message.body + "\n" +
                "Date " + message.date + "\n" +
                "Debuginfo " + message.debugInfo
    }

    override fun getItemCount(): Int {
        return messages.size
    }

}
