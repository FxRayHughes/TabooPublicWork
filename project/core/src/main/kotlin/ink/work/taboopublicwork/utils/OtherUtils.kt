package ink.work.taboopublicwork.utils

import org.bukkit.Location
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import java.util.*

val tpMap = HashMap<UUID, Location>()

/**
 *  延迟传送组件
 *
 *  sender.tpDelay(ModuleHome.delayTp, home.second, {
 *    sender.sendLang("module-home-home-tp", name, ModuleHome.delayTp)
 *  }, {
 *    sender.sendLang("module-home-home-tp-cancel")
 *  })
 */
fun Player.tpDelay(mint: Long, locationTo: Location, start: Player.() -> Unit = {}, error: Player.() -> Unit = {}) {
    tpMap[this.uniqueId] = this.location
    start.invoke(this)
    submit(delay = mint) {
        val a = this@tpDelay.location
        val b = tpMap[this@tpDelay.uniqueId] ?: return@submit
        if (a.x != b.x || a.y != b.y || a.z != b.z) {
            error.invoke(this@tpDelay)
            tpMap.remove(this@tpDelay.uniqueId)
            return@submit
        }
        this@tpDelay.teleport(locationTo)
        tpMap.remove(this@tpDelay.uniqueId)
    }
}
