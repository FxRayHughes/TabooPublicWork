package ink.ptms.yesod.function

import ink.work.taboopublicwork.module.yesod.Yesod
import ink.work.taboopublicwork.module.yesod.Yesod.bypass
import org.bukkit.Material
import org.bukkit.entity.Hanging
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.*
import org.bukkit.event.entity.*
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.player.*
import org.bukkit.event.raid.RaidTriggerEvent
import org.bukkit.util.Vector
import taboolib.common.platform.Ghost
import taboolib.common.platform.command.simpleCommand
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.common.util.Location
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.util.setLocation
import taboolib.platform.util.attacker
import taboolib.platform.util.toBukkitLocation
import taboolib.platform.util.toProxyLocation

/**
 * @author sky
 * @since 2019-11-20 21:21
 */
object FunctionWorld {

    init {
        if (Yesod.isEnable() && Yesod.config.getBoolean("enable-world-spawn", true)) {
            simpleCommand("setserverspawn", permission = "admin") { sender, args ->
                val player = sender.castSafely<Player>() ?: return@simpleCommand
                val loc = player.location.clone()
                Yesod.data.setLocation("spawn", loc.toProxyLocation())
                sender.sendMessage("服务器出生点已被重设在${loc.x},${loc.y},${loc.z}(${loc.yaw},${loc.pitch})")
            }
        }

    }

    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if (Yesod.data.contains("spawn") && Yesod.config.getBoolean("enable-world-spawn", true)) {
            submit(delay = 20) {
                e.player.teleport(Yesod.data.getLocation("spawn")!!.toBukkitLocation())
            }
        }
    }

    @SubscribeEvent
    fun e(e: PlayerRespawnEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if (Yesod.data.contains("spawn") && Yesod.config.getBoolean("enable-world-spawn", true)) {
            e.respawnLocation = Yesod.data.getLocation("spawn")!!.toBukkitLocation()
        }
    }

    @SubscribeEvent
    fun e(e: EntityBreedEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("BREED" in Yesod.blockFeatures) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: EntityCombustEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if (Yesod.config.getBoolean("block-monster-burn")) {
            if (e.entity !is Player) {
                e.isCancelled = true
            }
        }
    }

    @SubscribeEvent
    fun e(e: LeavesDecayEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("LEAVES_DECAY" in Yesod.blockFeatures) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: EntityChangeBlockEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("ENTITY_CHANGE_BLOCK" in Yesod.blockFeatures && e.entity is LivingEntity) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: PlayerInteractEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("FARMLAND_PHYSICAL" in Yesod.blockFeatures && e.action == Action.PHYSICAL && e.clickedBlock!!.type == Material.FARMLAND) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: EntityInteractEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("FARMLAND_PHYSICAL" in Yesod.blockFeatures && e.block.type == Material.FARMLAND) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: HangingBreakEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("HANGING_BREAK" in Yesod.blockFeatures && e.cause != HangingBreakEvent.RemoveCause.ENTITY) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: HangingBreakByEntityEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("HANGING_BREAK" in Yesod.blockFeatures && e.remover?.bypass(true) == false) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: PlayerInteractEntityEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("HANGING_BREAK" in Yesod.blockFeatures && !e.player.bypass(true) && e.rightClicked is Hanging) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: EntityDamageByEntityEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        val player = e.attacker ?: return
        if ("HANGING_BREAK" in Yesod.blockFeatures && !player.bypass(true) && e.entity is Hanging) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: PlayerTeleportEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        e.isCancelled = e.cause.name in Yesod.blockTeleport
    }

    @SubscribeEvent
    fun e(e: EntityExplodeEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("ENTITY_EXPLODE" in Yesod.blockFeatures) {
            e.blockList().clear()
        }
    }

    @SubscribeEvent
    fun e(e: BlockExplodeEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("BLOCK_EXPLODE" in Yesod.blockFeatures) {
            e.blockList().clear()
        }
    }

    @Ghost
    @SubscribeEvent
    fun e(e: RaidTriggerEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("RAID" in Yesod.blockFeatures) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: BlockSpreadEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("SPREAD" in Yesod.blockFeatures) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: BlockGrowEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if ("GROW" in Yesod.blockFeatures) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: PlayerMoveEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        val to = e.to!!
        if (e.from.x != to.x || e.from.y != to.y || e.from.z != to.z) {
            if (to.y < 10 && Yesod.voidProtect) {
                e.isCancelled = true
                // 返回大厅
                submit {
                    e.player.velocity = Vector(0, 0, 0)
                    e.player.teleport(e.player.world.spawnLocation)
                }
            }
        }
    }

    private fun ConfigurationSection.getLocation(path: String): Location? {
        getConfigurationSection(path)?.let { section ->
            return Location(
                section.getString("world"),
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                section.getDouble("yaw").toFloat(),
                section.getDouble("pitch").toFloat()
            )
        } ?: return null
    }
}
