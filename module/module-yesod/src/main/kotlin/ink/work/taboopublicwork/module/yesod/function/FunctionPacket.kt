package ink.work.taboopublicwork.module.yesod.function

import com.mojang.brigadier.suggestion.Suggestions
import ink.work.taboopublicwork.module.yesod.Yesod
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.CraftingInventory
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.xseries.XSound
import taboolib.module.nms.PacketReceiveEvent
import taboolib.module.nms.PacketSendEvent
import java.util.concurrent.ConcurrentHashMap

/**
 * @author sky
 * @since 2019-11-20 21:49
 */
object FunctionPacket {

    val bite = ConcurrentHashMap<String, Int>()
    val entityPackets = arrayOf("PacketPlayOutEntityVelocity", "PacketPlayOutEntityMetadata", "PacketPlayOutEntityStatus", "PacketPlayOutEntityEffect")

    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        bite.remove(e.player.name)
    }

    @SubscribeEvent
    fun e(e: PlayerKickEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        bite.remove(e.player.name)
    }

    @SubscribeEvent
    fun e(e: PacketSendEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if (e.packet.name == "PacketPlayOutChat" && e.packet.read<String>("a").toString().contains("chat.type.advancement")) {
            e.isCancelled = true
        }
        if (e.packet.name == "PacketPlayOutTabComplete" && !e.player.isOp) {
            if (e.packet.read<Suggestions>("b")!!.list.any { Bukkit.getPlayerExact(it.text) == null }) {
                return
            }
            e.isCancelled = true
        }
        if (e.packet.name == "PacketPlayOutWorldParticles" && e.packet.read<Any>("j")!!.invokeMethod<String>("a")!! == "minecraft:damage_indicator") {
            e.isCancelled = true
        }
        if (e.packet.name in entityPackets && e.packet.read<Int>("a") == bite[e.player.name]) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: PacketReceiveEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if (e.packet.name == "PacketPlayInAutoRecipe" || e.packet.name == "PacketPlayInRecipeDisplayed") {
            if (!Yesod.allowCraftDisplay) {
                e.isCancelled = true
            }
        }
        if (e.packet.name == "PacketPlayInUseItem" || e.packet.name == "PacketPlayInUseEntity" || e.packet.name == "PacketPlayInArmAnimation") {
            if (e.player.openInventory.topInventory !is CraftingInventory) {
                e.isCancelled = true
            }
        }
    }

    @SubscribeEvent
    fun e(e: PlayerFishEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if (!Yesod.config.getBoolean("anti-auto-fishing", true)) {
            return
        }
        if (e.state == PlayerFishEvent.State.REEL_IN) {
            // 声音欺骗
            val hook = e.invokeMethod<Entity>("getHook")!!
            submit(delay = 20) {
                XSound.ENTITY_FISHING_BOBBER_SPLASH.play(hook.location, 0f, 0f)
            }
            submit(delay = 40) {
                XSound.ENTITY_FISHING_BOBBER_SPLASH.play(hook.location, 0f, 0f)
            }
        }
        if (e.state == PlayerFishEvent.State.BITE) {
            bite[e.player.name] = e.invokeMethod<Entity>("getHook")!!.entityId
            submit(delay = 40) {
                bite.remove(e.player.name)
            }
        }
    }
}
