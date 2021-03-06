package com.example.todomanager

import android.content.pm.ActivityInfo
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        realm = Realm.getDefaultInstance()
        val todos = realm.where<ToDo>().findAll()
        listView.adapter = ToDoAdapter(todos)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            startActivity<ToDoEditActivity>()
        }

        listView.setOnItemClickListener{ parent, view, position, id ->
            val todos = parent.getItemAtPosition(position) as ToDo
            startActivity<ToDoEditActivity>(
                "todo_id" to todos.id
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}