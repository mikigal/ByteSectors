package pl.mikigal.bytesectors.system.listener;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import pl.mikigal.bytesectors.system.ByteSectorsSystem;
import pl.mikigal.bytesectors.system.configuration.SectorsConfiguration;
import pl.mikigal.bytesectors.system.util.Utils;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(ChatEvent event) {
        if (event.isCancelled() || event.isCommand()) {
            return;
        }

        event.setCancelled(true);
        ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
        String message = Utils.fixColors(SectorsConfiguration.getChatFormat())
                .replace("{PLAYER}", sender.getName())
                .replace("{MESSAGE}", event.getMessage());

        for (ProxiedPlayer player : ByteSectorsSystem.getInstance().getProxy().getPlayers()) {
            player.sendMessage(new TextComponent(message));
        }
    }
}
