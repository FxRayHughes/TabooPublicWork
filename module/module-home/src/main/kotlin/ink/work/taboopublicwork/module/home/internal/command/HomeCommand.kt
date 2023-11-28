package ink.work.taboopublicwork.module.home.internal.command

import ink.work.taboopublicwork.module.home.ModuleHome
import ink.work.taboopublicwork.module.home.ModuleHome.sendLang
import ink.work.taboopublicwork.utils.tpDelay
import org.bukkit.entity.Player
import taboolib.common.platform.command.command

object HomeCommand {

    fun registerCommand() {
        if (!ModuleHome.isEnable()) {
            return
        }
        command("sethome") {
            dynamic("name") {
                execute<Player> { sender, context, argument ->
                    val name = context["name"]
                    ModuleHome.setHome(sender, name)
                }
            }
        }

        command("delhome") {
            dynamic("name") {
                suggestion<Player> { sender, context ->
                    ModuleHome.homeList(sender)
                }
                execute<Player> { sender, context, argument ->
                    val name = context["name"]
                    ModuleHome.deleteHome(sender, name)
                }
            }
        }

        command("home") {
            dynamic("name") {
                suggestion<Player> { sender, context ->
                    ModuleHome.homeList(sender)
                }
                execute<Player> { sender, context, argument ->
                    val name = context["name"]
                    val home = ModuleHome.getHome(sender, name)
                    if (home == null) {
                        sender.sendLang("module-home-home-notexist", name)
                        return@execute
                    }
                    sender.tpDelay(ModuleHome.delayTp, home.second, {
                        sender.sendLang("module-home-home-tp", name, ModuleHome.delayTp)
                    }, {
                        sender.sendLang("module-home-home-tp-cancel")
                    })
                    sender.sendLang("module-home-home-success", name)
                }
            }
        }
    }

}
