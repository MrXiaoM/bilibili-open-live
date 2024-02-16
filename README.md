# bilibili-open-live

哔哩哔哩直播开放平台互动玩法 Java 第三方 SDK

本项目编写参考了[官方的 .NET SDK](https://open-live.bilibili.com/document/67d15b4d-c693-0941-64ca-7232565a5172)

本项目为 Minecraft Spigot 插件，适用于大部分版本，理论上全版本支持。至少需要 java 8 环境才可使用。  
本插件将会把接受到的弹幕、礼物、大航海、SC、点赞等通过 Bukkit 的事件系统广播出去，供其它插件使用。

SDK 逻辑在子模块 [SDK](/sdk) 中，与 Minecraft 插件实现分离。

SDK 使用示例详见 [CraftBLiveClient.java](src/main/java/top/mrxiaom/bili/bukkit/CraftBLiveClient.java)

## 插件命令

```
/bili connect [连接码] - 连接到开放平台，不输入连接码时，使用配置文件里的默认code
/bili code <名称> - 连接到开放平台，使用配置文件里 codes 指定名称对应的连接码
/bili close - 断开与开放平台连接
/bili reload - 重载配置文件
```
