BookReader
===
一个在线免费小说阅读项目，实现了在线小说搜索、小说信息展示、在线阅读、阅读模式切换、章节列表跳转、加入书架、书签、删除书架小说等功能


实现功能：
---
1.书架小说加载&数据库搭建<br>
2.在线搜索小说<br>
3.在线阅读（阅读章节&小说内容&章节标题）<br>
4.阅读模式切换（白天&黑夜）<br>
5.加入书架<br>
6.书签<br>
7.章节跳转<br>
8.删除书架小说<br>


进入界面
---
![image](https://github.com/PengLeixin/BookReader/blob/master/app/src/main/res/drawable/gif4_1.gif)

>运行程序直接进入书架界面，主要由recyclerView构成，书籍信息由数据库中加载而来，由于没有UI素材故无法做的更为精美

在线小说搜索
---
![image](https://github.com/PengLeixin/BookReader/blob/master/app/src/main/res/drawable/gif4_2.gif)

>该功能主要由一个SearchView实现，所用到的小说搜索API接口是由Charles抓包小说阅读神器APP得到的，由接口得到json数据
再进行解析，得到的信息存入List再传入recyclerview进行布局排列并设置监听事件点击进入书籍详情页面

书籍详情&加入书架
---
![image](https://github.com/PengLeixin/BookReader/blob/master/app/src/main/res/drawable/gif4_3.gif)<br>
![image](https://github.com/PengLeixin/BookReader/blob/master/app/src/main/res/drawable/gif4_4.gif)
>该页面由搜索页面点击搜索结果跳转进入，页面展示了书籍的详细信息，并且实现了加入书架与阅读功能，展示书籍信息主要使用了ExpandableTextView框架，书籍介绍
如果超过四行便会收起，可以点开查看完整信息，加入书架功能则是将该页面展示的书籍的信息存入数据库中并通知书架activity更新，根据书籍id可判断该书是否已经加入
书架，阅读功能便是点击跳转到阅读界面，会根据当前书籍变动阅读界面信息

小说阅读
---
![image](https://github.com/PengLeixin/BookReader/blob/master/app/src/main/res/drawable/gif4_5.gif)
>小说阅读界面主要由顶部小说章节名称、中部小说内容、底部小说章节数三部分构成，此外还包含一个阅读工具的popwindow。在onTouchEvent我通过对x坐标的判断
将整个页面分为左中右三部分设置点击事件，左部点击会切换到上一页，到了该章第一页再点击则会切换到上一章最后一页，到了第一章第一页则会提示无法切换，
右部则与之相反，中部点击则会跳出popwindow，同时阅读界面设置anim动画背景变暗

章节跳转&阅读模式切换
---
![image](https://github.com/PengLeixin/BookReader/blob/master/app/src/main/res/drawable/gif4_6.gif)<br>
![image](https://github.com/PengLeixin/BookReader/blob/master/app/src/main/res/drawable/gif4_7.gif)
>章节跳转主要由一个带recyclerView的popWindow实现，小说在进入阅读界面时便会将小说的章节列表信息传给recyclerView并嵌入popwindow并设置每一个item点击
事件为跳转到该章节第一页，在阅读工具popwindow中可以点击弹出该popwindow。阅读模式切换则是通过一个标志判断当前模式，再根据当前模式切换到另一模式，主要
是设置阅读背景颜色与字体颜色


书签&删除小说&刷新
---
![image](https://github.com/PengLeixin/BookReader/blob/master/app/src/main/res/drawable/gif4_8.gif)<br>
![image](https://github.com/PengLeixin/BookReader/blob/master/app/src/main/res/drawable/gif4_9.gif)
>书签功能是指对于已经加入书架的小说，再次阅读时会直接跳转到上次阅读结束的章数及页数，在阅读界面的onStop方法中将当前阅读章数及页数存入数据库并且使用
notifyDataSetChanged方法通知书架recyclerview进行更新，再次阅读时先判断其是否在书架内，在则直接跳转到上次阅读页数。删除小说则是为书架item设置长按监听事件
，跳出一个popmenu,其中删除则是删除数据库及list中该书籍信息并notifyDataSetChanged刷新，也可以下拉书架界面进行手动刷新，主要运用SwipeRefreshLayout


使用开源框架：
---
1.ExpandableTextView<br>
2.Glide<br>
3.Okhttp<br>
4.RecyclerView<br>

使用设计模式：
---
1.单例模式<br>
2.适配器模式<br>

