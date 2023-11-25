package ink.work.taboopublicwork

import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.Plugin

/**
 * 这是你的插件在 Bukkit 平台运行的基础
 * 一般情况下你不需要修改这个类
 */
@PlatformSide([Platform.BUKKIT])
object BukkitPlugin : Plugin()
