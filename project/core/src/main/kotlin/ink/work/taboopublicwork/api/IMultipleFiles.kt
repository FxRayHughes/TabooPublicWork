package ink.work.taboopublicwork.api

import taboolib.module.configuration.Configuration
import java.io.File

interface IMultipleFiles {

    val files: MutableList<Configuration>

    fun loadFile(file: File) {
        if (file.isFile) {
            files.add(Configuration.loadFromFile(file))
        } else {
            file.listFiles()?.forEach {
                loadFile(it)
            }
        }
    }

}
