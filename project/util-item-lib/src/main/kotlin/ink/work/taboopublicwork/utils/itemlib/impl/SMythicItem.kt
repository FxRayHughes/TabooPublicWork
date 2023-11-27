package ink.work.taboopublicwork.utils.itemlib.impl

import ink.ptms.um.Mythic
import ink.work.taboopublicwork.utils.itemlib.SourceItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object SMythicItem : SourceItem {

    override val source: String = "MythicItem"
    override val priority: Int = 4

    @Awake(LifeCycle.ENABLE)
    fun init() {
        registerItem("MythicMobs")
    }

    override fun getItem(player: Player, id: String): ItemStack? {
        return Mythic.API.getItemStack(id)
    }

    override fun getItemId(itemStack: ItemStack): String? {
        return Mythic.API.getItemId(itemStack)
    }
}
