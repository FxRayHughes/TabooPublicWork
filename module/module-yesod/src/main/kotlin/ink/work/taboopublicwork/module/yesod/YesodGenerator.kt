package ink.work.taboopublicwork.module.yesod

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import java.util.*

class YesodGenerator : ChunkGenerator() {

    override fun getDefaultPopulators(world: World): List<BlockPopulator> {
        return emptyList()
    }

    override fun canSpawn(world: World, x: Int, z: Int): Boolean {
        return true
    }

    fun xyzToByte(x: Int, y: Int, z: Int): Int {
        return (x * 16 + z) * 128 + y
    }

    fun generate(world: World?, rand: Random?, cx: Int, cz: Int): ByteArray {
        val result = ByteArray(32768)
        if (cx == 0 && cz == 0) {
            result[xyzToByte(0, 64, 0)] = Material.BEDROCK.id.toByte()
        }
        return result
    }

    override fun getFixedSpawnLocation(world: World, random: Random): Location {
        return Location(world, 0.0, 66.0, 0.0)
    }

    override fun generateChunkData(world: World, random: Random, x: Int, z: Int, biome: BiomeGrid): ChunkData {
        return createChunkData(world)
    }
}
