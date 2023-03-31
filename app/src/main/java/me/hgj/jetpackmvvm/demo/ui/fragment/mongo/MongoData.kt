package me.hgj.jetpackmvvm.demo.ui.fragment.mongo

import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import me.hgj.jetpackmvvm.demo.data.model.mongo.AppListBean
import org.bson.conversions.Bson
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

val MongoDataCoroutine: MongoRequestManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    MongoRequestManger()
}

class MongoRequestManger {
    private val HOST_IP = "mongodb://192.168.0.202:27017"
    private val client = KMongo.createClient(HOST_IP) //get com.mongodb.MongoClient new instance
    private val database = client.getDatabase("xiaomi")
    val applistcollection = database.getCollection<AppListBean>("all_app_list_single")
    val pageSize = 20

    suspend fun getAppListData(
        page: Int,
        filters: Bson?
    ): MutableList<AppListBean> {
        return withContext(Dispatchers.IO) {
            val listData = async {
                if (filters != null) {
                    val list = applistcollection.find(filters).limit(pageSize)
                        .skip(page * pageSize).toMutableList()
                    Logger.e("获取数据大小：：" + list.size + filters.toString())
                    return@async list
                } else {
                    return@async applistcollection.find().limit(pageSize).skip(page * pageSize)
                        .toMutableList()
                }
            }

            listData.await()

        }

    }
}