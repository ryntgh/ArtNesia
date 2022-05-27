package com.bangkit.artnesia.data.model

data class LiteratureModel (
    var image: String = "",
    var name: String = "",
    var origin: String = "",
    var description: String = ""
)

data class ArticleModel (
    var image: String = "",
    var name: String = "",
    var description: String = ""
)

data class CategoryModel (
    var categoryName: String = ""
)