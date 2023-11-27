package ink.work.taboopublicwork.utils

import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common5.Coerce
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions

fun List<String>.evalKether(player: Player?, vars: Map<String, Any> = mapOf()) {
    KetherShell.eval(this, ScriptOptions.builder().apply {
        namespace(listOf("adyeshach", "adyeshach-inner", "chemdah", "taboo-public-work"))
        sender(player ?: console())
        vars(vars)
    }.build())
}

fun String.evalKether(player: Player?, vars: Map<String, Any> = mapOf()) {
    KetherShell.eval(this, ScriptOptions.builder().apply {
        namespace(listOf("adyeshach", "adyeshach-inner", "chemdah", "taboo-public-work"))
        sender(player ?: console())
        vars(vars)
    }.build())
}

fun String.evalKetherBoolean(player: Player?, vars: Map<String, Any> = mapOf()): Boolean {
    if (this.isEmpty()) {
        return true
    }
    return try {
        KetherShell.eval(this, ScriptOptions.builder().apply {
            namespace(listOf("adyeshach", "adyeshach-inner", "chemdah", "taboo-public-work"))
            sender(player ?: console())
            vars(vars)
        }.build()).thenApply {
            Coerce.toBoolean(it)
        }.get()
    } catch (_: Exception) {
        false
    }
}

fun List<String>.evalKetherBoolean(player: Player?, vars: Map<String, Any> = mapOf()): Boolean {
    if (this.isEmpty()) {
        return true
    }
    return try {
        KetherShell.eval(this, ScriptOptions.builder().apply {
            namespace(listOf("adyeshach", "adyeshach-inner", "chemdah", "taboo-public-work"))
            sender(player ?: console())
            vars(vars)
        }.build()).thenApply {
            Coerce.toBoolean(it)
        }.get()
    } catch (_: Exception) {
        false
    }
}
