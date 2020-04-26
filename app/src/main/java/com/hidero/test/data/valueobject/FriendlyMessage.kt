package com.hidero.test.data.valueobject

class FriendlyMessage(){
    var id: String? = null
    lateinit var sender: String
    lateinit var receiver: String
    var message: String? = null
    var imageUrl: String? = null
    var seen: Boolean = false
    constructor(sender: String, receiver: String, message: String?, imageUrl: String?, seen: Boolean): this(){
        this.sender = sender
        this.receiver = receiver
        this.message = message
        this.imageUrl = imageUrl
        this.seen = seen
    }
}