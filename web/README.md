# 安装node.js及npm

>1. 获取安装包 

https://www.jianguoyun.com/p/DaK_85sQk_bxBxiorakD 

![1593998661040](C:\Users\chenfei\AppData\Roaming\Typora\typora-user-images\1593998661040.png)

根据32位还是64位下载对应的版本

下载完成后点击安装，全部默认即可。
查看对应的版本号

![1593998902626](C:\Users\chenfei\AppData\Roaming\Typora\typora-user-images\1593998902626.png)


>2. 安装cnpm, 加速下载速度

打开命令行窗口，运行如下命令
npm install -g cnpm --registry=https://registry.npm.taobao.org

> 3. 安装依赖  
到项目文件夹下面
运营一下命令：
cnpm install 
等待下载完成

> 4. 启动服务
在项目文件夹下面，运行 startup.bat 或者运行命令
npm run dev