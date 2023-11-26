package ink.work.taboopublicwork.command

import ink.work.taboopublicwork.TabooPublicWork
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper

@CommandHeader("taboopublicwork", aliases = ["tpw"], description = "TabooPublicWork", permission = "taboopublicwork.command")
object MainCommand {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val list = subCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendMessage("§7[§fTabooPublicWork§7] §f模块列表:")
            TabooPublicWork.modules.forEach { (t, u) ->
                sender.sendMessage("§7[§fTabooPublicWork§7] §f- §f${u.name} §7(${u.id} a:${u.author})")
            }
        }
    }

    @CommandBody
    val info = subCommand {
        dynamic("module") {
            suggestion<CommandSender> { _, _ ->
                TabooPublicWork.modules.map { it.key }
            }
            execute<CommandSender> { sender, c, argument ->
                val module = c["module"]
                if (TabooPublicWork.modules[module] == null) {
                    sender.sendMessage("§7[§fTabooPublicWork§7] §f模块不存在: §7$module")
                    return@execute
                }
                val m = TabooPublicWork.modules[module]!!
                sender.sendMessage("§7[§fTabooPublicWork§7] §f模块信息: §7$module")
                sender.sendMessage("§7[§fTabooPublicWork§7] §f- §f名称: §7${m.name}")
                sender.sendMessage("§7[§fTabooPublicWork§7] §f- §fID: §7${m.id}")
                sender.sendMessage("§7[§fTabooPublicWork§7] §f- §f作者: §7${m.author}")
                sender.sendMessage("§7[§fTabooPublicWork§7] §f- §f兼容版本: §7${m.versionMin()} - ${m.versionMax()}")
                sender.sendMessage("§7[§fTabooPublicWork§7] §f- §f描述: §7${m.description}")
            }
        }
    }

    @CommandBody
    val reload = subCommand {
        dynamic("module") {
            suggestion<CommandSender> { _, _ ->
                TabooPublicWork.modules.map { it.key }
            }
            execute<CommandSender> { sender, c, argument ->
                val module = c["module"]
                if (TabooPublicWork.modules[module] == null) {
                    sender.sendMessage("§7[§fTabooPublicWork§7] §f模块不存在: §7$module")
                    return@execute
                }
                TabooPublicWork.modules[module]?.reloadModule()
                sender.sendMessage("§7[§fTabooPublicWork§7] §f模块重载成功: §7$module")
            }
        }
        execute<CommandSender> { sender, _, argument ->
            TabooPublicWork.reload()
            sender.sendMessage("§7[§fTabooPublicWork§7] §f所有模块重载完成 共${TabooPublicWork.modules.size}个")
        }
    }

}
