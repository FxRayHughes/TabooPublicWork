package ink.work.taboopublicwork.utils

import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 *  用于应对通过判断玩家权限然后来获取最大数量的情况
 *
 *  比如 home.max.* 会直接认为是 maxSize
 *  比如 home.max.[数字] 则会进行判断 看玩家最大的值是多少
 *
 *  @param permission 权限节点 home.
 */
fun Player.getPermissionNumber(permission: String, maxSize: Int = 20): Int {
    var number = 0
    if (hasPermission("$permission.*")) {
        return maxSize
    }
    val group = Bukkit.getServer().pluginManager.getPermission(permission) ?: return 0
    return group.children.keys.maxOf { it.toIntOrNull() ?: 0 }
}
