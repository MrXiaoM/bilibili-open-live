# bilibili-open-live

哔哩哔哩直播开放平台互动玩法 Java 第三方 SDK

本项目编写参考了[官方的 .NET SDK](https://open-live.bilibili.com/document/67d15b4d-c693-0941-64ca-7232565a5172)

本项目为 Minecraft Spigot 插件，适用于 1.17 或以上版本，如需在更低版本使用，请自行将 java 17 代码转换为 java 8 编译。  
本插件将会把接受到的弹幕、礼物、大航海、SC等通过 Bukkit 的事件系统广播出去，供其它插件使用。

SDK 逻辑在子模块 [SDK](/sdk) 中，与 Minecraft 插件实现分离。

使用示例详见 [CraftBLiveClient.java](src/main/java/top/mrxiaom/bili/CraftBLiveClient.java)
