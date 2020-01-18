package pl.mikigal.bytesectors.commons.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigAPI {

    private static boolean bukkit = false;
    private static Class<?> yamlClass;
    private static Method get;

    static {
        try {
            Class.forName("org.bukkit.Bukkit");
            bukkit = true;
        } catch (ClassNotFoundException e) {
            // BungeeCord!
        }

        try {
            yamlClass = Class.forName(bukkit ? "org.bukkit.configuration.file.FileConfiguration" : "net.md_5.bungee.config.Configuration");
            get = yamlClass.getMethod("get", String.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void load(Object configuration, Class<?> configClass) {
        try {
            for (Field field : configClass.getDeclaredFields()) {
                loadField(configuration, field, configClass.getAnnotation(ConfigEntry.class));
            }

            Method loadManual = configClass.getDeclaredMethod("load", yamlClass);
            loadManual.setAccessible(true);
            loadManual.invoke(null, yamlClass.cast(configuration));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't get access to config fields! " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // There's no load method, ignoring
        }
    }

    private static void loadField(Object config, Field f, ConfigEntry classAnnotation) throws IllegalAccessException, InvocationTargetException {
        if ((classAnnotation == null && !f.isAnnotationPresent(ConfigEntry.class)) || (classAnnotation != null && f.isAnnotationPresent(ConfigExclude.class))) {
            return;
        }
        ConfigEntry annotation = classAnnotation == null ? f.getAnnotation(ConfigEntry.class) : classAnnotation;

        String path = annotation.value();
        Object result = (path.equals("-") ? get.invoke(config, f.getName()) : (path.endsWith(".") ? get.invoke(config, annotation.value() + f.getName()) : get.invoke(config, annotation.value())));

        if (result == null) {
            throw new RuntimeException("Path for field " + f.getName() + " is invalid (null)!");
        }

        f.setAccessible(true);
        f.set(null, result);
    }
}