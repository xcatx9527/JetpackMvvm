package me.hgj.jetpackmvvm.demo.ui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.ext.setAdapterAnimation
import me.hgj.jetpackmvvm.demo.app.util.DatetimeUtil
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.app.util.load
import me.hgj.jetpackmvvm.demo.app.util.loadAppIcon
import me.hgj.jetpackmvvm.demo.app.weight.customview.CollectView
import me.hgj.jetpackmvvm.demo.data.model.bean.AriticleResponse
import me.hgj.jetpackmvvm.demo.data.model.bean.IntegralHistoryResponse
import me.hgj.jetpackmvvm.demo.data.model.mongo.AppListBean
import org.litote.kmongo.MongoOperator

/**
 * 积分获取历史 adapter
 * @Author:         hegaojian
 * @CreateDate:     2019/9/1 12:21
 */
class AppListAdapter(data: MutableList<AppListBean>) :
    BaseQuickAdapter<AppListBean, BaseViewHolder>(
        R.layout.item_app_list, data
    ) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: AppListBean) {
        item.run {
            holder.setText(R.id.app_name, item.appName)
            holder.getView<ImageView>(R.id.app_icon).loadAppIcon(item.icon_url)
        }
    }
    fun setCollectClick(inputCollectAction: (item: AriticleResponse, v: CollectView, position: Int) -> Unit) {
    }

}


