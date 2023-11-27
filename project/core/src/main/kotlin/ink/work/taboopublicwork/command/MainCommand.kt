package ink.work.taboopublicwork.command

import ink.work.taboopublicwork.TabooPublicWork
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.module.chat.colored

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
                val isEnable = TabooPublicWork.modulesEnable[t] ?: false
                sender.sendMessage("§7[§fTabooPublicWork§7] §f- §${if (isEnable) "a" else "c"}${u.name} §7(${u.id} a:${u.author})")
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
                val isEnable = TabooPublicWork.modulesEnable[module] ?: false
                sender.sendMessage("§7[§fTabooPublicWork§7] §f模块信息: §7$module")
                sender.sendMessage("§7[§fTabooPublicWork§7] §f- §f名称: §7${m.name}")
                sender.sendMessage("§7[§fTabooPublicWork§7] §f- §fID: §7${m.id}")
                sender.sendMessage("§7[§fTabooPublicWork§7] §f- §f状态: §${if (isEnable) "a启动" else "c关闭"}")
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
                TabooPublicWork.reloadModule(module)
                sender.sendMessage("§7[§fTabooPublicWork§7] §f模块重载成功: §7$module")
            }
        }
        execute<CommandSender> { sender, _, argument ->
            TabooPublicWork.reload()
            val size = TabooPublicWork.modules.size
            val enable = TabooPublicWork.modulesEnable.filter { it.value }.size
            sender.sendMessage("§7[§fTabooPublicWork§7] §f所有模块重载完成 共${size}个 (&${if (enable == size) "a" else "c"}${enable}&f/&a${size}&f)".colored())
        }
    }

}
