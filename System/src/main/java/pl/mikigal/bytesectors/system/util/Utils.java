package pl.mikigal.bytesectors.system.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.mikigal.bytesectors.system.ByteSectorsSystem;

public class Utils {

    public static String fixColors(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(ProxiedPlayer sender, String message) {
        sender.sendMessage(new TextComponent(fixColors(message)));
    }

    public static void log(Object object) {
        ByteSectorsSystem.getInstance().getProxy().getConsole().sendMessage(new TextComponent(fixColors("&4[ByteSectorsClient] &c" + object.toString())));
    }

    public static void log(Exception exception) {
        log("&4&l[EXCEPTION] " + exception.getMessage());
        throw new RuntimeException(exception);
    }

}
