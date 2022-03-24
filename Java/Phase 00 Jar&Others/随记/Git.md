# Git的基本使用

## 一、 安装

- 过



## 二、基本配置

**1. 设置用户信息**

```git
# 设置全局用户名
git config --global user.name "your-name"
# 设置邮箱
git config --global user.email "your-email"

注: 
global: 为全局配置，若不使用该参数，则为当前所在仓库配置
```



**2. 查看配置信息**

``` git
# 查看配置信息
git config --list
```



**3. 创建本地仓库**

``` git
step 1: 新建一个存放版本库的目录
step 2: 在新建的目录下执行 git init
```



**4. 查看文件状态**

``` git
git status [-s]

# 更简洁
git status -s
```



**5. 将文件添加到本地仓库**

``` git
# 添加单个文件到暂存区
git add fileName
# 将当前目录下所有修改添加到暂存区，除按规则忽略的文件
git add .



# 将暂存区中的文件提交到仓库
git commit
# 带说明提交
git commit -m 'your comments'
```



**6. 查看提交记录**

``` git
# 显示所有提交的历史记录
git log
# 单行显示提交历史记录的内容
git log --pretty=oneline
```



**7. 版本回退**

```
# 回退到commit—id 指定的提交版本
git reset --hard 'commit_id'


回到未来的某个提交
git reflog
git reset --hard 'commit_id'
```



**8. 删除文件**

``` git
git rm Readme.md
```



**9. 查看分支**

``` git
# 查看本地分支
git branch

# 查看相对详细的本地分支信息
git branch -v

# 查看包括远程仓库在内的分支信息
git branch -av
```



**10. 创建分支**

``` git
# 新建一个名为dev的分支
git branch dev

切换分支
git checkout dev

# 新建dev分支并切换到该分支上
git checkout -b dev
```



**11. 合并分支** 

``` git
# 切换到master分支
git checkout master
# 将dev分支中修改合并到master分支
git merge dev
```



**12. 删除分支**

``` git
git branch -d dev
```



## 二、 链接远程服务器

**1. 生成公钥和私钥**

``` git
ssh-keygen -t rsa
```



**2. 查看远程仓库**

``` git
# 命令形式
git remote -v
```



**3. 添加远程仓库**

``` git
# 为本地仓库添加远程仓库
git remote add origin your_remote_git_repo
```



**4. 推送本地的内容到远程仓库**

``` git
# 第一次推送时使用，可以简化后续的推送或拉取命令的使用
git push -u origin master
# 将本地master分支推送到origin远程分支
git push origin master
```



**5. 从远程仓库获取最新内容**

``` git
git fetch origin master
git pull origin master
```



**6. 移除远程仓库**

``` git
git remote rm <short-name>
```



**7. 从远程仓库克隆**

``` git
# 通过ssh协议
git clone git@github.com:guoxxxxxxx/learn_repo1.git
```



**8. 从远程仓库拉取**

``` git
git pull <远程仓库名称> <分支>
```



