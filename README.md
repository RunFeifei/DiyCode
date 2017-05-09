### crnetwork 模块接入
####在app模块build.gradle文件中
#####本地方式
debugCompile project(path: ':crnetwork', configuration: 'debug')
releaseCompile project(path: ':crnetwork', configuration: 'release')
#####maven方式
releaseCompile "com.dianrong.android:crnetwork:1.0.0:release@aar"
debugCompile "com.dianrong.android:crnetwork:1.0.0:debug@aar"


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
在库的根目录下执行 ../gradlew uploadArchives
