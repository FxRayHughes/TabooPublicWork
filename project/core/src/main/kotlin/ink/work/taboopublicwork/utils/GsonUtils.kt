package ink.work.taboopublicwork.utils

import com.google.gson.GsonBuilder
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

val gsonUtils = GsonBuilder()
    .apply {

    }.create()!!

fun Location.saveToString(): String {
    return gsonUtils.toJson(LocationSave(this))
}

fun String.fromSaveLocation(): Location {
    return gsonUtils.fromJson(this, LocationSave::class.java).toLocation()
}

data class LocationSave(
    val world: String,
    val worldUuid: UUID,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) {
    constructor(location: Location) : this(
        location.world!!.name,
        location.world!!.uid,
        location.x,
        location.y,
        location.z,
        location.yaw,
        location.pitch
    )

    fun toLocation(): Location {
        return Location(Bukkit.getWorld(worldUuid) ?: Bukkit.getWorld(world), x, y, z, yaw, pitch)
    }
}
