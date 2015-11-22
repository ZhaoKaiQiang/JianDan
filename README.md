# JianDan客户端高仿版

---

##效果图

废话不多说，先上效果图

![GIF](https://github.com/ZhaoKaiQiang/JianDan/blob/master/images/demo.gif)

##项目介绍

对煎蛋客户端进行优化，API用的是煎蛋官方的。

项目虽小，五脏俱全，学习这个项目，你将学到

- Android Studio开发Android的基本配置
- 对MD风格的Theme兼容
- 对Volley网络请求库的封装和自定义
- Application、Activity和Fragment基类的基本设计
- MVC架构的使用
- 对超长图片加载的解决方案
- WebView与JS通信，及加载本地缓存图片的实现
- RecyclerView自动加载、加载完成回调，以及item进入动画
- 多楼层回复自定义控件的实现
- 严格模式(StrictMode)的设置及用法
- 使用GreenDao对数据进行本地缓存
- 对网络状态的实时检测
- 对UIL的个性化设置、封装和基本使用
- 使用LeakCanary检测内存泄露

##其他分支

- [使用OkHttp替换Volley底层实现作为网络框架的版本](https://github.com/ZhaoKaiQiang/JianDan_OkHttpWithVolley)
- [使用Android-Async-Http作为网络框架的版本](https://github.com/ZhaoKaiQiang/JianDan_AsyncHttpClient)
- [纯粹使用OkHttp作为网络框架的版本](https://github.com/ZhaoKaiQiang/JianDan_OkHttp)

##优化的功能

- 添加加载等待动画
- 添加加载失败提示
- 添加评论楼层过多隐藏
- 添加网络状态检测
- 优化无聊图列表显示，非WIFI状态下，显示GIF缩略图，点击后下载
- 加载模式全自动智能切换，显著提高加载速度，节省大量流量
- 修改图片详情页为完全沉浸效果
- 添加图片列表滚动检测，滚动状态暂停加载，进一步提高加载速度，减少卡顿
- 添加图片加载默认图片
- 添加当前栏目标志，避免重复切换
- 修改新鲜事列表页效果为CardView
- 由于小视频接口有问题，当加载成功的数据少于10条时，会自动加载下一页，直到大于10条为止
- 添加新鲜事大图模式切换功能

##关于我

- [Android研发工程师](http://weibo.com/zhaokaiqiang1992)
- [CSDN博客专家](http://blog.csdn.net/zhaokaiqiang1992)

##使用到的开源框架

- [Volley](https://android.googlesource.com/platform/frameworks/volley)
- [Universal Image Load](https://github.com/nostra13/Android-Universal-Image-Loader)
- [butter knife](https://github.com/JakeWharton/butterknife)
- [EventBus](https://github.com/greenrobot/EventBus)
- [material-dialogs](https://github.com/afollestad/material-dialogs)
- [gson](https://code.google.com/p/google-gson/)
- [GreenDao](http://greendao-orm.com/)
- [Loading](https://github.com/yankai-victor/Loading)
- [LeakCanary](https://github.com/square/leakcanary)
