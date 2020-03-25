package com.hidero.test.data.valueobject

data class Book(
    var idBook: Int,
    var nameBook: String?,
    var quantity: Int,
    var cost: Int,
    var imageBook: String?,
    var description: String?,
    var idLanguage: Int?,
    var idAuthor: Int,
    var idSpecies: Int?
) 