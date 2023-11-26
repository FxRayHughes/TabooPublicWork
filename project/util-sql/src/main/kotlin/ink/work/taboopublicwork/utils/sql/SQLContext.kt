package ink.work.taboopublicwork.utils.sql

import ink.work.taboopublicwork.TabooPublicWork
import ink.work.taboopublicwork.api.IModule
import ink.work.taboopublicwork.utils.sql.impl.DataContainer
import ink.work.taboopublicwork.utils.sql.impl.Database
import ink.work.taboopublicwork.utils.sql.impl.TypeSQL

import taboolib.module.database.HostSQL

interface SQLContext {

    var database: Database

    /**
     *  初始化DataSource
     *
     *  1. 首先判断子模块的配置文件是否声明了数据源局域化 （database.public = false）
     *  2. 局域化：就是每个模块来自不同的数据库 不然默认来自于一个数据库 只需要指定数据库的表
     *  3. 如果没有声明局域化 则从公共数据源导入
     *  4. 模块配置文件可以缺省，省略的内容都从公共配置文件获取
     *  5. 比如 password 在子文件没有写 那么就读取公共文件的password
     *  6. 表名不写就默认为是 模块名小写_database
     */
    fun initDataSource(module: IModule) {
        // 首先判断子文件是否写入了数据源
        var host = TabooPublicWork.conf.getString("database.host", "localhost")
        var port = TabooPublicWork.conf.getString("database.port", "3306")
        var user = TabooPublicWork.conf.getString("database.user", "root")
        var password = TabooPublicWork.conf.getString("database.password", "root")
        var database = TabooPublicWork.conf.getString("database.database", "minecraft")
        // 判断私有源
        if (!module.config.getBoolean("database.public", true)) {
            module.config.getString("database.host", "Inheritance")?.let {
                if (it != "Inheritance") {
                    host = it
                }
            }
            module.config.getString("database.port", "Inheritance")?.let {
                if (it != "Inheritance") {
                    port = it
                }
            }
            module.config.getString("database.user", "Inheritance")?.let {
                if (it != "Inheritance") {
                    user = it
                }
            }
            module.config.getString("database.password", "Inheritance")?.let {
                if (it != "Inheritance") {
                    password = it
                }
            }
            module.config.getString("database.database", "Inheritance")?.let {
                if (it != "Inheritance") {
                    database = it
                }
            }
        }
        val table = module.config.getString("database.table", "${module.name.lowercase()}_database")!!
        val hostSql = HostSQL(host!!, port!!, user!!, password!!, database!!)
        this.database = Database(TypeSQL(hostSql, table))
    }


    /**
     *  创建数据库链接对象
     *  请保证次方法仅执行一次 通常使用by lazy 或 手写单例模式
     */
    fun buildContainer(iModule: IModule, load: SQLContext.() -> Unit = {}): DataContainer {

        initDataSource(iModule)
        load.invoke(this)
        return DataContainer(database)
    }


}

