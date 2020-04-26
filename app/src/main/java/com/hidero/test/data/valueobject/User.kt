package com.hidero.test.data.valueobject

class User {
    var id: String = ""
    var username: String = ""
    var photoUrl: String = ""
    lateinit var status: String
    lateinit var search: String

    constructor(
        id: String,
        username: String,
        photoURL: String,
        status: String,
        search: String
    ) {
        this.id = id
        this.username = username
        this.photoUrl = photoURL
        this.status = status
        this.search = search
    }

    constructor()

}
