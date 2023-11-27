package ink.work.taboopublicwork.utils.itemlib.impl

import ac.github.oa.internal.core.item.ItemInstance
import ac.github.oa.internal.core.item.ItemPlant
import ink.work.taboopublicwork.utils.itemlib.SourceItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
object SOriginAttributeItem : SourceItem {

    override val source: String = "OriginItem"

    override val priority: Int = 1

    @Awake(LifeCycle.ENABLE)
    fun init() {
        registerItem("OriginAttribute")
    }

    override fun getItem(player: Player, id: String): ItemStack? {
        if (!id.contains(":")) {
            return ItemPlant.build(player, id)
        }
        val args = id.split(":")
        val idz = args[0]
        val arg = args.toList().drop(1)
        return ItemPlant.build(player, idz, toMap(arg))
    }

    fun toMap(list: List<String>): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        list.forEachIndexed { index, value ->
            if (index % 2 == 0) {
                map[value] = list[index + 1]
            } else {
                map[value] = ""
            }
        }
        return map
    }


    override fun getItemId(itemStack: ItemStack): String? {
        return ItemInstance.get(itemStack)?.item?.id
    }
}
