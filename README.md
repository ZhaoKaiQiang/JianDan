# JianDan客户端高仿版
---

##项目介绍

2015-4-10

本开源项目是我的毕设，对煎蛋Android客户端进行了高仿和优化，欢迎大家提交issue或者pull request。本项目使用Volley作为主要的网络请求框架，图片加载使用Fresco作为加载库，希望能够成为国内首批使用Fresco的App。

2015-4-21

本来想使用Fresco的，因为对GIF和渐进式加载的支持，但是，由于Fresco暂时不支持wrap_content，对于图片显示功能里面高度不同的图片无法正常显示，所以只能暂时放弃Fresco，改用UIL。虽然Fresco的功能很强大，但是毕竟是刚出的一个类库，还需要逐步的完善，等慢慢成熟之后再用在商业项目比较好，现在不是很推荐使用。


##目前已完成的功能
- 查看段子
- 对段子、无聊图进行投票
- 查看吐槽
- 吐槽与回复
- 查看无聊图静态图和GIF动态图

##优化功能
- 添加加载等待动画
- 添加加载失败提示
- 添加段子列表界面，点击标题栏快速返回顶端
- 添加评论楼层过多隐藏
- 优化无聊图列表显示，GIF使用缩略图，不直接下载，后期将添加网络状态判断

##使用到的开源框架
- [Volley](https://android.googlesource.com/platform/frameworks/volley)
- [Fresco](https://code.facebook.com/projects/465232426958622/fresco/)
- [Universal Image Load](https://github.com/nostra13/Android-Universal-Image-Loader)
- [butter knife](https://github.com/JakeWharton/butterknife)
- [material-dialogs](https://github.com/afollestad/material-dialogs)
- [gson](https://code.google.com/p/google-gson/)

##关于我
- [青岛科技大学](http://www.qust.edu.cn/)信息学院大四学生,信息113班长
- 有追求的Android开发工程师
- [CSDN博客专家](http://blog.csdn.net/zhaokaiqiang1992)
- 慕课网Android讲师

##效果图
![](http://i2.tietuku.com/e40e1f58d310977c.png)