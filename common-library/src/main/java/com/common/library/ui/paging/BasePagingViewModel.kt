package com.common.library.ui.paging

import android.app.Application
import androidx.paging.PagingData
import com.common.library.viewModel.BaseViewModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by *** on 2022/1/13 2:34 下午
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
abstract class BasePagingViewModel<T : Any>(application: Application) : BaseViewModel(application) {


    abstract fun loadData(map: HashMap<String, Any>? ): Flow<PagingData<T>>


}