package com.example.todomanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_to_do_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ToDoEditActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_edit)
        realm = Realm.getDefaultInstance()

        save.setOnClickListener{
            realm.executeTransaction{
                val maxId = realm.where<ToDo>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1
                val todos = realm.createObject<ToDo>(nextId)
                todos.title = titleEdit.text.toString()
                todos.detail = detailEdit.text.toString()
                limitDateEdit.text.toString().toDate("yyyy/MM/dd")?.let{
                    todos.limit_date = it
                }
                todos.status = statusView.text.toString()
            }
            alert("追加しました"){
                yesButton { finish() }
            }.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date? {
        val sdFormat = try{
            SimpleDateFormat(pattern)
        } catch (e: IllegalArgumentException){
            null
        }
        val date = sdFormat?.let {
            try{
                it.parse(this)
            } catch (e: ParseException){
                null
            }
        }
        return date
    }
}