package com.hidero.test.ui.notifications

class Data(

) {
    var user: String? = null
    var icon: Int = 0
     var body: String? = null
     var title: String? = null
     var sent: String? = null
    constructor(user: String, icon: Int, body: String, title: String, sent: String) : this() {
        this.user = user
        this.icon = icon
        this.body = body
        this.title = title
        this.sent = sent
    }
}

    