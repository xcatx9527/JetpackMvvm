package me.hgj.jetpackmvvm.demo.data.model.mongo

import org.bson.types.ObjectId

data class AppListBean(
    val _id: ObjectId,
    val appName: String,
    val app_id: Int,
    val checked: Int,
    val downLoadState: Int,
    val fileName: String,
    val icon_url: String,
    val market: String,
    val packageName: String
)