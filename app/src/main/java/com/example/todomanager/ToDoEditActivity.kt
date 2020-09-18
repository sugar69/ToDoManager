package com.example.todomanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateFormat.format
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_to_do_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.lang.IllegalArgumentException
//import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.widget.RadioButton


class ToDoEditActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_edit)
        realm = Realm.getDefaultInstance()

        val todoId = intent?.getLongExtra("todo_id", -1L)
        if (todoId != -1L){
            val todos = realm.where<ToDo>()
                .equalTo("id", todoId).findFirst()
            titleEdit.setText(todos?.title)
            detailEdit.setText(todos?.detail)
            limitDateEdit.setText(DateFormat.format("yyyy/MM/dd", todos?.limit_date))
            statusView.setText(todos?.status)
            delete.visibility = View.VISIBLE
        }else{
            delete.visibility = View.INVISIBLE
        }

        radioGroup.setOnCheckedChangeListener {
            group, checkedId ->
            statusView.text = findViewById<RadioButton>(checkedId).text
        }

        //  保存ボタンの処理
        save.setOnClickListener{
            when(todoId){
                //  追加処理
                -1L -> {
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
                //  更新処理
                else -> {
                    realm.executeTransaction{
                        val todos = realm.where<ToDo>()
                            .equalTo("id", todoId).findFirst()
                        todos?.title = titleEdit.text.toString()
                        todos?.detail = detailEdit.text.toString()
                        limitDateEdit.text.toString().toDate("yyyy/MM/dd")?.let{
                            todos?.limit_date = it
                        }
                        todos?.status = statusView.text.toString()
                        alert ("修正しました") {
                            yesButton { finish() }
                        }.show()
                    }
                }
            }
        }

        //  削除ボタンの処理
        delete.setOnClickListener {
            realm.executeTransaction{
                realm.where<ToDo>().equalTo("id", todoId)
                    ?.findFirst()
                    ?.deleteFromRealm()
            }
            alert ("削除しました") {
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