package ink.work.taboopublicwork.module.spawner

import ink.work.taboopublicwork.utils.getString
import ink.work.taboopublicwork.utils.ifAir
import ink.work.taboopublicwork.utils.itemTagReader
import org.bukkit.Material
import org.bukkit.block.CreatureSpawner
import org.bukkit.entity.EntityType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.SpawnEggMeta
import taboolib.common.platform.event.SubscribeEvent

object OtherListener {

    //    # 是否允许玩家手持刷怪蛋设置刷怪笼内容
//    # 这会读取刷怪蛋的NBT 用于设置一些额外属性
//    # 如果物品不是刷怪蛋 但是NBT里包含 SpawnData.spawnedType 也会被认为是刷怪蛋
//    # NBT:
//    # SpawnData:
//    #  delay: Integer (设置刷怪笼刷怪延迟.)
//    #  maxNearbyEntities: Integer (设置刷怪范围内允许出现的相似实体的最大数量.)
//    #  maxSpawnDelay: Tick (设置刷怪笼刷怪的最大延迟.)
//    #  minSpawnDelay: Tick (设置刷怪笼刷怪的最小延迟.)
//    #  requiredPlayerRange: Integer (设置玩家使刷怪笼激活所需的最远距离.)
//    #  spawnCount: Integer (设置刷怪笼每次刷怪的数量.)
//    #  spawnedType: String (设置刷怪笼刷出的实体类型.) https://bukkit.windit.net/javadoc/org/bukkit/entity/EntityType.html
//    #  spawnRange: Integer (设置刷怪笼刷怪的范围.)
    @SubscribeEvent
    fun setSpawner(event: PlayerInteractEvent) {
        if (!ModuleSpanwer.isEnable() || !ModuleSpanwer.config.getBoolean("allow-set-spawner")) {
            return
        }
        val block = event.clickedBlock ?: return
        if (block.type == Material.AIR) {
            return
        }
        val state = block.state
        val item = event.item?.ifAir() ?: return
        val itemMeta = item.itemMeta ?: return
        if (state is CreatureSpawner && (itemMeta is SpawnEggMeta || item.getString("SpawnData.spawnedType") != "null")) {
            if (itemMeta is SpawnEggMeta) {
                val valueOf = item.getString("SpawnData.spawnedType")
                if (valueOf != "null") {
                    state.spawnedType = EntityType.valueOf(valueOf)
                }
                state.spawnedType = itemMeta.spawnedType
            } else {
                state.spawnedType = EntityType.valueOf(item.getString("SpawnData.spawnedType"))
            }

            item.itemTagReader {
                getInt("SpawnData.delay")?.let {
                    state.delay = it
                }
                getInt("SpawnData.maxNearbyEntities")?.let {
                    state.maxNearbyEntities = it
                }
                getInt("SpawnData.maxSpawnDelay")?.let {
                    state.maxSpawnDelay = it
                }
                getInt("SpawnData.minSpawnDelay")?.let {
                    state.minSpawnDelay = it
                }
                getInt("SpawnData.requiredPlayerRange")?.let {
                    state.requiredPlayerRange = it
                }
                getInt("SpawnData.spawnCount")?.let {
                    state.spawnCount = it
                }
                getInt("SpawnData.spawnRange")?.let {
                    state.spawnRange = it
                }
            }
            state.update()
            item.amount -= 1

        }
    }

}
