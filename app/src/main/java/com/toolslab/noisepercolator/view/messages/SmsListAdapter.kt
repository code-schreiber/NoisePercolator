package com.toolslab.noisepercolator.view.messages


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.model.Message


// TODO show only blocked messages and nice empty screen if none
// TODO rename to MessagesAdapter
class SmsListAdapter(private val messages: List<Message>) : RecyclerView.Adapter<SmsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_of_list_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_of_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.textView.text =
                "Spam " + message.spam + "\n" +
                "From " + message.address + "\n" +
                "Body " + message.body + "\n" +
                "Date " + message.date + "\n" +
                "Debuginfo\n\t" + message.debugInfo.replace(";", "\n\t")
    }

    override fun getItemCount(): Int {
        return messages.size
    }

}
