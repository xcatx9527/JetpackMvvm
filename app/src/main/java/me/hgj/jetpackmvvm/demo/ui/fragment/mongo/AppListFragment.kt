package me.hgj.jetpackmvvm.demo.ui.fragment.mongo

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment1
import me.hgj.jetpackmvvm.demo.app.ext.*
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.DefineLoadMoreView
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.demo.databinding.FragmentHomeBinding
import me.hgj.jetpackmvvm.demo.ui.adapter.AppListAdapter
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestHomeViewModel
import me.hgj.jetpackmvvm.demo.viewmodel.state.AppListViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction
import org.bson.conversions.Bson

/**
 * 作者　: chenyang
 * 时间　: 2023/03/31
 * 描述
 */
class AppListFragment : BaseFragment1<AppListViewModel, FragmentHomeBinding>() {

    //适配器
    private val appListAdapter: AppListAdapter by lazy { AppListAdapter(arrayListOf()) }

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //recyclerview的底部加载view 因为在首页要动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView
    private var filters: Bson? = null
    private val requestHomeViewModel: RequestHomeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(mViewBind.includeList.includeRecyclerview.swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            mViewModel.getAppList(true)
        }
        //初始化
        mViewBind.includeToolbar.toolbar.run {
            init("首页")
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        nav().navigateAction(R.id.action_mainfragment_to_searchFragment)
                    }
                }
                true
            }
        }
        //初始化recyclerView
        mViewBind.includeList.includeRecyclerview.recyclerView.init(
            LinearLayoutManager(context),
            appListAdapter
        ).let {
            //因为首页要添加轮播图，所以我设置了firstNeedTop字段为false,即第一条数据不需要设置间距
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                mViewModel.getAppList(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(mViewBind.includeList.floatbtn)
        }
        //初始化 SwipeRefreshLayout
        mViewBind.includeList.includeRecyclerview.swipeRefresh.init {
            //触发刷新监听时请求数据
            mViewModel.getAppList(true, filters)

        }
        appListAdapter.run {
            setCollectClick { item, v, _ ->
                if (v.isChecked) {
                } else {
                }
            }
        }
    }

    /**
     * 懒加载
     */
    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        //请求轮播图数据
        requestHomeViewModel.getBannerData()
        //请求文章列表数据
        mViewModel.getAppList(true, filters)
    }

    override fun createObserver() {
        mViewModel.run {
            //监听首页文章列表请求的数据变化
            appListDataState.observe(viewLifecycleOwner, Observer {
                //设值 新写了个拓展函数，搞死了这个恶心的重复代码
                loadListDataM(
                    it,
                    appListAdapter,
                    loadsir,
                    mViewBind.includeList.includeRecyclerview.recyclerView,
                    mViewBind.includeList.includeRecyclerview.swipeRefresh
                )
            })
        }
    }
}