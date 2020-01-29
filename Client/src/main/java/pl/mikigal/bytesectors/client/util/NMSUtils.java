package pl.mikigal.bytesectors.client.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

public class NMSUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.##");
    private static String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
    private static Object serverInstance;
    private static Field tpsField;

    private static Method spawnParticleMethod;
    private static Object particle;

    static {
        try {
            Class<?> minecraftServerClass = Class.forName("net.minecraft.server." + version + ".MinecraftServer");
            serverInstance = minecraftServerClass.getMethod("getServer").invoke(null);
            tpsField = minecraftServerClass.getDeclaredField("recentTps");

            if (NMSUtils.getVersion().startsWith("v1_13_") || NMSUtils.getVersion().startsWith("v1_14_") || NMSUtils.getVersion().startsWith("v1_15_")) {
                Class<?> effectClass = Class.forName("org.bukkit.Particle");
                Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
                spawnParticleMethod = playerClass.getDeclaredMethod("spawnParticle", effectClass, Location.class, int.class);

                final Object[] effectTypes = effectClass.getEnumConstants();
                for (Object object : effectTypes) {
                    if (object.toString().equals("PORTAL")) {
                        particle = object;
                        break;
                    }
                }
            }
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

    public static String getVersion() {
        return version;
    }

    public static Object getParticle() {
        return particle;
    }

    public static Method getSpawnParticleMethod() {
        return spawnParticleMethod;
    }
}
