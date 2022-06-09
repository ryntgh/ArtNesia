package com.bangkit.artnesia.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    var image: String = "",
    var name: String = "",
    var description: String = "",
    var article_id: String = ""
): Parcelable