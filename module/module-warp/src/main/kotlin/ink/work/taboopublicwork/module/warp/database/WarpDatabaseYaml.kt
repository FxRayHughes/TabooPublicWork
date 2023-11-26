package ink.work.taboopublicwork.module.warp.database

import ink.work.taboopublicwork.api.IMultipleFiles
import ink.work.taboopublicwork.module.warp.ModuleWarp
import ink.work.taboopublicwork.module.warp.data.WarpData
import taboolib.common.io.newFile
import taboolib.module.configuration.Configuration
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class WarpDatabaseYaml : IWarpDatabase, IMultipleFiles {

    private val data = ConcurrentHashMap<String, WarpData>()

    override val files = mutableListOf<Configuration>()

    override fun load() {
        data.clear()
        files.clear()
        loadFile(File(ModuleWarp.getFilePath(), "warps"))

        files.forEach {
            WarpData(it).let { warpData ->
                data[warpData.name] = warpData
            }
        }
    }

    override fun getData(name: String): WarpData? {
        return data[name]
    }

    override fun getDataList(): List<WarpData> {
        return data.values.toList()
    }

    override fun addData(warpData: WarpData) {
        data[warpData.name] = warpData
        val newFile = newFile(File(ModuleWarp.getFilePath(), "warps"), "${warpData.name}.yml", create = true)
        val configuration = Configuration.loadFromFile(newFile)
        configuration["name"] = warpData.name
        configuration["world"] = warpData.world.toString()
        configuration["worldName"] = warpData.worldName
        configuration["x"] = warpData.x
        configuration["y"] = warpData.y
        configuration["z"] = warpData.z
        configuration["yaw"] = warpData.yaw
        configuration["pitch"] = warpData.pitch
        configuration["lastOwner"] = warpData.lastOwner.toString()
        configuration.saveToFile()
    }

    override fun removeData(name: String) {
        data.remove(name)
        val file = File(ModuleWarp.getFilePath(), "warps/${name}.yml")
        if (file.exists()) {
            file.delete()
        }
    }
}
