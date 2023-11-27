package ink.work.taboopublicwork.utils.itemlib.impl

import github.saukiya.sxitem.SXItem
import ink.work.taboopublicwork.utils.itemlib.SourceItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object SSXItem : SourceItem {

    override val source: String = "SxItem"

    override val priority: Int = 0

    @Awake(LifeCycle.ENABLE)
    fun init() {
        registerItem("SX-Item")
    }

    private val itemManager by lazy {
        SXItem.getItemManager()
    }


    override fun getItem(player: Player, id: String): ItemStack? {
        if (!id.contains(":")) {
            return itemManager.getItem(id, player)
        }
        val args = id.split(":")
        val idz = args[0]
        val arg = args.toList().drop(1)
        return itemManager.getItem(idz, player, *arg.toTypedArray())
    }

    override fun getItemId(itemStack: ItemStack): String? {
        return itemManager.getGenerator(itemStack)?.key
    }
}
