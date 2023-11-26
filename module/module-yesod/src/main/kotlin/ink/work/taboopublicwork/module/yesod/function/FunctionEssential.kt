package ink.ptms.yesod.function

import ink.work.taboopublicwork.module.yesod.Yesod
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Container
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
import org.bukkit.inventory.meta.CrossbowMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.colored
import taboolib.platform.util.isAir

/**
 * @author sky
 * @since 2021/3/11 10:37 上午
 */
object FunctionEssential {

    /**
     * 牌子颜色
     */
    @SubscribeEvent
    fun e(e: SignChangeEvent) {
        if (e.player.hasPermission("yesod.color") && Yesod.isEnable()) {
            (0..3).forEach { e.setLine(it, e.getLine(it)?.colored() ?: "") }
        }
    }

    /**
     * 进入提示
     */
    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        val message = Yesod.config["join-message"]
        if (message == null || message.toString().isEmpty()) {
            e.joinMessage = null
        } else {
            e.joinMessage = message.toString().colored().replace("@p", e.player.name)
        }

    }

    /**
     * 离开提示
     */
    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        val message = Yesod.config["quit-message"]
        if (message == null || message.toString().isEmpty()) {
            e.quitMessage = null
        } else {
            e.quitMessage = message.toString().colored().replace("@p", e.player.name)
        }
    }

    /**
     * 非创造模式使用鼠标中键点击弩卸载已装备的箭
     */
    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: InventoryClickEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if (!Yesod.config.getBoolean("crossbow-unload", true)) {
            return
        }
        if (e.click == ClickType.MIDDLE && e.currentItem?.type == Material.CROSSBOW && e.whoClicked.gameMode != GameMode.CREATIVE && e.cursor.isAir()) {
            val meta = e.currentItem!!.itemMeta as? CrossbowMeta
            if (meta?.chargedProjectiles == null) {
                return
            }
            val charged = meta.chargedProjectiles.toMutableList()
            if (charged.isEmpty()) {
                return
            }
            e.isCancelled = true
            e.whoClicked.setItemOnCursor(charged.removeAt(0))
            e.currentItem!!.itemMeta = meta.run {
                this.setChargedProjectiles(charged)
                this
            }
            (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.ITEM_CROSSBOW_LOADING_END, 1f, 1f)
        }
    }

    /**
     * 允许玩家生存模式放下带有 NBT 结构的容器
     */
    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlace(e: BlockPlaceEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if (e.blockPlaced.state is Container && isContainer(e.itemInHand)) {
            try {
                (e.blockPlaced.state as Container).inventory.contents = ((e.itemInHand.itemMeta as BlockStateMeta).blockState as Container).snapshotInventory.contents
            } catch (t: Throwable) {
                e.isCancelled = true
                e.player.sendMessage("§cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.")
                t.printStackTrace()
            }
        }
    }

    /**
     * 允许玩家喝下带有饱和效果的药水
     */
    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onConsume(e: PlayerItemConsumeEvent) {
        if (!Yesod.isEnable()) {
            return
        }
        if (e.item.itemMeta is PotionMeta) {
            (e.item.itemMeta as PotionMeta).customEffects.forEach {
                if (it.type == PotionEffectType.SATURATION) {
                    e.player.addPotionEffect(it)
                }
            }
        }
    }

    fun isContainer(item: ItemStack): Boolean {
        return item.itemMeta is BlockStateMeta && (item.itemMeta as BlockStateMeta).blockState is Container
    }
}
