package com.common.library.dialog

/**
 * Created by BOBOZHU on 2022/6/18 15:08
 * Supporte By BOBOZHU
 */
interface AttachFormatter<T> {

    fun formatItem(item: T):String

    fun isSameItem(item: T, item1: T): Boolean
}