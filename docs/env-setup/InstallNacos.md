# 在服务器上安装Nacos Server

从 [Nacos Server](https://nacos.io/download/nacos-server/) 官网或者 [Github 发布页](https://github.com/alibaba/nacos/releases) 获取 Nacos Server 二进制包。  
撰写本文时，Nacos Server 已经发布 3.1.0 版本，本项目将使用该版本。  

## 获取 Nacos Server 二进制包

获取 Nacose Server 安装包，并解压到工作目录。  

```text
使用wget获取安装包，解压并赋权限给到 appfid 用户。
  cd /app
  sudo wget https://github.com/alibaba/nacos/releases/download/3.1.0-bugfix/nacos-server-3.1.0.tar.gz
  sudo tar -xzf nacos-server-3.1.0.tar.gz
  sudo chown -R appfid:webapps nacos
```

**后文中提到的 NACOS_HOME 将指代 /app/nacos 目录。**  

## 配置 Nacos 认证相关的核心项

根据 [Nacos Quick Start](https://nacos.io/docs/latest/quickstart/quick-start/) 文档，我们需要添加三个鉴权配置。  

```text
nacos.core.auth.plugin.nacos.token.secret.key 令牌签名密钥
nacos.core.auth.server.identity.key 服务器身份标识 key
nacos.core.auth.server.identity.value 服务器身份标识 value
```

由于我们已经禁用掉了 appfid 用户的控制台交互权限，所以我们手动添加这三个配置项到 NACOS_HOME/conf/application.properties 文件中。  

```text
nacos.core.auth.plugin.nacos.token.secret.key 需要是一个至少32字符的Base64编码的字符串，可以使用本项目 /scripts/powershell/Base64KeyGenerator.ps1 生成。
本项目使用如下字符串作为令牌签名密钥😏：
SXRpc2FodWdlYmxlc3Npbmd0b2JlYWJsZXRvJzk5Nic=

nacos.core.auth.server.identity.key 和 nacos.core.auth.server.identity.value 用来标识服务器身份，value 需要是一个复杂的字符串。
本项目分别使用（仅该项目使用，生产环境务必使用安全系数更高的字符串！）：
nacosidentity 和 nacosserveridentityvalue
```

对于集群，每个节点的这三个配置要一致。由于本项目以 standalone 模式启动服务，本文不再深入该部分。  

## 启动 Nacos Server

首先在服务器上启动 Nacos Server 并检查服务状态。

```text
由于我们使用的是 Debian 而非 RedHat，所以需要使用如下命令来启动服务。
  sudo -u appfid bash startup.sh -m standalone
当看到控制台有如下 log 打印，则说明 Nacos Server 正在启动：
nacos is starting. you can check the /app/nacos/logs/startup.log

这个时候我们使用下述命令检查 startup.log:
  tail /app/nacos/logs/startup.log
当看到有如下 log，则说明 Nacos Server 启动成功：
Nacos Console started successfully in 532 ms

再去检查一下应用进程状态是否正常：
  ps -ef | grep 'nacos'
```

之后回到客户端，打开浏览器操作。  

```text
输入 http://[ip]:[host] (比如我这边是 http://172.31.234.7:8080/)
第一次打开时会弹出初始化密码的页面，输入nacos用户的初始密码。
本项目不会暴露在公网，直接使用 nacos 作为密码，生产环境务必使用高强度密码！
```

之后我们使用刚刚创建的用户登录，就能打开 nacos 控制台了。  

## 配置 Nacos Server 自动启动

创建 Nacos-Server 服务，并配置开机启动。

```text
创建 nacos-server.service 文件，并输入下段的内容：
  sudo vim /etc/systemd/system/nacos-server.service
更改文件权限为644：
  sudo chmod 644 nacos-server.service
之后执行如下命令激活并启动该服务：
  sudo systemctl daemon-reload
  sudo systemctl enable nacos-server
  sudo systemctl start nacos-server
使用如下命令检查服务状态并尝试访问 Nacos Server 控制台：
  sudo systemctl status nacos-server
尝试使用如下命令停止服务，并检查是否可以访问 Nacos Server 控制台：
  sudo systemctl stop nacos-server
```

```text
[Unit]
Description=Nacos-Server
After=network.target

[Service]
Type=forking
User=appfid
ExecStart=bash /app/nacos/bin/startup.sh -m standalone
ExecStop=bash /app/nacos/bin/shutdown.sh
Restart=no

[Install]
WantedBy=multi-user.target
```
