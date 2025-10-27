package dev.joshuan.customPrefix.listener

import dev.joshuan.customPrefix.CustomPrefix
import dev.joshuan.customPrefix.LuckpermsUtil
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatListener(private val plugin: CustomPrefix) : Listener {

    @EventHandler
    fun onPlayerChat(event: AsyncChatEvent) {
        val player = event.player

        val prefix = LuckpermsUtil.getCustomPrefix(plugin, player)
        val suffix = LuckpermsUtil.getCustomSuffix(plugin, player)

        event.renderer { _, sourceDisplayName: Component, message: Component, _ ->
            LuckpermsUtil.getStyledName(sourceDisplayName, prefix, suffix, true)
                .append(Component.space())
                .append(message)
        }
    }
}