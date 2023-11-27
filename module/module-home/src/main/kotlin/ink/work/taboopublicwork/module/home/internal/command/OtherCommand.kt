package ink.work.taboopublicwork.module.home.internal.command

import ink.work.taboopublicwork.module.home.ModuleHome
import taboolib.common.platform.command.simpleCommand

object OtherCommand {

    fun registerCommand() {
        if (!ModuleHome.isEnable()) {
            return
        }

        simpleCommand("delhome", permission = "taboopublicwork.command.delhome") { sender, args ->

        }
    }

}
