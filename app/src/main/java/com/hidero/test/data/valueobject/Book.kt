package com.hidero.test.data.valueobject

data class Book(
    var bookId: Int,
    var bookName: String?,
    var quantity: Int,
    var cost: Int,
    var bookImage: String?,
    var description: String?,
    var languageId: Int?,
    var authorId: Int,
    var genreId: Int?
) 