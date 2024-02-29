# 清理windows空间磁盘空间，避免数据被恢复

### 查看可执行任务
 ./gradlew tasks

### 命令行打包
 调试版本
 ./gradlew assembleDebug
 正式打包 
 ./gradlew assembleRelease

### 整数信息
    签名文件: diskclear.jks
    别名: diskclear
    密码: 88888888

### 查看证书信息
    keytool -list -keystore diskclear.jks -alias diskclear -v

