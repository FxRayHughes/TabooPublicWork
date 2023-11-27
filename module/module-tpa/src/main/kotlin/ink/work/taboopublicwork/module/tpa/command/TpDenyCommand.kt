package ink.work.taboopublicwork.module.tpa.command

import ink.work.taboopublicwork.module.tpa.ModuleTpa
import ink.work.taboopublicwork.module.tpa.ModuleTpa.sendLang
import ink.work.taboopublicwork.module.tpa.TpaPlayerData
import ink.work.taboopublicwork.module.tpa.TpaPlayerData.isTimeout
import ink.work.taboopublicwork.utils.evalKether
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand

@CommandHeader("tpdeny", aliases = ["tpno"], description = "传送", permission = "taboopublicwork.command.tpa")
object TpDenyCommand {

    @CommandBody
    val main = mainCommand {

        execute<Player> { sender, _, _ ->
            val playerName = TpaPlayerData.getAsked.computeIfAbsent(sender) {
                arrayListOf()
            }.lastOrNull { Bukkit.getPlayerExact(it)?.isTimeout(sender.name) == false }
                ?: return@execute sender.sendLang("module-tpa-ask-timeout-any")

            sender.denyTp(playerName)
        }

        dynamic("player") {
            suggestion<Player>(uncheck = true) { sender, _ ->
                TpaPlayerData.getAsked.computeIfAbsent(sender) {
                    arrayListOf()
                }.filter { Bukkit.getPlayerExact(it)?.isTimeout(sender.name) == false }
            }

            execute<Player> { sender, context, _ ->
                val playerName = context["player"]
                val sentPlayer = Bukkit.getPlayerExact(playerName) ?: return@execute sender.sendLang("module-tpa-target-not-online", playerName)
                if (sentPlayer.isTimeout(sender.name)) {
                    sender.sendLang("module-tpa-ask-timeout", playerName)
                    return@execute
                }

                sender.denyTp(playerName)
            }
        }
    }

    private fun Player.denyTp(player: String) {
        val sentPlayer = Bukkit.getPlayerExact(player)!!
        this.sendLang("module-tpa-deny-target", player)
        sentPlayer.sendLang("module-tpa-deny-player", this.name)

        ModuleTpa.config.getString("actions.deny.self", "")!!.evalKether(this).thenRun {
            ModuleTpa.config.getString("actions.deny.sent", "")!!.evalKether(sentPlayer)
        }

        TpaPlayerData.askTimeout[sentPlayer]!!.timeout.remove(this.name)
        TpaPlayerData.getAsked[this]!!.remove(player)
    }

}