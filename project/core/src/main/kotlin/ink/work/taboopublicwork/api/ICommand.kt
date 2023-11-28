package ink.work.taboopublicwork.api

import ink.work.taboopublicwork.TabooPublicWork

/**
 *  仅作为标记接口
 *
 *  用于标记模块 根据启动情况取消命令注册
 */
interface ICommand {

    val command: String

    fun register(iModule: IModule) {
        TabooPublicWork.modulesCommand.computeIfAbsent(iModule.id) {
            arrayListOf()
        }.add(this)
    }

}
