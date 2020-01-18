package pl.mikigal.bytesectors.client.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.mikigal.bytesectors.client.ByteSectorsClient;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActionBarAPI {

    private static String nms = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
    private static Class<?> craftPlayerClass;
    private static Class<?> packetPlayOutChatClass;
    private static Class<?> packetClass;
    private static Class<?> chatSerializerClass;
    private static Class<?> iChatBaseComponentClass;
    private static Method craftPlayerHandleMethod;

    static {
        try {
            craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nms + ".entity.CraftPlayer");
            packetPlayOutChatClass = Class.forName("net.minecraft.server." + nms + ".PacketPlayOutChat");
            packetClass = Class.forName("net.minecraft.server." + nms + ".Packet");
            iChatBaseComponentClass = Class.forName("net.minecraft.server." + nms + ".IChatBaseComponent");
            chatSerializerClass = iChatBaseComponentClass.getDeclaredClasses()[0];
            craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void sendActionBar(Player player, String message) {

        if (!player.isOnline()) {
            return;
        }

        ByteSectorsClient.getInstance().getServer().getScheduler().runTaskAsynchronously(ByteSectorsClient.getInstance(), () -> {
            try {
                Object craftPlayer = craftPlayerClass.cast(player);
                Method m3 = chatSerializerClass.getDeclaredMethod("a", String.class);
                Object cbc = iChatBaseComponentClass.cast(m3.invoke(chatSerializerClass, "{\"text\": \"" + Utils.fixColors(message) + "\"}"));
                Object packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, byte.class}).newInstance(cbc, (byte) 2);
                Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
                Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
                Object playerConnection = playerConnectionField.get(craftPlayerHandle);
                Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", packetClass);
                sendPacketMethod.invoke(playerConnection, packet);
            } catch (Exception e) {
                Utils.log(e);
            }
        });
    }

    public static void sendActionBar(final Player player, final String message, int duration) {
        sendActionBar(player, message);

        if (duration >= 0) {
            ByteSectorsClient.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(ByteSectorsClient.getInstance(), () -> sendActionBar(player, ""), duration + 1);
        }

        while (duration > 40) {
            duration -= 40;
            ByteSectorsClient.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(ByteSectorsClient.getInstance(), () -> sendActionBar(player, message), duration);
        }
    }

    public static void sendActionBarToAllPlayers(String message) {
        sendActionBarToAllPlayers(message, -1);
    }

    public static void sendActionBarToAllPlayers(String message, int duration) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendActionBar(p, message, duration);
        }
    }
}