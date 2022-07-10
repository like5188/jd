## 简対商城应用
* 该应用用Java+Kotlin语言混合开发
* 该应用是以Android原生+H5混合开发
* 该应用打包具体配置见start/build.gradle.kts

*  buildSrc --> appVersion 版本升级控制  
   --> BuildVersion app       
   --> Dependencise 第三方框架统一版本管理    
   --> ThirdConfig  第三方SDK的一些配置 如 微信appKey

### 插件说明
*  app 主包
*  common-library 通用包

*  权限请求框架 https://github.com/getActivity/XXPermissions
*  状态栏 https://github.com/gyf-dev/ImmersionBar
*  弹框（dialog，popup ） https://github.com/li-xiaojun/XPopup
*  滚动控件（三级联动， ） https://github.com/gzu-liyujiang/AndroidPicker 按需求引入
*  工具类 https://github.com/Blankj/AndroidUtilCode  工具优先使用这个框架里面的。没有相关功能再添加自定义的
*  事件分发 https://github.com/JeremyLiao/LiveEventBus  采用livedata 具有生命周期感知的分发工具
*  图片选择框架 https://github.com/LuckSiege/PictureSelector
*  刷新框架 https://github.com/scwang90/SmartRefreshLayout  也可用系统自带的SwipeRefresh
*  状态管理控件 https://github.com/liangjingkanji/StateLayout
*  控件阴影 https://github.com/lihangleo2/ShadowLayout
*  翻页 paging3

### 资源储放
* 多个页面共用的资源可以放到 common-library里面
* 其他的尽量放在各自的module里面

### 命名
* 布局 ---> module名称_视图用途_加其他关键字    如：登录layout  user_activity_login     如：消息列表item  user_item_msg
* 资源命名 规则   图片 ---> module名称_ic Or bg _ 加其他关键字  如：应用logo  base_ic_logo  或者 base_ic_logo_1024
* xml背景或者样式---> module名称_(如 shape stroke select)_(颜色值)_(其他关键字)   
  如：如10dp的纯色背景 base_shape_white_10dp  
  如：如10dp的蓝边框背景 base_stroke_blue_10dp  
  如：如12的#333333的状态框背景 base_select_333_12dp
* 颜色命名---> 不出意外，颜色基本在common-library里面定义  
  color_颜色值 前三位数 如 color_333    
  如与纯色 可以采用对应的英文 如 color_red  color_white  
  开发中 遇到颜色比较向近的颜色值，优先选择由common-library里面定义的  
  其他 各个资源命名 都以"moduleName_"名未开头 如user module --> user_
### 路由Arouter
* 路由地址定义com.common.library.router.ARouterPath 下各个模块对应的类下面
* 地址命名规则：module名称/关键字（一般以页面功能或者类功能作为关键字）   
  如 user/login (登录页面)   
  如 user/userInfoManager(用户信息管理manager)

### 基类说明
*  顶级基类AppBaseActivity 和 AppBaseFragment  所以页面activity 和fragment 都继承或者间接继承
*  vm 基类  BaseView-- 和BaseVm 都可以。
*  viewModel 都继承 com.common.library.viewModel.BaseViewModel
*  列表页面 继承 BasePagingActivity 翻页采用的paging3
*  actionbar 采用的noActionBar 主题  顶部Toolbar采用自定义 base_view_title 直接布局文件引用。  
   若需要其他样式 可在base重新创建新的  由于基类都对toolbar进行了预处理，若返回事件，或者设置title 所以控件id尽可能一致，若不一致，则自己处理相关事件
* 网络请求，按照common 包里面的BaseNewApi 和 BaseNetApiService 来处理

### module说明
* common-library 工具内，和基类模块， 公共模块
* http 网络请求模块
* user 登录模块，
* setup 系统设置模块，消息管理，员工管理，评价管理
* good 首页，商品管理，注册商户，团购获得，订单管理
*  是否开始独立模块运行 控制开关在buildSrc
### module结构说明
```

├── user
│   ├──libs
│   ├── src
│      ├── androidTest
│      ├── main
│         ├── java
│            ├── com.cq.jd.user
│               ├── bean
│               ├── login
│                  ├── loginActivity 
│                  ├── loginViewModel 
│               ├── register
│               ├── msg
│                  ├── MsgActivity 
│                  ├── MsgViewModel 
│                  ├── MsgAdapter 
│                  ├── MsgSource 
│               ├── dialog
│               ├── net
│               ├── utils
│               ├── widget
│         ├── res
│         ├── androidManifest.xml
│      ├── test
│   ├──.gitignore
│   ├──build
│   └──proguard

```
* 功能模块 页面相关的，非公共的，直接放到功能模块里面，
* 不用放到公共包下面去

### 新增 （如页面 ，module）
* 若无其他需要配置的  可以直接复制 user里面的build.gradle.kts 替换 build.gradle



### 其他注意点
* 引入第三方sdk 需要在混淆规则里面添加相应的混淆规则 没有可忽略



### 分支说明
* master 主分支 每次升级之后都会合并到主分支

### 渠道说明
