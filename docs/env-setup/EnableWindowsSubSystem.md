# 启用 Windows Subsystem for Linux

[WSL](https://aka.ms/wsldocs)（Windows Subsystem for Linux）是直接在 Windows 上运行的 Linux 环境，不同于传统的虚拟机。  

WSL 项目已在 Github 开源，感兴趣可以点击 [此处](https://github.com/microsoft/WSL) 访问。  

本文主要讲解如何启用 WSL 并配置我们所需的基本环境。  

在此之前，我建议先去阅读 [安装Windows终端](./InstallTerminal.md) 来启用 Windows Terminal。  

## 启用 WSL 并安装需要的 Linux 分发版

参考 [如何使用&nbsp;WSL&nbsp;在&nbsp;Windows&nbsp;上安装&nbsp;Linux](https://learn.microsoft.com/zh-cn/windows/wsl/install)  

本项目将安装 Debian 发行版的子系统，并用于后续的服务部署。  

```text
打开终端，并输入如下命令
  wsl.exe --install --distribution Debian
之后按提示授权，并等待安装完毕，之后配置用户名和密码。
```

## 配置 SSH 连接

Windows 资源管理器可以直接访问子系统的文件，但是并不方便管理文件权限，我们可以自行配置 SSH 连接来方便地管理文件和权限。  
由于子系统并未配置SSH，我们需要自己配置安装。

```text
使用终端打开子系统，并输入如下命令来检查 SSH 状态。
 sudo systemctl status ssh
如果提示 Unit ssh.service could not be found. ，那使用如下命令安装 ssh 并启用 ssh 服务。
 sudo apt install openssh-server -y
 sudo systemctl enable ssh --now
之后再次检查 SSH 服务状态。

服务启动后，使用如下命令检查子系统的 ip 地址
 ip a
并尝试使用 Powershell 终端，使用 ssh 命令进行登录，按提示接受 fingerprint 并输入密码。
 ssh <UserName>@<IP>
登录成功后则说明 SSH 连接配置正常。
```

## 配置本地磁盘挂载点为只读（可选）（但重要！）

wsl 会自动挂载本地磁盘到 /mnt 下，但是在绝大多数场景下，我们并不需要去写入本地磁盘。  
我们可以将挂载点设置为只读，以避免子系统误操作本地磁盘文件。  
（比如在子系统中根目录下执行 "rm -rf ."，这会顺带帮你删除所有的本地磁盘文件！）  

```text
使用如下命令编辑 wsl.conf 文件，来更改挂载点的行为。
  sudo vim /etc/wsl.conf
在文件中添加如下两行：（事实上options只包含ro就足够了）
  [automount]
  options = "ro,metadata,umask=22,fmask=11"
保存退出，并重启子系统。

重启后使用 mount 命令来检查本地磁盘是否已经挂载为ReadOnly。
也可使用touch命令来检查读写状态。
  touch /mnt/d/test.txt
```  

## 配置公钥登录（可选）

参考 OpenSSH 的 [ssh-keygen](https://man.openbsd.org/ssh-keygen) 的文档。  
首选推荐 ed25519 密钥，也可使用**至少**4096位的 RSA 密钥。  

```text
在 %USERPROFILE% 文件夹中，创建 .ssh 目录。（理论上讲，既然你已经在使用 Github，那你应该配置过密钥对了。）
进入 .ssh 目录并打开终端，使用如下命令生成 ed25519 或者 4096 位的 RSA 密钥对。
 ssh-keygen -t ed25519
 ssh-keygen -t rsa -b 4096
根据提示来配置密钥名称和加密，也可以一路回车生成默认密钥对，此处以 id_rsa 和 id_rsa.pub 为例讲解。

需要注意的是，无论如何不要遗失或者共享你的私钥！

然后回到 Debian 的终端，使用如下命令检查生成的 key 的类型是否受支持。
 ssh -Q key
然后使用如下命令去添加刚刚生成的公钥，注意切换到连接要使用的用户。
 mkdir -p ~/.ssh
 chmod 700 ~/.ssh
 touch ~/.ssh/authorized_keys
 chmod 600 ~/.ssh/authorized_keys
 echo "刚刚生成的id_rsa.pub的内容" >> ~/.ssh/authorized_keys

之后回到 Windows 的终端，再次使用 ssh <UserName>@<IP> 去尝试连接，当不需要输入密码时，说明配置成功。
```

## 安装 JDK21

```text
先使用如下命令安装更新 wget。

//TODO

````  
