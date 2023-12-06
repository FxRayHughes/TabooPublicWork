package ink.work.taboopublicwork.module.tpa.command

import ink.work.taboopublicwork.api.ICommand
import ink.work.taboopublicwork.module.tpa.ModuleTpa
import ink.work.taboopublicwork.module.tpa.ModuleTpa.sendLang
import ink.work.taboopublicwork.module.tpa.TpaPlayerData.askTimeout
import ink.work.taboopublicwork.module.tpa.TpaPlayerData.getAsked
import ink.work.taboopublicwork.module.tpa.TpaPlayerData.isTimeout
import ink.work.taboopublicwork.utils.evalKether
import ink.work.taboopublicwork.utils.tpDelay
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand


@CommandHeader("tpaccept", aliases = ["tpyes"], description = "传送", permission = "taboopublicwork.command.tpa")
object TpAcceptCommand : ICommand {

    override val command: String = "tpaccept"

    @Awake(LifeCycle.LOAD)
    fun init() {
        register(ModuleTpa)
    }

    @CommandBody
    val main = mainCommand {

        execute<Player> { sender, _, _ ->
            val playerName = getAsked.computeIfAbsent(sender) {
                arrayListOf()
            }.lastOrNull { Bukkit.getPlayerExact(it)?.isTimeout(sender.name) == false }
                ?: return@execute sender.sendLang("module-tpa-ask-timeout-any")

            sender.acceptTp(playerName)
        }

        dynamic("player") {
            suggestion<Player>(uncheck = true) { sender, _ ->
                getAsked.computeIfAbsent(sender) {
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

                sender.acceptTp(playerName)
            }
        }
    }

    private fun Player.acceptTp(player: String) {
        val sentPlayer = Bukkit.getPlayerExact(player)!!
        this.sendLang("module-tpa-accept-target", player)
        sentPlayer.sendLang("module-tpa-accept-player", this.name)
        val location = this.location
        val delay = ModuleTpa.config.getLong("tpa-delay", 0)

//        submitAsync(delay = ModuleTpa.config.getLong("tpa-delay", 0)) {
//            sentPlayer.teleport(location)
//        }
        sentPlayer.tpDelay(delay, location, start = {
            ModuleTpa.config.getString("actions.accept.self", "")!!.evalKether(this, mapOf("target" to sentPlayer.name)).thenRun {
                ModuleTpa.config.getString("actions.accept.sent", "")!!.evalKether(sentPlayer, mapOf("target" to this.name))
            }
        }, error = {
            ModuleTpa.config.getString("actions.cancel.self", "")!!.evalKether(this, mapOf("target" to sentPlayer.name)).thenRun {
                ModuleTpa.config.getString("actions.cancel.sent", "")!!.evalKether(sentPlayer, mapOf("target" to this.name))
            }
        })


        askTimeout[sentPlayer]!!.timeout.remove(this.name)
        getAsked[this]!!.remove(player)
    }

}
