package ink.work.taboopublicwork.module.warp

import ink.work.taboopublicwork.TabooPublicWork
import ink.work.taboopublicwork.module.warp.ModuleWarp.sendLang
import ink.work.taboopublicwork.module.warp.data.WarpData
import org.bukkit.command.CommandSender
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
        dynamic("name") {
            suggestion<CommandSender> { _, context ->
                ModuleWarp.database.getDataList().map { it.name }
            }
            execute<Player> { sender, context, argument ->
                val name = context["name"]
                val warpData = ModuleWarp.database.getData(name)
                if (warpData == null) {
                    sender.sendLang("module-warp-warp-not-exists", name)
                    return@execute
                }
                val location = warpData.toLocation()
                sender.teleport(location)
                sender.sendLang("module-warp-warp-success", name)
            }
        }
        createHelper()
    }


    @CommandBody
    val help = subCommand {
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
                val worldName = sender.world.name
                val location = sender.location
                val yaw = location.yaw
                val pitch = location.pitch
                val warpData = WarpData(name, world, worldName, location.x, location.y, location.z, yaw, pitch, sender.uniqueId)
                ModuleWarp.database.addData(warpData)
                sender.sendLang("module-warp-create-success", name)
            }
        }
    }

    @CommandBody
    val delete = subCommand {
        dynamic("name") {
            suggestion<CommandSender> { _, context ->
                ModuleWarp.database.getDataList().map { it.name }
            }
            execute<CommandSender> { sender, context, argument ->
                val name = context["name"]
                if (ModuleWarp.database.getData(name) == null) {
                    sender.sendLang("module-warp-delete-not-exists", name)
                    return@execute
                }
                ModuleWarp.database.removeData(name)
                sender.sendLang("module-warp-delete-success", name)
            }
        }
    }

    @CommandBody
    val list = subCommand {
        execute<Player> { sender, context, argument ->
            val list = ModuleWarp.database.getDataList()
            sender.sendLang("module-warp-list", list.joinToString { "${it.name}," })
        }
    }

    @CommandBody
    val goto = subCommand {
        dynamic("name") {
            suggestion<CommandSender> { _, context ->
                ModuleWarp.database.getDataList().map { it.name }
            }
            execute<Player> { sender, context, argument ->
                val name = context["name"]
                val warpData = ModuleWarp.database.getData(name)
                if (warpData == null) {
                    sender.sendLang("module-warp-warp-not-exists", name)
                    return@execute
                }
                val location = warpData.toLocation()
                sender.teleport(location)
                sender.sendLang("module-warp-warp-success", name)
            }
        }
    }

    @CommandBody
    val reload = subCommand {
        execute<CommandSender> { sender, context, argument ->
            TabooPublicWork.reloadModule(ModuleWarp.id)
            sender.sendLang("module-warp-reload")
        }
    }

}
