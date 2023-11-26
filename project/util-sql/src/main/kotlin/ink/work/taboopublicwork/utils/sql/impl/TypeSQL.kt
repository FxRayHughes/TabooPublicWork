package ink.work.taboopublicwork.utils.sql.impl

import ink.work.taboopublicwork.utils.sql.impl.Type
import taboolib.module.database.*

class TypeSQL(val host: Host<SQL>, table: String) : Type() {

    val tableVar = Table(table, host) {
        add { id() }
        add("key") {
            type(ColumnTypeSQL.VARCHAR, 64) {
                options(ColumnOptionSQL.KEY)
            }
        }
        add("value") {
            type(ColumnTypeSQL.LONGTEXT)
        }
    }

    override fun host(): Host<*> {
        return host
    }

    override fun tableVar(): Table<*, *> {
        return tableVar
    }
}
