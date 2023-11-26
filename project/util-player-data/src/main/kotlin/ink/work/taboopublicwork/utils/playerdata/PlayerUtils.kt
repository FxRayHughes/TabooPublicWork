package ink.work.taboopublicwork.utils.playerdata

import org.bukkit.OfflinePlayer

fun OfflinePlayer.getDataString(key: String): String? {
    return PlayerDatabase.database.get(this, key)
}

fun OfflinePlayer.getDataString(key: String, default: String): String {
    return PlayerDatabase.database.get(this, key) ?: default
}

fun OfflinePlayer.setDataString(key: String, value: String?) {
    PlayerDatabase.database.set(this, key, value)
}

fun OfflinePlayer.getDataKeys(): Set<String> {
    return PlayerDatabase.database.keys(this)
}

fun OfflinePlayer.removeDataString(key: String) {
    PlayerDatabase.database.remove(this, key)
}

fun OfflinePlayer.loadData() {
    PlayerDatabase.database.load(this)
}

fun OfflinePlayer.saveData() {
    PlayerDatabase.database.save(this)
}

fun OfflinePlayer.hasDataString(key: String): Boolean {
    return PlayerDatabase.database.get(this, key) != null
}
