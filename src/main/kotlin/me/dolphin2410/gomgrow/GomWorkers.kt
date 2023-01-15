package me.dolphin2410.gomgrow

import io.github.monun.tap.fake.FakeEntity
import io.github.monun.tap.mojangapi.MojangAPI
import me.dolphin2410.gomgrow.GomGrow.Companion.gompowder
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

data class GomWorker(val npc: FakeEntity<Player>, val type: GomWorkers.Worker, val owner: Player) {
    fun click(whoClicked: Player) {
        val skills = GomEconomy.economy.computeIfAbsent(this) { ArrayList() }
        val maxSkill = skills.filterIsInstance<GomEconomy.ClickSkill>().lastOrNull()?.buff(1) ?: 1
        whoClicked.inventory.addItem(gompowder.clone().apply { amount = maxSkill })
    }

    fun auto(owner: Player) {
        val skills = GomEconomy.economy.computeIfAbsent(this) { ArrayList() }
        val autoSkill = skills.filterIsInstance<GomEconomy.AutoSkill>().lastOrNull()?.buff(0) ?: return
        owner.inventory.addItem(gompowder.clone().apply { amount = autoSkill })
    }
}

object GomWorkers {
    enum class Worker(val relPos: Vector) {
        GOMPOWDER(Vector(0, 0, 0)),
        SOLOMIRO(Vector(1, 0, -1)),
        DOLPHIN2410(Vector(-1, 0, -1))
    }

    fun createWorker(owner: Player, prev: GomWorker, worker: Worker): GomWorker {
        val location = prev.npc.location.clone().add(worker.relPos.subtract(prev.type.relPos))
        return createWorker(owner, location, worker)
    }

    fun createWorker(owner: Player, location: Location, worker: Worker): GomWorker {
        val uuid = MojangAPI.fetchProfile(worker.name)!!.uuid()
        val profile = MojangAPI.fetchSkinProfile(uuid)!!.profileProperties().toSet()
        val player = GomGrow.instance.fakeServer.spawnPlayer(location, worker.name, profile)
        val gomWorker = GomWorker(player, worker, owner)
        GomEconomy.entities.computeIfAbsent(owner) { ArrayList() }.add(gomWorker)
        object: BukkitRunnable() {
            override fun run() {
                gomWorker.auto(owner)
            }
        }.runTaskTimer(GomGrow.instance, 0, 20)
        return gomWorker
    }
}