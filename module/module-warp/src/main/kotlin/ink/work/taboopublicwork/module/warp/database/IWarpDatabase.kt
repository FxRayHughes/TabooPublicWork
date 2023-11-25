package ink.work.taboopublicwork.module.warp.database

import ink.work.taboopublicwork.module.warp.data.WarpData

interface IWarpDatabase {

    fun load()

    fun getData(name: String): WarpData?

    fun getDataList(): List<WarpData>

    fun addData(warpData: WarpData)

    fun removeData(name: String)

}
