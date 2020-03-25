package com.hidero.test.data.valueobject

import android.text.TextUtils
import android.util.Patterns
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Account private constructor() {

    companion object{
        private var account: Account? = null

        fun getInstance(): Account {
            if (account == null){
                account =
                    Account()
            }
            return account!!
        }
    }

    constructor(username: String?, password: String?): this(){
        this.username = username
        this.password = password
    }

    constructor(username: String?, password: String?, role: Int?, fullname: String?, address: String?, phone: String, email: String?) : this() {
        this.username = username
        this.password = password
        this.role = role
        this.fullname = fullname
        this.address = address
        this.phone = phone
        this.email = email
    }

    @SerializedName("username")
    @Expose
    internal var username: String? = null
    @SerializedName("password")
    @Expose
    internal var password: String? =null
    @SerializedName("role")
    @Expose
    internal var role: Int? = null
    @SerializedName("fullname")
    @Expose
    internal var fullname: String? =null
    @SerializedName("address")
    @Expose
    internal var address: String? =null
    @SerializedName("phone")
    @Expose
    internal var phone: String? =null
    @SerializedName("email")
    @Expose
    var email: String? =null

    fun isValidData(): Int {
        return if (TextUtils.isEmpty(username))
            0
        else if (TextUtils.isEmpty(password))
            1
        else if (!Patterns.PHONE.matcher(phone).matches())
            2
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            3
        else if (password!!.length < 8)
            4
        else
            -1
    }

}