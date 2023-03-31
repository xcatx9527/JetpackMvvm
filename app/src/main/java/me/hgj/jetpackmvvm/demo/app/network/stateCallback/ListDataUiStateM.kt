package me.hgj.jetpackmvvm.demo.app.network.stateCallback

/**
 * 作者　: chenyang
 * 时间　: 2023/03/31
 * 描述　: 列表数据状态类
 */
data class ListDataUiStateM<T>(
    //是否请求成功
    val isSuccess: Boolean,
    //错误消息 isSuccess为false才会有
    val errMessage: String = "",
    //是否为刷新
    val isRefresh: Boolean = false,
    //是否为空
    val isEmpty: Boolean = false,
    //是否还有更多
    val hasMore: Boolean = false,
    //是第一页且没有数据
    val isFirstEmpty:Boolean = false,
    //列表数据
    val listData: MutableList<T> = mutableListOf()
    )