package me.dolphin2410.gomgrow

import io.github.monun.invfx.openFrame
import io.github.monun.kommand.kommand
import io.github.monun.tap.fake.FakeEntityServer
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import net.kyori.adventure.text.Component.text
import net.minecraft.network.protocol.game.ServerboundInteractPacket
import net.minecraft.world.entity.Pose
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class GomGrow: JavaPlugin(), Listener {
    companion object {
        val gompowder = ItemStack(Material.GUNPOWDER).apply {
            editMeta {
                it.displayName(text("Gompowder"))
            }
        }

        lateinit var instance: GomGrow
    }

    lateinit var fakeServer: FakeEntityServer

    override fun onEnable() {
        fakeServer = FakeEntityServer.create(this)
        server.scheduler.runTaskTimer(this, fakeServer::update, 0L, 1L)
        server.pluginManager.registerEvents(this, this)
        instance = this

        kommand {
            register("gomgrow") {
                requires { isPlayer }
                then("reset") {
                    executes {
                        val iter = (GomEconomy.entities[player] ?: return@executes).iterator()
                        while(iter.hasNext()) {
                            val entity = iter.next()
                            GomEconomy.economy.remove(entity)
                            entity.npc.remove()
                            iter.remove()
                        }
                    }
                }

                then("create") {
                    executes {
                        val location = player.location.apply {
                            yaw = 0f
                            pitch = 0f
                        }

                        GomWorkers.createWorker(player, location, GomWorkers.Worker.GOMPOWDER)
                    }
                }
            }
        }
    }

    fun togglePlayer(whoClicked: Player, self: GomWorker) = object: BukkitRunnable() {
        override fun run() {
            if (self.owner != whoClicked) return
            GomEconomy.entities.computeIfAbsent(whoClicked) { ArrayList() }.forEach {
                val sneaking = (self.npc.bukkitEntity as CraftPlayer).handle.pose == Pose.CROUCHING
                it.npc.updateMetadata {
                    val nmsPlayer = (this as CraftPlayer).handle
                    if (sneaking) {
                        nmsPlayer.pose = Pose.STANDING
                    } else {
                        nmsPlayer.pose = Pose.CROUCHING
                    }
                }
                it.click(whoClicked)
            }
        }
    }.runTask(this)

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        fakeServer.addPlayer(e.player)
        val channel = (e.player as CraftPlayer).handle.connection.connection.channel

        channel.pipeline().addBefore("packet_handler", e.player.name, object: ChannelDuplexHandler() {
            override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                super.channelRead(ctx, msg)
                if (msg is ServerboundInteractPacket) {
                    val target = GomEconomy.entities[e.player]?.find { it.npc.bukkitEntity.entityId == msg.entityId }
                    if (target != null) {
                        if (msg.actionType == ServerboundInteractPacket.ActionType.ATTACK) {
                            togglePlayer(e.player, target)
                        } else {
                            object: BukkitRunnable() {
                                override fun run() {
                                    e.player.openFrame(GomFrames.mainFrame(target))
                                }
                            }.runTask(this@GomGrow)
                        }
                    }
                }
            }
        })
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        fakeServer.removePlayer(e.player)

        val channel = (e.player as CraftPlayer).handle.connection.connection.channel
        channel.eventLoop().submit {
            channel.pipeline().remove(e.player.name)
        }
    }
}