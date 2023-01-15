package me.dolphin2410.gomgrow

import io.github.monun.invfx.InvFX.frame
import io.github.monun.invfx.frame.InvFrame
import io.github.monun.invfx.openFrame
import io.github.monun.tap.fake.FakeEntity
import me.dolphin2410.gomgrow.GomWorkers.createWorker
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object GomFrames {
    fun autoFrame(npc: GomWorker): InvFrame {
        return frame(3, text("자동 강화")) {
            slot(1, 1) {
                item = ItemStack(Material.IRON_SWORD).apply {
                    editMeta {
                        it.displayName(text("철 세트", NamedTextColor.AQUA))
                        it.lore(listOf(text("초당 1개"), text("가격: Gompowder 10세트")))
                    }
                }

                onClick {
                    if (GomEconomy.getPlayerGompowder(it.whoClicked as Player) >= 10 * 64) {
                        GomEconomy.usePlayerGompowder(it.whoClicked as Player, 10 * 64)
                        GomEconomy.playerAddSkill(npc, GomEconomy.AutoSkill.IRON)
                    } else {
                        it.whoClicked.sendMessage("Gompowder 10세트가 필요합니다")
                    }
                }
            }

            slot (4, 1) {
                item = ItemStack(Material.DIAMOND_SWORD).apply {
                    editMeta {
                        it.displayName(text("다이아 세트", NamedTextColor.AQUA))
                        it.lore(listOf(text("초당 2개"), text("철 세트 필요"), text("가격: Gompowder 15세트")))
                    }
                }

                onClick {
                    if (!GomEconomy.playerHasSkill(npc, GomEconomy.AutoSkill.IRON)) {
                        it.whoClicked.sendMessage("철 세트가 필요합니다")
                        return@onClick
                    }
                    if (GomEconomy.getPlayerGompowder(it.whoClicked as Player) < 15 * 64) {
                        it.whoClicked.sendMessage("Gompowder 15세트가 필요합니다")
                        return@onClick
                    }

                    GomEconomy.usePlayerGompowder(it.whoClicked as Player, 15 * 64)
                    GomEconomy.playerAddSkill(npc, GomEconomy.AutoSkill.DIA)
                    GomEconomy.playerRemoveSkill(npc, GomEconomy.AutoSkill.IRON)
                }
            }

            slot (7, 1) {
                item = ItemStack(Material.NETHERITE_SWORD).apply {
                    editMeta {
                        it.displayName(text("네더라이트 세트", NamedTextColor.AQUA))
                        it.lore(listOf(text("초당 3개"), text("다이아 세트 필요"), text("가격: Gompowder 20세트")))
                    }
                }

                onClick {
                    if (!GomEconomy.playerHasSkill(npc, GomEconomy.AutoSkill.DIA)) {
                        it.whoClicked.sendMessage("다이아 세트가 필요합니다")
                        return@onClick
                    }
                    if (GomEconomy.getPlayerGompowder(it.whoClicked as Player) < 20 * 64) {
                        it.whoClicked.sendMessage("Gompowder 20세트가 필요합니다")
                        return@onClick
                    }

                    GomEconomy.usePlayerGompowder(it.whoClicked as Player, 20 * 64)
                    GomEconomy.playerAddSkill(npc, GomEconomy.AutoSkill.NETHERITE)
                    GomEconomy.playerRemoveSkill(npc, GomEconomy.AutoSkill.DIA)
                }
            }
        }
    }

    fun clickFrame(npc: GomWorker): InvFrame {
        return frame(3, text("클릭 강화")) {
            slot(1, 1) {
                item = ItemStack(Material.IRON_HELMET).apply {
                    editMeta {
                        it.displayName(text("철 세트", NamedTextColor.AQUA))
                        it.lore(listOf(text("클릭 시 Gompowder 2개"), text("가격: Gompowder 7세트")))
                    }
                }

                onClick {
                    if (GomEconomy.getPlayerGompowder(it.whoClicked as Player) >= 7 * 64) {
                        GomEconomy.usePlayerGompowder(it.whoClicked as Player, 7 * 64)
                        GomEconomy.playerAddSkill(npc, GomEconomy.ClickSkill.IRON)
                    } else {
                        it.whoClicked.sendMessage("Gompowder 7세트가 필요합니다")
                    }
                }
            }

            slot (4, 1) {
                item = ItemStack(Material.DIAMOND_HELMET).apply {
                    editMeta {
                        it.displayName(text("다이아 세트", NamedTextColor.AQUA))
                        it.lore(listOf(text("클릭 시 Gompowder 3개"), text("철 세트 필요"), text("가격: Gompowder 10세트")))
                    }
                }

                onClick {
                    if (!GomEconomy.playerHasSkill(npc, GomEconomy.ClickSkill.IRON)) {
                        it.whoClicked.sendMessage("철 세트가 필요합니다")
                        return@onClick
                    }
                    if (GomEconomy.getPlayerGompowder(it.whoClicked as Player) < 10 * 64) {
                        it.whoClicked.sendMessage("Gompowder 10세트가 필요합니다")
                        return@onClick
                    }

                    GomEconomy.usePlayerGompowder(it.whoClicked as Player, 10 * 64)
                    GomEconomy.playerAddSkill(npc, GomEconomy.ClickSkill.DIA)
                    GomEconomy.playerRemoveSkill(npc, GomEconomy.ClickSkill.IRON)
                }
            }

            slot (7, 1) {
                item = ItemStack(Material.NETHERITE_HELMET).apply {
                    editMeta {
                        it.displayName(text("네더라이트 세트", NamedTextColor.AQUA))
                        it.lore(listOf(text("클릭 시 Gompowder 4개"), text("다이아 세트 필요"), text("가격: Gompowder 15세트")))
                    }
                }

                onClick {
                    if (!GomEconomy.playerHasSkill(npc, GomEconomy.ClickSkill.DIA)) {
                        it.whoClicked.sendMessage("다이아 세트가 필요합니다")
                        return@onClick
                    }
                    if (GomEconomy.getPlayerGompowder(it.whoClicked as Player) < 15 * 64) {
                        it.whoClicked.sendMessage("Gompowder 15세트가 필요합니다")
                        return@onClick
                    }

                    GomEconomy.usePlayerGompowder(it.whoClicked as Player, 15 * 64)
                    GomEconomy.playerAddSkill(npc, GomEconomy.ClickSkill.NETHERITE)
                    GomEconomy.playerRemoveSkill(npc, GomEconomy.ClickSkill.DIA)
                }
            }
        }
    }

    fun friendsFrame(worker: GomWorker): InvFrame {
        return frame(3, text("친구 찬스")) {
            slot(1, 1) {
                item = ItemStack(Material.PLAYER_HEAD).apply {
                    editMeta {
                        it.displayName(text("Gompowder"))
                        it.lore(listOf(text("DEFAULT")))
                    }
                }
            }

            slot(4, 1) {
                item = ItemStack(Material.PLAYER_HEAD).apply {
                    editMeta {
                        it.displayName(text("SOLOMIRO"))
                        it.lore(listOf(text("클릭 당 2배"), text("가격: Gompowder 15세트")))
                    }
                }

                onClick {
                    if (GomEconomy.getPlayerGompowder(it.whoClicked as Player) < 15 * 64) {
                        it.whoClicked.sendMessage("Gompowder 15세트가 필요합니다")
                        return@onClick
                    }
                    if (GomEconomy.entities[it.whoClicked as Player]?.any { a -> a.npc.bukkitEntity.name == "SOLOMIRO" } == true) {
                        it.whoClicked.sendMessage("이미 고용하였습니다")
                        return@onClick
                    }
                    GomEconomy.usePlayerGompowder(it.whoClicked as Player, 15 * 64)
                    val gompowder = GomEconomy.entities[it.whoClicked as Player]!!.find { it.npc.bukkitEntity.name == "GOMPOWDER" }!!
                    createWorker(it.whoClicked as Player, gompowder, GomWorkers.Worker.SOLOMIRO)
                }
            }

            slot(7, 1) {
                item = ItemStack(Material.PLAYER_HEAD).apply {
                    editMeta {
                        it.displayName(text("DOLPHIN2410"))
                        it.lore(listOf(text("클릭 당 3배"), text("가격: Gompowder 20세트")))
                    }
                }

                onClick {
                    if (GomEconomy.getPlayerGompowder(it.whoClicked as Player) < 20 * 64) {
                        it.whoClicked.sendMessage("Gompowder 20세트가 필요합니다")
                        return@onClick
                    }
                    if (GomEconomy.entities[it.whoClicked as Player]?.any { a -> a.npc.bukkitEntity.name == "DOLPHIN2410" } == true) {
                        it.whoClicked.sendMessage("이미 고용하였습니다")
                        return@onClick
                    }
                    val gompowder = GomEconomy.entities[it.whoClicked as Player]!!.find { it.npc.bukkitEntity.name == "GOMPOWDER" }!!
                    GomEconomy.usePlayerGompowder(it.whoClicked as Player, 20 * 64)
                    createWorker(it.whoClicked as Player, gompowder, GomWorkers.Worker.DOLPHIN2410)
                }
            }
        }
    }
    
    fun mainFrame(npc: GomWorker) = frame(3, text("스킬 구매")) {
        slot(1, 1) {
            item = ItemStack(Material.IRON_HELMET).apply {
                editMeta {
                    it.displayName(text("클릭 강화", NamedTextColor.AQUA))
                }
            }

            onClick {
                (it.whoClicked as Player).openFrame(clickFrame(npc))
            }
        }

        slot(4, 1) {
            item = ItemStack(Material.IRON_SWORD).apply {
                editMeta {
                    it.displayName(text("자동 강화", NamedTextColor.AQUA))
                }
            }

            onClick {
                (it.whoClicked as Player).openFrame(autoFrame(npc))
            }
        }

        slot(7, 1) {
            item = ItemStack(Material.PLAYER_HEAD).apply {
                editMeta {
                    it.displayName(text("친구 찬스", NamedTextColor.AQUA))
                }
            }

            onClick {
                (it.whoClicked as Player).openFrame(friendsFrame(npc))
            }
        }
    }
}