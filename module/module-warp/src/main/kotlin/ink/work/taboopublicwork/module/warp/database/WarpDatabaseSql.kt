package ink.work.taboopublicwork.module.warp.database

import ink.work.taboopublicwork.module.warp.ModuleWarp
import ink.work.taboopublicwork.module.warp.data.WarpData
import ink.work.taboopublicwork.utils.gsonUtils
import ink.work.taboopublicwork.utils.sql.SQLContext
import ink.work.taboopublicwork.utils.sql.impl.Database
import java.util.concurrent.ConcurrentHashMap

class WarpDatabaseSql : SQLContext, IWarpDatabase {

    override lateinit var database: Database

    private val cache = ConcurrentHashMap<String, WarpData>()

    private val container by lazy {
        buildContainer(ModuleWarp) {
        }
    }

    init {
        container.keys()
    }

    override fun load() {
        cache.clear()
        container.values().forEach { (t, u) ->
            cache[t] = gsonUtils.fromJson(u, WarpData::class.java)
        }
    }

    override fun getData(name: String): WarpData? {
        val data = cache[name]
        if (data != null) {
            return data
        }
        val json = container[name]
        if (json != null) {
            val warpData = gsonUtils.fromJson(json, WarpData::class.java)
            cache[name] = warpData
            return warpData
        }
        return null
    }

    override fun getDataList(): List<WarpData> {
        return cache.values.toList()
    }

    override fun addData(warpData: WarpData) {
        cache[warpData.name] = warpData
        container[warpData.name] = gsonUtils.toJson(warpData)
    }

    override fun removeData(name: String) {
        cache.remove(name)
        container.remove(name)
    }

}
