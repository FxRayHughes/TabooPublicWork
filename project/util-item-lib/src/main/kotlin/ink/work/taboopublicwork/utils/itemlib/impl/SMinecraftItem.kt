package ink.work.taboopublicwork.utils.itemlib.impl

import ink.work.taboopublicwork.utils.itemlib.SourceItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.library.xseries.parseToItemStack
import taboolib.platform.util.hasLore
import taboolib.platform.util.hasName

object SMinecraftItem : SourceItem {

    override val source: String = "Minecraft"
    override val priority: Int = 6

    @Awake(LifeCycle.ENABLE)
    fun init() {
        registerItem()
    }


    override fun getItem(player: Player, id: String): ItemStack? {
        return try {
            id.parseToItemStack()
        } catch (e: Exception) {
            null
        }
    }

    override fun getItemId(itemStack: ItemStack): String? {
        if (itemStack.hasItemMeta() || itemStack.hasName() || itemStack.hasLore()) {
            return null
        }
        return itemStack.type.name
    }

}
