package ink.work.taboopublicwork.module.tpa.command

import ink.work.taboopublicwork.api.ICommand
import ink.work.taboopublicwork.module.tpa.ModuleTpa
import ink.work.taboopublicwork.module.tpa.ModuleTpa.sendLang
import ink.work.taboopublicwork.module.tpa.TpaPlayerData
import ink.work.taboopublicwork.module.tpa.TpaPlayerData.askTime
import ink.work.taboopublicwork.module.tpa.TpaPlayerData.askTimeout
import ink.work.taboopublicwork.module.tpa.TpaPlayerData.getAsked
import ink.work.taboopublicwork.module.tpa.TpaPlayerData.isTimeout
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.expansion.createHelper

@CommandHeader("tpa", aliases = ["tpask"], description = "传送", permission = "taboopublicwork.command.tpa")
object TpaCommand : ICommand {

    override val command: String = "tpa"

    @Awake(LifeCycle.LOAD)
    fun init() {
        register(ModuleTpa)
    }

    @CommandBody
    val main = mainCommand {
        dynamic("target") {
            suggestion<CommandSender> { sender, _ ->
                if (sender is Player) {
                    Bukkit.getOnlinePlayers().map { it.name }.filter { it != sender.name }
                } else {
                    Bukkit.getOnlinePlayers().map { it.name }
                }
            }
            execute<Player> { sender, context, _ ->
                val target = context["target"]
                sender.sendTpAsk(target)
            }
            dynamic("player") {
                suggestion<CommandSender> { _, _ ->
                    Bukkit.getOnlinePlayers().map { it.name }
                }
                execute<CommandSender> { sender, context, _ ->
                    val player = Bukkit.getPlayerExact(context["player"]) ?: return@execute sender.sendLang("module-tpa-target-not-online", context["player"])
                    val target = context["target"]
                    player.sendTpAsk(target)
                }
            }
        }
        createHelper()
    }

    private fun Player.sendTpAsk(target: String) {
        if (this.name == target) {
            return
        }
        if (!this.isTimeout(target)) {
            this.sendLang("module-tpa-ask-too-fast", target)
            return
        }
        val tPlayer = Bukkit.getPlayerExact(target) ?: return this.sendLang("module-tpa-target-not-online", target)
        this.sendLang("module-tpa-ask-sent", target, askTime)

        tPlayer.sendLang("module-tpa-target-get", this.name, askTime)

        // 给玩家 sender 对 target 设置超时
        askTimeout.computeIfAbsent(this) {
            TpaPlayerData.TeleportTimeout(this)
        }.timeout[target] = System.currentTimeMillis() + (askTime * 1000)

        getAsked.computeIfAbsent(tPlayer) {
            arrayListOf()
        }.add(this.name)
    }
}
