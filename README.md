MethodLog
=========

前言
---

* 当应用的业务逻辑比较复杂时，我们经常会因为方法的调用逻辑烦恼，且Debug远远没有Log来的直观。
* 用AspectJ实现日志的repo很多，却没有找到整体输出的。
* 实现比较简单，但由于AspectJ的实现需要创建lib，建一个库可以方便在测试时使用。

**MethodLog将所有的方法当作切面，在日志输出时进行统一过滤，并非最优解。但由于主要目的是为了展示方法的调用顺序，
并且能够方便快速地使用，于是就这么做了。建议只在测试时引入。**

引入
---

AspectJ插件直接用了Jake Wharton God [Hugo](https://github.com/JakeWharton/hugo) 中的Plugin:

```groovy
buildscript {
    dependencies {
        classpath 'com.jakewharton.hugo:hugo-plugin:1.2.1'
    }
}
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

apply plugin: 'com.android.application'
apply plugin: 'com.jakewharton.hugo'

dependencies {
    implementation 'com.github.XingdongYu:MethodLog:v1.0.0'
}
```
使用
---

Runtime期间可以通过 `LogConfig.get().enable()`启用Log，通过 `LogConfig.get().disable()`停用。默认开启。

在Application中进行统一配置:

```java
public class LogApplication extends Application {

    /**
     * 若不配置，默认输出所有方法log
     */
    {
        final LogConfig logConfig = LogConfig.get();
        // 屏蔽类
        logConfig.mode(LogMode.BLOCK).blockClass(MainActivity.class, SecondActivity.class);
        // 选择类
        logConfig.mode(LogMode.SELECT).selectClass(MainActivity.class, SecondActivity.class);

        // 屏蔽方法
        logConfig.blockMethod("onCreate");
    }

}
```
LogMode.BLOCK和LogMode.SELECT互斥，默认模式为LogMode.BLOCK，屏蔽method不受LogMode约束。
若blockMethod参数为"on"，则会屏蔽所有以"on"开头的方法。

输出
----
日志的输出借鉴了[logger](https://github.com/orhanobut/logger)

![image](https://github.com/XingdongYu/MethodLog/blob/master/art/log.png)

关于Kotlin
--------
可参考[gradle_plugin_android_aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)
