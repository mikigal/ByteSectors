package pl.mikigal.bytesectors.client.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

public class PerformanceUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.##");
    private static String nms = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
    private static Object serverInstance;
    private static Field tpsField;

    static {
        try {
            Class<?> minecraftServerClass = Class.forName("net.minecraft.server." + nms + ".MinecraftServer");
            serverInstance = minecraftServerClass.getMethod("getServer").invoke(null);
            tpsField = minecraftServerClass.getDeclaredField("recentTps");
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static String getTps() {
        try {
            return DECIMAL_FORMAT.format(((double[]) tpsField.get(serverInstance))[0]);
        } catch (IllegalAccessException e) {
            Utils.log(e);
            return "ERROR";
        }
    }
}
