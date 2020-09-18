package com.example.todomanager

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ToDo : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var title: String = ""
    var detail: String = ""
    var limit_date: Date = Date()
    var status: String = ""
}