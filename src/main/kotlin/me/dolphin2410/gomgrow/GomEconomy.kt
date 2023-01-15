package me.dolphin2410.gomgrow

import io.github.monun.tap.fake.FakeEntity
import me.dolphin2410.gomgrow.GomGrow.Companion.gompowder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.min

object GomEconomy {
    interface Skill {
        fun onAdd(npc: FakeEntity<Player>)

        fun buff(prev: Int): Int
    }

    enum class ClickSkill: Skill {
        IRON {
            override fun onAdd(npc: FakeEntity<Player>) {
                npc.updateEquipment {
                    helmet = ItemStack(Material.IRON_HELMET)
                    chestplate = ItemStack(Material.IRON_CHESTPLATE)
                    leggings = ItemStack(Material.IRON_LEGGINGS)
                    boots = ItemStack(Material.IRON_BOOTS)
                }
            }

            override fun buff(prev: Int): Int {
                return prev + 1
            }
        },
        DIA {
            override fun onAdd(npc: FakeEntity<Player>) {
                npc.updateEquipment {
                    helmet = ItemStack(Material.DIAMOND_HELMET)
                    chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
                    leggings = ItemStack(Material.DIAMOND_LEGGINGS)
                    boots = ItemStack(Material.DIAMOND_BOOTS)
                }
            }

            override fun buff(prev: Int): Int {
                return prev + 2
            }
        },
        NETHERITE {
            override fun onAdd(npc: FakeEntity<Player>) {
                npc.updateEquipment {
                    helmet = ItemStack(Material.NETHERITE_HELMET)
                    chestplate = ItemStack(Material.NETHERITE_CHESTPLATE)
                    leggings = ItemStack(Material.NETHERITE_LEGGINGS)
                    boots = ItemStack(Material.NETHERITE_BOOTS)
                }
            }

            override fun buff(prev: Int): Int {
                return prev + 3
            }
        }
    }

    enum class AutoSkill: Skill {
        IRON {
            override fun onAdd(npc: FakeEntity<Player>) {
                npc.updateEquipment { setItemInMainHand(ItemStack(Material.IRON_SWORD)) }
            }

            override fun buff(prev: Int): Int {
                return prev + 1
            }
        },
        DIA {
            override fun onAdd(npc: FakeEntity<Player>) {
                npc.updateEquipment { setItemInMainHand(ItemStack(Material.DIAMOND_SWORD)) }
            }

            override fun buff(prev: Int): Int {
                return prev + 2
            }
        },
        NETHERITE {
            override fun onAdd(npc: FakeEntity<Player>) {
                npc.updateEquipment { setItemInMainHand(ItemStack(Material.NETHERITE_SWORD)) }
            }

            override fun buff(prev: Int): Int {
                return prev + 3
            }
        }
    }
    val entities = HashMap<Player, ArrayList<GomWorker>>()

    val economy = HashMap<GomWorker, ArrayList<Skill>>()

    fun playerHasSkill(player: GomWorker, skill: Skill): Boolean {
        return economy.computeIfAbsent(player) { ArrayList() }.contains(skill)
    }

    fun playerAddSkill(player: GomWorker, skill: Skill) {
        economy.computeIfAbsent(player) { ArrayList() }
        economy[player]!!.add(skill)
        skill.onAdd(player.npc)
    }

    fun playerRemoveSkill(player: GomWorker, skill: Skill) {
        economy.computeIfAbsent(player) { ArrayList() }
        economy[player]!!.remove(skill)
    }

    fun getPlayerGompowder(player: Player): Int {
        var amount = 0
        player.inventory.filter { it?.isSimilar(gompowder) == true }.forEach {
            amount += it.amount
        }
        return amount
    }

    fun usePlayerGompowder(player: Player, _amount: Int) {
        var amount = _amount
        player.inventory.filter { it?.isSimilar(gompowder) == true }.forEach {
            if (amount <= 0) return
            it.amount -= min(amount, 64)
            amount -= 64
        }
    }
}