package com.example.mychatapp.notifications

class Data(

) {
    lateinit var user: String
    var icon: Int = 0
    lateinit var body: String
    lateinit var title: String
    lateinit var sent: String
    constructor(user: String, icon: Int, body: String, title: String, sent: String) : this() {
        this.user = user
        this.icon = icon
        this.body = body
        this.title = title
        this.sent = sent
    }
}

    