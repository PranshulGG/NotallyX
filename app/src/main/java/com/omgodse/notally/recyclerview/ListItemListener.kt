package com.omgodse.notally.recyclerview

interface ListItemListener {

    fun delete(position: Int, force: Boolean)

    fun moveToNext(position: Int)

    fun add(position: Int)

    fun textChanged(position: Int, text: String)

    fun checkedChanged(position: Int, checked: Boolean)

    fun isChildItemChanged(position: Int, isChildItem: Boolean)
}