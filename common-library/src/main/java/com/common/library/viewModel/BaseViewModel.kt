package com.common.library.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.common.library.liveData.BooleanLiveData
import com.common.library.liveData.StringLiveData

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    open var hintMsg = StringLiveData()

    open var showAppLoading = StringLiveData();

    open var hideAppLoading = BooleanLiveData();
}





