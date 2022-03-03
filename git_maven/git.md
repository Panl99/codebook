# git常用命令
## 1、提交
#### 初始化git仓库
    git init
#### 克隆远程仓库项目：
    git clone 'github项目地址'
#### 把文件添加到缓存区中：
    git add <file>
#### 把文件提交到本地仓库： 
    git commit -m '提交描述'
#### 关联GitHub远程库：
    git remote add origin 'github项目地址'
#### 获取远程库与本地同步合并
    git pull --rebase origin master
#### 推送到远程仓库(git push [remote-name] [branch-name])
    git push -u origin master
    
## 2、查看
#### 查看提交状态：
    git status
#### 查看远程仓库地址：
    git remote -v  
#### 查看提交记录
    git log
#### 查看帮助
    git help

## 3、修改
#### 重命名
    git mv <oldFlie> <newFile>

## 4、删除
#### 删除缓存区文件(取消add状态)
    git rm --cached <file>
#### 删除git版本库文件
    git rm <file>
#### 放弃工作区中的修改(工作区内容add后有，又修改了工作区，放弃修改的内容)
    git checkout --<file>
#### 清空缓存区
    git reset HEAD <file>

## 5、分支
#### 创建分支
    git branch <branchName>
#### 查看分支
    git branch
#### 切换分支
    git checkout <branchName>
#### 创建并切换到分支
    git checkout -b <branchName>
#### 合并分支(先切换到主分支，在合并其他分支内容)
    git merge <branchName>
#### 查看哪些分支已合并到当前分支
    git branch --merged
    查看还没合并的分支：
    git branch --no-merge
#### 删除分支(-D：强制删除)
    git branch -d <branchName>

## 6、问题
#### 当项目没开发完，但需要将远程仓库新代码更新到本地：
    git stash //将本地代码暂存缓存区
    git pull --rebase //拉取远程仓库代码
    git stash pop //将代码从缓存区取出