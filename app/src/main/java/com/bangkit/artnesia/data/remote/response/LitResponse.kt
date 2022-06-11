package com.bangkit.artnesia.data.remote.response

import com.google.gson.annotations.SerializedName

data class LitResponse(

	@field:SerializedName("literature")
	val literature: ArrayList<LiteratureItem>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LiteratureItem(

	@field:SerializedName("literature_id")
	val literatureId: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("origin")
	val origin: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("classification")
	val classification: String? = null
)
