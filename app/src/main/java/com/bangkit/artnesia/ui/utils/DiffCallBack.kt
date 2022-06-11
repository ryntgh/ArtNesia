package com.bangkit.artnesia.ui.utils

import androidx.recyclerview.widget.DiffUtil
import com.bangkit.artnesia.data.remote.response.LiteratureItem

class DiffCallBack(
    private val oldList: ArrayList<LiteratureItem>,
    private val newList: ArrayList<LiteratureItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].literatureId == newList[newItemPosition].literatureId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].image != newList[newItemPosition].image -> false
            oldList[oldItemPosition].name != newList[newItemPosition].name -> false
            oldList[oldItemPosition].description != newList[newItemPosition].description -> false

            else -> true
        }
    }
}