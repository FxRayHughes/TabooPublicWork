package ink.work.taboopublicwork.module.warp

import ink.work.taboopublicwork.module.warp.ModuleWarp.sendLang
import ink.work.taboopublicwork.module.warp.data.WarpData
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper

@CommandHeader("warp", description = "地标", permission = "taboopublicwork.command.warp")
object WarpCommand {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    // warp create 地标名
    @CommandBody
    val create = subCommand {
        dynamic("name") {
            execute<Player> { sender, context, argument ->
                val name = context["name"]
                if (ModuleWarp.database.getData(name) != null) {
                    sender.sendLang("module-warp-create-exists", name)
                    return@execute
                }
                val world = sender.world.uid
                val location = sender.location
                val yaw = location.yaw
                val pitch = location.pitch
                val warpData = WarpData(name, world, location.x, location.y, location.z, yaw, pitch, sender.uniqueId)
                ModuleWarp.database.addData(warpData)
                sender.sendLang("module-warp-create-success", name)
            }
        }
    }

}
