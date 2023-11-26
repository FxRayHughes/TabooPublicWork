package ink.work.taboopublicwork.utils.sql.impl

import java.util.concurrent.ConcurrentHashMap
import javax.sql.DataSource

class Database(val type: Type, val dataSource: DataSource = type.host().createDataSource()) {

    init {
        type.tableVar().createTable(dataSource)
    }

    fun getMap(): MutableMap<String, String> {
        return type.tableVar().select(dataSource) {
            rows("key", "value")
        }.map {
            getString("key") to getString("value")
        }.toMap(ConcurrentHashMap())
    }

    fun getValue(name: String): String? {
        return type.tableVar().select(dataSource) {
            rows("value")
            where("key" eq name)
            limit(1)
        }.firstOrNull {
            getString("value")
        }
    }

    fun set(name: String, data: String) {
        type.tableVar().workspace(dataSource){
            if (getValue(name) == null) {
                insert("key", "value") { value(name, data) }
            } else {
                update {
                    set("value", data)
                    where("key" eq name)
                }
            }
        }.run()
    }

    fun remove(name: String) {
        type.tableVar().delete(dataSource) {
            where("key" eq name)
        }
    }
}
