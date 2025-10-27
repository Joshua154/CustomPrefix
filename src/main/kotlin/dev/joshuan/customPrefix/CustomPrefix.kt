package dev.joshuan.customPrefix

import dev.joshuan.customPrefix.listener.ChatListener
import dev.joshuan.customPrefix.listener.NametagListener
import net.luckperms.api.LuckPerms
import net.luckperms.api.event.EventBus
import net.luckperms.api.event.EventSubscription
import net.luckperms.api.event.node.NodeMutateEvent
import net.luckperms.api.event.user.UserDataRecalculateEvent
import net.luckperms.api.model.user.User
import net.luckperms.api.node.NodeType
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin




class CustomPrefix : JavaPlugin() {

    lateinit var luckPerms: LuckPerms private set
    private var lpEventSubscription: EventSubscription<UserDataRecalculateEvent>? = null

    override fun onEnable() {
        Log.init(slF4JLogger)

        val provider = server.servicesManager.getRegistration(LuckPerms::class.java)
        if (provider == null) {
            logger.severe("LuckPerms not found! This plugin requires LuckPerms.")
            server.pluginManager.disablePlugin(this)
            return
        }
        this.luckPerms = provider.provider

        registerLuckPermsListener()


        server.pluginManager.registerEvents(ChatListener(this), this)
        server.pluginManager.registerEvents(NametagListener(this), this)

        logger.info("CustomPrefix has been enabled!")
    }

    override fun onDisable() {
        lpEventSubscription?.close()

        LuckpermsUtil.unregisterAllCustomTeams(server)

        Log.info("CustomPrefix has been disabled.")
    }

    private fun registerLuckPermsListener() {
        val lp = this.luckPerms
        val eventBus: EventBus = lp.eventBus

        eventBus.subscribe(this, NodeMutateEvent::class.java, ::onNodeMutate)
    }

    private fun onNodeMutate(event: NodeMutateEvent) {
        if (!event.isUser) return

        val user = event.target as? User ?: return
        val player = Bukkit.getPlayer(user.uniqueId) ?: return


        val setAfter = event.dataAfter.filter { it.type == NodeType.PREFIX || it.type == NodeType.SUFFIX }.toSet()

        val setBefore = event.dataBefore.filter { it.type == NodeType.PREFIX || it.type == NodeType.SUFFIX }.toSet()

        val addedNodes = setAfter.subtract(setBefore)


        if (addedNodes.isNotEmpty()) {
            Bukkit.getScheduler().runTask(this, Runnable {
                LuckpermsUtil.applyNameTag(player, this)
            })
        }
    }
}
