package ink.work.taboopublicwork.utils.sql.impl

import ink.work.taboopublicwork.utils.sql.impl.Type
import taboolib.common.io.newFile
import taboolib.common.platform.function.pluginId
import taboolib.module.database.ColumnTypeSQLite
import taboolib.module.database.Host
import taboolib.module.database.Table
import taboolib.module.database.getHost
import java.io.File

class TypeSQLite(file: File) : Type() {

    val host = newFile(file).getHost()

    val tableVar = Table(pluginId, host) {
        add("key") {
            type(ColumnTypeSQLite.TEXT, 64)
        }
        add("value") {
            type(ColumnTypeSQLite.TEXT)
        }
    }

    override fun host(): Host<*> {
        return host
    }

    override fun tableVar(): Table<*, *> {
        return tableVar
    }
}
