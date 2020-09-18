package com.example.todomanager

import android.text.format.DateFormat.format
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import java.text.DateFormat

class ToDoAdapter(data: OrderedRealmCollection<ToDo>?) : RealmBaseAdapter<ToDo>(data) {

    inner class ViewHolder(cell: View){
        val title = cell.findViewById<TextView>(android.R.id.text1)
        val status = cell.findViewById<TextView>(android.R.id.text2)
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        when(convertView){
            null -> {
                val inflater = LayoutInflater.from(parent?.context)
                view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            }
            else -> {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }
        }

        adapterData?.run{
            val todos = get(position)
            viewHolder.title.text = todos.title
            viewHolder.status.text = todos.status
        }
        return view
    }
}