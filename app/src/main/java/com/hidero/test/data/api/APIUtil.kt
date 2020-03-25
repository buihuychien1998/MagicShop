package com.hidero.test.data.api

object APIUtil {
    //    Làm trung gian gửi phương thức lên và nhận dữ liệu về chứa trong APIService
    val getData = { url: String ->
        RetrofitClient.getClient(url).create(APIService::class.java)
    }
}