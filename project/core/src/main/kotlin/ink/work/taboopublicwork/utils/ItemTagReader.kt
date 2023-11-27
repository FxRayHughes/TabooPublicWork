package ink.work.taboopublicwork.utils

import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import java.util.*

/**
 *  更简单的使用ItemTag
 *
 *  注: 如果修改了ItemTag 需要使用 write 设置回去
 */
fun ItemTag.reader(reader: ItemTagReader.() -> Unit) {
    val itemTagReader = ItemTagReader(this)
    reader.invoke(itemTagReader)
}

fun ItemStack?.itemTagReader(reader: ItemTagReader.() -> Unit) {
    val item = this.ifAir() ?: return
    item.getItemTag().reader(reader)
}

data class ItemTagReader(
    val itemTag: ItemTag
) {

    fun getString(key: String, def: String): String {
        return itemTag.getDeepOrElse(key, ItemTagData(def)).asString()
    }

    fun getString(key: String): String? {
        return itemTag.getDeep(key)?.asString()
    }

    fun getInt(key: String, def: Int): Int {
        return itemTag.getDeepOrElse(key, ItemTagData(def)).asInt()
    }

    fun getInt(key: String): Int? {
        return itemTag.getDeep(key)?.asInt()
    }

    fun getDouble(key: String, def: Double): Double {
        return itemTag.getDeepOrElse(key, ItemTagData(def)).asDouble()
    }

    fun getDouble(key: String): Double? {
        return itemTag.getDeep(key)?.asDouble()
    }

    fun getStringList(key: String): List<String> {
        return itemTag.getDeep(key)?.asList()?.map { it.asString() } ?: listOf()
    }

    fun getDoubleList(key: String): List<Double> {
        return itemTag.getDeep(key)?.asList()?.map { it.asDouble() } ?: listOf()
    }

    fun getBoolean(key: String, def: Boolean): Boolean {
        return getString(key, def.toString()).toBoolean()
    }

    fun getBoolean(key: String): Boolean {
        return getString(key)?.toBoolean() ?: false
    }

    fun getLong(key: String, def: Long): Long {
        return itemTag.getDeepOrElse(key, ItemTagData(def)).asLong()
    }

    fun getLong(key: String): Long? {
        return itemTag.getDeep(key)?.asLong()
    }

    fun getFloat(key: String, def: Float): Float {
        return itemTag.getDeepOrElse(key, ItemTagData(def)).asFloat()
    }

    fun getFloat(key: String): Float? {
        return itemTag.getDeep(key)?.asFloat()
    }

    fun getByte(key: String, def: Byte): Byte {
        return itemTag.getDeepOrElse(key, ItemTagData(def)).asByte()
    }

    fun getByte(key: String): Byte? {
        return itemTag.getDeep(key)?.asByte()
    }

    fun write(itemStack: ItemStack) {
        itemTag.saveTo(itemStack)
    }

    fun saveToItem(itemStack: ItemStack) {
        itemTag.saveTo(itemStack)
    }

    fun putAll(map: Map<String, Any?>) {
        itemTag.putAll(map.mapNotNull { it.key to ItemTagData.toNBT(it.value) })
    }

    operator fun set(key: String, value: Any?) {
        if (value == null) {
            itemTag.removeDeep(key)
        } else {
            if (value is UUID) {
                itemTag.putDeep(key, value.toString())
                return
            }
            itemTag.putDeep(key, value)
        }
    }

    fun remove(key: String) {
        itemTag.removeDeep(key)
    }

    fun close(): ItemTag {
        return itemTag
    }


}
