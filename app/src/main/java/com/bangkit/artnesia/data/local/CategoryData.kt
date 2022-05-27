package com.bangkit.artnesia.data.local

import com.bangkit.artnesia.data.model.CategoryModel

object CategoryData {
    fun generateCategory(): ArrayList<CategoryModel> {
        val category = ArrayList<CategoryModel>()

        category.add(
            CategoryModel(
                "Mask"
            )
        )

        category.add(
            CategoryModel(
                "Wearable"
            )
        )

        category.add(
            CategoryModel(
                "Statue"
            )
        )

        category.add(
            CategoryModel(
                "Music"
            )
        )

        category.add(
            CategoryModel(
                "Tools"
            )
        )

        category.add(
            CategoryModel(
                "Ceremony"
            )
        )
        return category
    }

}