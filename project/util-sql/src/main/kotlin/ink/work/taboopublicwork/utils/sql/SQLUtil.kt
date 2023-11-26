package ink.work.taboopublicwork.utils.sql

import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object SQLUtil {

    @Config(value = "sql.yml")
    lateinit var config: ConfigFile

}
