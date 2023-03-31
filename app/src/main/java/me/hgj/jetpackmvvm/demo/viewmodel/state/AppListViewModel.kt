package me.hgj.jetpackmvvm.demo.viewmodel.state

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiStateM
import me.hgj.jetpackmvvm.demo.data.model.mongo.AppListBean
import me.hgj.jetpackmvvm.demo.ui.fragment.mongo.MongoDataCoroutine
import me.hgj.jetpackmvvm.ext.requestNoCheck
import org.bson.conversions.Bson

/**
 * 作者　: chenyang
 * 时间　: 2023/03/31
 */
class AppListViewModel : BaseViewModel() {
    var pageNo = 0

    var appListDataState: MutableLiveData<ListDataUiStateM<AppListBean>> = MutableLiveData()

    fun getAppList(isRefresh: Boolean, filter: Bson? =null) {
        if (isRefresh) {
            pageNo = 0
        }
        requestNoCheck({ MongoDataCoroutine.getAppListData(pageNo, filter) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiStateM(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.size == MongoDataCoroutine.pageSize,
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it
                )
            appListDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiStateM(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = mutableListOf<AppListBean>()
                )
            appListDataState.value = listDataUiState
        })
    }
}