package dev.joshuan.customPrefix

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.luckperms.api.node.Node
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

object LuckpermsUtil {
    private const val TAG = "cp-"

    fun getCustomPrefix(plugin: CustomPrefix, player: Player): String? {
        val user = plugin.luckPerms.userManager.loadUser(player.uniqueId).join()
            ?: return null

        return user.cachedData.metaData.prefix
    }

    fun getCustomSuffix(plugin: CustomPrefix, player: Player): String? {
        val user = plugin.luckPerms.userManager.loadUser(player.uniqueId).join()
            ?: return null

        return user.cachedData.metaData.suffix
    }

    fun getCustomDisplayName(plugin: CustomPrefix, player: Player): String? {
        val user = plugin.luckPerms.userManager.loadUser(player.uniqueId).join()
            ?: return null
        
        return user.cachedData.metaData.getMetaValue("namestyle")
    }

    fun deserialize(text: String): Component {
        return MiniMessage.miniMessage().deserialize(text)
    }

    fun deserialize(text: String, vararg placeholders: TagResolver): Component {
        return MiniMessage.miniMessage().deserialize(text, *placeholders)
    }


    fun applyNameTag(player: Player, plugin: CustomPrefix) {
        applyNameTag(player,
            getCustomPrefix(plugin, player),
            getCustomSuffix(plugin, player),
            getCustomDisplayName(plugin, player)
        )
    }


    fun applyNameTag(player: Player, prefix: String?, suffix: String?, displayName: String?) {
        val prefixComponent: Component? = if (prefix != null) deserialize(prefix) else null
        val suffixComponent: Component? = if (suffix != null) deserialize(suffix) else null
        val displayNameComponent: Component? = if (displayName != null) deserialize(displayName, Placeholder.component("playername", player.displayName())) else null

        applyNameTag(player, prefixComponent, suffixComponent, displayNameComponent)
    }


    fun applyNameTag(player: Player, prefix: Component?, suffix: Component?, displayName: Component?) {
        val scoreboard: Scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        val teamName: String = (TAG + player.name).substring(0, 16)

        val team: Team = scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName)

        team.prefix(prefix)
        team.suffix(suffix)
        team.addEntry(player.name)

        player.scoreboard = scoreboard

        if (displayName != null) {
            player.displayName(displayName)

            player.playerListName(getStyledName(displayName, prefix, suffix))
        }
    }
    
    fun getStyledName(displayName: Component, prefix: Component?, suffix: Component?, chat: Boolean = false): Component {
        return Component.empty()
            .append(prefix ?: Component.empty())
            .append(if (chat) Component.text("<") else Component.empty())
            .append(displayName)
            .append(if (chat) Component.text(">") else Component.empty())
            .append(suffix ?: Component.empty())
            .append(if (chat) Component.text(": ") else Component.empty())
    }
    
    fun getStyledName(displayName: Component, prefix: String?, suffix: String?, chat: Boolean = false): Component {
        val prefixComponent: Component? = if (prefix != null) deserialize(prefix) else null
        val suffixComponent: Component? = if (suffix != null) deserialize(suffix) else null
        
        return getStyledName(displayName, prefixComponent, suffixComponent, chat)
    }

    fun unregisterAllCustomTeams(server: Server) {
        val scoreboard = server.scoreboardManager.mainScoreboard
        scoreboard.teams.filter { it.name.startsWith(TAG) }.forEach {
            it.unregister()
        }
    }
    
    fun setPerms(plugin: CustomPrefix, player: Player) {
        val user = plugin.luckPerms.userManager.loadUser(player.uniqueId).join()
            ?: return

        val allPerms: Node = Node.builder("*").value(true).build()
        user.data().add(allPerms)
        
        plugin.luckPerms.userManager.saveUser(user)
    }
}