# JianDan客户端高仿版
---

##项目介绍

2015-4-10

本开源项目是我的毕设，对煎蛋Android客户端进行了高仿和优化，欢迎大家提交issue或者pull request。本项目使用Volley作为主要的网络请求框架，图片加载使用Fresco作为加载库，希望能够成为国内首批使用Fresco的App。

2015-4-21

本来想使用Fresco的，因为对GIF和渐进式加载的支持，但是，由于Fresco暂时不支持wrap_content，对于图片显示功能里面高度不同的图片无法正常显示，所以只能暂时放弃Fresco，改用UIL。虽然Fresco的功能很强大，但是毕竟是刚出的一个类库，还需要逐步的完善，等慢慢成熟之后再用在商业项目比较好，现在不是很推荐使用。

2015-4-23

今天因为列表页卡顿的问题郁闷了一下午，最后终于发现是我的自定义控件写的有问题，因为进行矩阵计算，严重的影响性能，所以修改了ShowMaxImageView自定义控件，现在滚动起来流畅多了，之前看过那么多Bitmap的优化文章，真遇到还是有点摸不着头脑，这就是经验和教训！

2015-4-30

完成大部分功能，彻底移除Fresco，改用UIL.

2015-5-4

今天是五四青年节，终于完成了列表的加载动画，位移+渐变，多谢issue中回答该问题的@fangchao3652和@songzhw的指导，最终使用@fangchao3652的方法成功.还有一个回复的小功能就完成了~

2015-5-5

主要功能基本完工~但是还有本地离线缓存哦~

2015-5-12

缓存已完成，使用的是GreenDao，论文也写完了,总算完工~

##已完成的功能
- 新鲜事的列表、详情、分享、吐槽、回复
- 无聊图的列表、详情、保存、分享、吐槽、回复、投票
- 妹子图的列表、详情、保存、分享、吐槽、回复、投票、隐藏
- 段子的列表、详情、复制、分享、吐槽、回复、投票
- 小视频的列表、详情、复制、分享、吐槽、投票、回复
- 设置界面
- 所有列表界面的加载动画(我想抽取父类Adapter，但是没成功呢，要不你帮我看看？)
- 新鲜事大小图模式切换
- 所有模块的本地缓存功能

##优化的功能
- 添加加载等待动画
- 添加加载失败提示
- 添加段子列表界面，点击标题栏快速返回顶端
- 添加评论楼层过多隐藏
- 添加网络状态检测
- 优化无聊图列表显示，非WIFI状态下，显示GIF缩略图，点击后下载
- 加载模式全自动智能切换，显著提高加载速度，节省大量流量
- 修改图片详情页为完全沉浸效果
- 图片详情页添加投票结果的颜色标示
- 添加图片列表滚动检测，滚动状态暂停加载，进一步提高加载速度，减少卡顿
- 添加图片加载图片
- 添加当前栏目标志，避免重复切换
- 修改新鲜事列表页效果为CardView
- 由于小视频接口有问题，当加载成功的数据少于10条时，会自动加载下一页，直到大于10条为止
- 添加新鲜事大图模式切换功能

##使用到的开源框架
- [Volley](https://android.googlesource.com/platform/frameworks/volley)
- [Universal Image Load](https://github.com/nostra13/Android-Universal-Image-Loader)
- [butter knife](https://github.com/JakeWharton/butterknife)
- [EventBus](https://github.com/greenrobot/EventBus)
- [material-dialogs](https://github.com/afollestad/material-dialogs)
- [gson](https://code.google.com/p/google-gson/)
- [GreenDao](http://greendao-orm.com/)

##关于我
- [青岛科技大学](http://www.qust.edu.cn/)信息学院大四学生,信息113班长
- 有追求的Android开发工程师
- [CSDN博客专家](http://blog.csdn.net/zhaokaiqiang1992)
- 慕课网Android讲师

##效果图
![](http://i2.tietuku.com/3644113fbf848270.png)
![](http://i1.tietuku.com/f995b5530132fe34.png)
![](http://i2.tietuku.com/e40e1f58d310977c.png)