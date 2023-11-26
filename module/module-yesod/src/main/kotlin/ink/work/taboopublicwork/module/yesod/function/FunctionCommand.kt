package ink.ptms.yesod.function

import ink.work.taboopublicwork.module.yesod.Yesod
import org.bukkit.command.PluginCommand
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerCommandSendEvent
import org.spigotmc.SpigotConfig
import taboolib.common.LifeCycle
import taboolib.common.io.taboolibId
import taboolib.common.platform.Awake
import taboolib.common.platform.Ghost
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.BukkitCommand
import java.util.*

/**
 * @author sky
 * @since 2019-11-20 21:48
 */
object FunctionCommand {

    @Awake(LifeCycle.ACTIVE)
    fun e() {
        if (Yesod.config.getBoolean("command-block") && Yesod.isEnable()) {
            PlatformFactory.getAPI<BukkitCommand>().commandMap.commands.forEach { command ->
                if (Yesod.config.getStringList("block-command-path").any { name -> command.javaClass.name.startsWith(name) }) {
                    if (command !is PluginCommand || !command.javaClass.name.startsWith("io.izzel.$taboolibId")) {
                        command.permission = "*"
                    }
                }
            }
        }
    }

    @Ghost
    @SubscribeEvent
    fun e(e: PlayerCommandSendEvent) {
        if (!e.player.isOp && Yesod.config.getBoolean("command-block") && Yesod.isEnable()) {
            e.commands.removeAll(Yesod.config.getStringList("block-command-name").toSet())
            e.commands.removeAll(Yesod.config.getStringList("block-command-send").toSet())
            e.commands.removeIf { it.contains(":") }
        }
    }

    @SubscribeEvent
    fun e(e: PlayerCommandPreprocessEvent) {
        if (e.player.isOp || !Yesod.config.getBoolean("command-block") && Yesod.isEnable()) {
            return
        }
        val v = e.message.split(" ")[0].lowercase(Locale.getDefault()).substring(1)
        if (v.contains(":") || v in Yesod.config.getStringList("block-command-name")) {
            e.isCancelled = true
            e.player.sendMessage(SpigotConfig.unknownCommandMessage)
        }
    }
}
