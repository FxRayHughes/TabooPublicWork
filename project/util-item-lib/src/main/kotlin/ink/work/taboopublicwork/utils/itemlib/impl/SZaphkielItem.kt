package ink.work.taboopublicwork.utils.itemlib.impl

import ink.ptms.zaphkiel.Zaphkiel
import ink.ptms.zaphkiel.impl.item.toExtensionStreamOrNull
import ink.work.taboopublicwork.utils.itemlib.SourceItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object SZaphkielItem : SourceItem {

    override val source: String = "Zaphkiel"
    override val priority: Int = 0

    @Awake(LifeCycle.ENABLE)
    fun init() {
        registerItem("Zaphkiel")
    }

    private val itemManager by lazy {
        Zaphkiel.api().getItemManager()
    }

    override fun getItem(player: Player, id: String): ItemStack? {
        return itemManager.getItem(id)?.buildItemStack(player)
    }

    override fun getItemId(itemStack: ItemStack): String? {
        return itemStack.toExtensionStreamOrNull()?.getZaphkielId()
    }
}
