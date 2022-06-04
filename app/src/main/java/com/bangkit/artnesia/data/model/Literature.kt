package com.bangkit.artnesia.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Literature(
    var image: String = "",
    var name: String = "",
    var origin: String = "",
    var classification: String = "",
    var description: String = "",
    var literature_id: String = ""
): Parcelable
