package com.hidero.test.data.valueobject

import android.text.TextUtils
import android.util.Patterns
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class Account private constructor() {
    private object Holder { val INSTANCE = Account() }
    companion object {
        val instance: Account by lazy { Holder.INSTANCE }
    }

    constructor(username: String?, password: String?) : this() {
        this.username = username
        this.password = password
    }

    constructor(
        username: String?,
        password: String?,
        role: Int?,
        name: String?,
        address: String?,
        phone: String,
        email: String?,
        photoUrl: String?
    ) : this() {
        this.username = username
        this.password = password
        this.role = role
        this.name = name
        this.address = address
        this.phone = phone
        this.email = email
        this.photoUrl = photoUrl
    }

    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
    @SerializedName("role")
    @Expose
    var role: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("photoUrl")
    @Expose
    var photoUrl: String? = null
    @SerializedName("gender")
    @Expose
    var gender: String? = null
    @SerializedName("dob")
    @Expose
    var dob: Date? = null
    @SerializedName("bankAcc")
    @Expose
    var bankAcc: String? = null

    fun isValidData(): Int {
        return when{
            (TextUtils.isEmpty(username))-> 0
            (TextUtils.isEmpty(password)) -> 1
            (!Patterns.PHONE.matcher(phone as CharSequence).matches()) -> 2
            (!Patterns.EMAIL_ADDRESS.matcher(email as CharSequence).matches()) -> 3
            (password!!.length < 3) -> 4
            else -> -1
        }
    }

}