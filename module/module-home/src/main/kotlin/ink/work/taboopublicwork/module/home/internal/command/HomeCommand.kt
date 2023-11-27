package ink.work.taboopublicwork.module.home.internal.command

import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand

@CommandHeader(
        name = "home",
        description = "家",
        permission = "taboopublicwork.command.home"
)
object HomeCommand {
    @CommandBody
    val main = mainCommand {
        dynamic(optional = true) {
            execute<Player> { sender, _, argument ->

            }
        }
        execute<Player> { sender, _, _ ->

        }
    }
}