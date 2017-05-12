###keystore
MD5: B8:03:22:EE:E9:72:02:98:72:AD:C9:FA:C4:9B:F3:34
SHA1: 86:BE:52:48:CB:00:13:48:AD:CB:23:F3:94:E4:C8:26:A9:CD:93:79
SHA256: 9E:0B:28:66:E3:05:85:72:B9:86:95:9A:B0:91:FD:96:00:C2:CD:9F:32:36:0F:15:E5:98:EB:5C:F6:3E:79:A1

### crnetwork 模块接入
####在app模块build.gradle文件中
#####本地方式
debugCompile project(path: ':crnetwork', configuration: 'debug')
releaseCompile project(path: ':crnetwork', configuration: 'release')
#####maven方式
releaseCompile "com.dianrong.android:crnetwork:1.0.0:release@aar"
debugCompile "com.dianrong.android:crnetwork:1.0.0:debug@aar"
###在Apllication(继承AppContext)中初始化
BaseUrlBindHelper.resetBaseUrl(DianRongHosts.PRODUCT);
BaseUrlBindHelper.resetBaseUrl(ServerType.PRODUCT);
DrErrorMsgHelper.initErrorMsgs();


### processor 模块接入
####在Project build.gradle文件中
dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
}
####在app模块build.gradle文件中
apply plugin: 'com.neenbedankt.android-apt'

packagingOptions {
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/NOTICE.txt'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/LICENSE.txt'
    }

在app中通过compile而非apt方式引入的processor,使用apt方式造成在app模块不正确引用MethodHostSurpported注解
dependencies {
    compile project(path: ':processor')
}

在 processor模块的RepositoryGenerator类中
需要手动定义各类的包名


###上传到maven库
在库的根目录下执行 ../gradlew uploadArchives --info
