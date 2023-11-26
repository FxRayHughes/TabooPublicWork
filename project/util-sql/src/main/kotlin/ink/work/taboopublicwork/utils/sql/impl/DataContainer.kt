package ink.work.taboopublicwork.utils.sql.impl

import taboolib.common.platform.Schedule
import taboolib.common.platform.function.submitAsync
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Zaphkiel
 * ink.ptms.zaphkiel.module.Vars
 *
 * @author sky
 * @since 2021/8/22 7:51 下午
 */
class DataContainer(val database: Database) {

    private val source = database.getMap()
    private val updateMap = ConcurrentHashMap<String, Long>()

    operator fun set(key: String, value: Any) {
        source[key] = value.toString()
        save(key)
    }

    fun setDelayed(key: String, value: Any, delay: Long = 3L, timeUnit: TimeUnit = TimeUnit.SECONDS) {
        source.set(key, value.toString())
        updateMap[key] = System.currentTimeMillis() - timeUnit.toMillis(delay)
    }

    operator fun get(key: String): String? {
        return source[key]
    }

    fun keys(): Set<String> {
        return source.keys
    }

    fun values(): Map<String, String> {
        return source
    }

    fun size(): Int {
        return source.size
    }

    fun save(key: String) {
        submitAsync {
            database.set(key, source[key]!!)
        }
    }

    fun remove(key: String) {
        submitAsync {
            database.remove(key)
        }
    }

    fun checkUpdate() {
        updateMap.filterValues { it < System.currentTimeMillis() }.forEach { (t, _) ->
            updateMap.remove(t)
            save(t)
        }
    }
}
