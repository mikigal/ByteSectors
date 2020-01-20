package pl.mikigal.bytesectors.client.util;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Listener;
import pl.mikigal.bytesectors.client.ByteSectorsClient;
import pl.mikigal.bytesectors.client.command.Command;

import java.lang.reflect.Field;

public class RegisterUtils {

    private static SimpleCommandMap commandMap;

    static {
        try {
            Class craftServerClass = Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().substring(23) + ".CraftServer");
            Field f = craftServerClass.getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (SimpleCommandMap) f.get(craftServerClass.cast(ByteSectorsClient.getInstance().getServer()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void register(Command... commands) {
        for (Command command : commands) {
            commandMap.register(command.getName(), command);
        }
    }

    public static void register(Listener... listeners) {
        for (Listener listener : listeners) {
            ByteSectorsClient.getInstance().getServer().getPluginManager().registerEvents(listener, ByteSectorsClient.getInstance());
        }
    }

}
