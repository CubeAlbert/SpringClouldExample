# 配置服务器环境  

## 安装 Oracle JDK

本项目全部使用 Oracle JDK 21，而非 OpenJDK，所以需要去 Oracle 官网获取安装包而非直接通过包管理软件安装 OpenJDK。  
（本项目不保证在 OpenJDK 下能正常运行）  

```text
先使用如下命令安装更新 wget：
  sudo apt-get install wget -y
然后在用户目录下执行：
  wget [Oracle JDK deb包下载地址]
例如
  wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.deb
之后执行如下命令来安装 Oracle JDK：
  sudo dpkg -i jdk-21_linux-x64_bin.deb
最后通过如下命令测试是否安装成功：
  java -version
  javac -version

````  

安装完毕后，最好去配置一下 $JAVA_HOME 环境变量。  
Nacos 的启动脚本会尝试一些默认路径，找不到时也会去反推 Java 的安装目录，但为了保证可以使用到正确版本的 JDK，最好手动配置一下。  

```text
使用如下命令查找 jdk 的安装目录：
  update-alternatives --list java
可得到类似如下结果：
  /usr/lib/jvm/jdk-21.0.8-oracle-x64/bin/java
可知 /usr/lib/jvm/jdk-21.0.8-oracle-x64 就是我们需要设置的 JAVA_HOME 环境变量
执行如下命令在 /etc/profile.d 创建一个新的脚本文件。(叫什么名字都行，不必是 variable)
  sudo vim /etc/profile.d/variable.sh
在其中添加一行：
  export JAVA_HOME=/usr/lib/jvm/jdk-21.0.8-oracle-x64
保存并退出。
然后使用如下命令更改该文件的权限：（644可避免文件被执行以及被除root用户外的其他用户修改）
  sudo chomd 644 /etc/profile.d/variable.sh
之后重新加载用户配置或者重启系统。
```

## 创建应用目录

本项目将在根目录下创建 /app 文件夹，并存放 Nacos Server 以及项目编译后的文件。

```text
  sudo mkdir /app
  sudo chmod 755 /app
```

## 创建新用户用来运行应用

本项目将创建 appfid 用户，作为运行服务的用户，后文不再解释 appfid 的由来。  

```text
使用如下命令创建组和用户，并设置默认目录:
  sudo groupadd -r webapps
  sudo useradd -r -s /bin/false -d /home/appfid -g webapps appfid
  sudo mkdir /home/appfid
  sudo chown appfid:webapps appfid
  sudo chmod 700 appfid
 ```
