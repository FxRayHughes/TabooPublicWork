package ink.work.taboopublicwork.module.warp.data

import org.bukkit.Bukkit
import org.bukkit.Location
import taboolib.module.configuration.Configuration
import java.util.*

data class WarpData(
    var name: String = "NONE",
    var world: UUID = UUID.randomUUID(),
    var worldName: String = "World",
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var yaw: Float = 0F,
    var pitch: Float = 0F,
    var lastOwner: UUID = UUID.randomUUID(),
) {

    constructor(config: Configuration) : this() {
        name = config.getString("name", "NONE")!!
        world = UUID.fromString(config.getString("world", UUID.randomUUID().toString())!!)
        worldName = config.getString("worldName", "world")!!
        x = config.getDouble("x", 0.0)
        y = config.getDouble("y", 0.0)
        z = config.getDouble("z", 0.0)
        yaw = config.getInt("yaw", 0).toFloat()
        pitch = config.getInt("pitch", 0).toFloat()
        lastOwner = UUID.fromString(config.getString("lastOwner", UUID.randomUUID().toString())!!)
    }

    fun toLocation(): Location {
        val bukkitWorld = Bukkit.getWorld(world) ?: Bukkit.getWorld(worldName) ?: error("World not found: $worldName")
        return Location(bukkitWorld, x, y, z, yaw, pitch)
    }

}
