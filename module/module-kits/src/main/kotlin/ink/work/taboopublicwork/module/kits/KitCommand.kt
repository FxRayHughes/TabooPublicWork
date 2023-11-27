package ink.work.taboopublicwork.module.kits

import ink.work.taboopublicwork.module.kits.ModuleKits.sendLang
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.simpleCommand
import taboolib.expansion.createHelper
import taboolib.module.lang.sendLang

@CommandHeader(name = "kits", aliases = ["kit"], permission = "taboopublicwork.command.kits")
object KitCommand {


    @CommandBody
    val main = mainCommand {
        dynamic("kitId") {
            suggestion<CommandSender> { sender, context ->
                ModuleKits.kits.keys.toList()
            }
            execute<Player> { sender, context, argument ->
                val kit = ModuleKits.kits[context["kitId"]]
                if (kit == null) {
                    sender.sendLang("module-kits-not-found", context["kitId"])
                    return@execute
                }
                kit.give(sender)
            }
        }
        createHelper()
    }

    @CommandBody
    val reload = mainCommand {
        execute<CommandSender> { sender, _, _ ->
            ModuleKits.reloadModule()
            sender.sendLang("module-kits-reload")
        }
    }

}
