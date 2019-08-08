# cmwa

> A Vue.js project

## Build Setup
``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build:prd

# build for test with minification
npm run build:test

# build for production and view the bundle analyzer report
npm run build:prd --report   
```

For a detailed explanation on how things work, check out the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).


#路由配置信息解释
 {
    "path": "/demo",                    //路由路径
    "name": "demo",                     //路由别名，禁止使用中文
    "component": "/demo",               //路由组件对应 vue 文件
    "redirect": "",                        //路由重定向
    "meta": {                              //路由元数据
        "title": "",                       //路由组件页标题
        "requireAuth": true,               //路由是否需要登录
        "keepAlive": true,                 //路由页是否需要被缓存
        "description": "demo",             //路由描述
        "wechatAuth":true,                 //是否要授权该页面
        "showFooter":true,                 //是否显示底部导航
    }
}
```

## 公共组件属性使用规范

-vue组件命名规范

-自定义的components 单文件组件的文件名应该要么始终是单词大写开头 (PascalCase)，要么始终是横线连接 (kebab-case)。

-页面布局方式采用弹性盒子布局 页面自适应采用rem跟px组合方式

-组件样式修改 需要在容下增加 /deep/ 然后指定某个样式   

-样式的命名用 采用连接符 “-” 命名，js命名的采用驼峰命名


#项目文件夹说明

├── build                   #项目构建(webpack)相关代码 
│   ├──build.js             #生产环境构建代码
│   ├──check-versions.js    #检查node&npm等版本
│   ├──utils.js             #构建配置公用工具
│   ├──vue-loader.conf.js   #vue加载器
│   ├──webpack.base.conf.js #webpack基础环境配置
│   ├──webpack.dev.conf.js  #webpack开发环境配置
│   └──webpack.prod.conf.js #webpack生产环境配置
├── dist                    #npm run build打包后生成的文件夹
├── node_modules            #项目依赖的模块
│── src                     #源码核心代码
│   ├──assets               #项目样式类文件（如css、less、sass）
│   ├──axios                #项目API
│   ├──components           #项目的功能组件
|   |——filters              #项目全局过滤器
│   ├──mock                 #项目的api mock数据拦截
│   ├──plugins              #项目的公共插件库
│   ├──router               #项目组件的路由控制
│   ├──store                #项目的状态管理
│   ├──util                 #项目的工具类
│   ├──views                #项目的业务视图
│   ├──App.vue              #页面入口文件（根组件）
│   └──main.js              #程序入口文件（入口js文件）    
├──static                   #图片类资源  
├──.babelrc                 #babelrc编译参数
│──.editorconfig            #代码格式
│──.gitignore               #git上传需要忽略的文件配置
│──.postcssrc.js            #转换css的工具
│──index.html               #项目主入口页面
│──package.json             #项目基本信息
├──package-lock.json        #记录当前状态下实际安装的各个npm package的具体来源和版本号。
└──README.md                #项目说明

#项目组件为mand-mobile
  参考API ：https://didi.github.io/mand-mobile/#/zh-CN/docs/started

#列表加载插件：mescroll
 参考API ：http://www.mescroll.com/api.html

#date-fns日期工具类库
 参考API：https://blog.csdn.net/fsxxzq521/article/details/85715213#31_min__351

#qrcode二维码自动生成
 参考API：https://www.npmjs.com/package/qrcode.vue

#如何安装dev-tools 调试工具
    一.在github上下载压缩包，github下载地址：https://github.com/vuejs/vue-devtools
    二.解压到本地的某盘
    三.用你的npm中进入该文件夹下
    四.依次输:1:npm install
             2:npm run build   
    五：修改shells>chrome文件夹下的mainifest.json 中的persistant为true
    六：我们找到谷歌浏览器的扩展程序功能，勾选开发者模式，然后我们将插件文件夹里的shells>chorme文件夹直接拖到页面中，完成安装。
    七：在插件的目录下执行npm run dev，这个时候我们的插件就可以运行了,打开localhost:8080可以看到插件已经安装并运行了。
    八：我们在打开本地的其他项目时，就不需要在vue-devtools文件夹下执行npm run dev了，因为这个插件已经安装在浏览器中。接下来就愉快的调试你的vue项目吧。


脚手架生产步骤
1、全局安装脚手脚 npm install -g vue-cli
2、初始化脚手架项目vue init webapck demo


将本地文件夹push到gitHub
1、进入项目文件夹git init  将目录变成git可以管理的仓库
2、git add .  添加文件夹的所有文件
3、git commit 告诉git，把文件提交到仓库
   git commit -m 'cmwa'
4、关联到远程仓库   
$  git remote rm origin
   git remote add origin  url
5、把本地库内容推送到远程
   git push -u origin master 