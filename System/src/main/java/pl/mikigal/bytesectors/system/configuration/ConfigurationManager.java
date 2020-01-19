package pl.mikigal.bytesectors.system.configuration;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import pl.mikigal.bytesectors.system.ByteSectorsSystem;
import pl.mikigal.bytesectors.system.util.Utils;

import java.io.*;

public class ConfigurationManager {

    private static void saveDefaultConfig() {
        File directory = ByteSectorsSystem.getInstance().getDataFolder();
        if (!directory.exists()) {
            directory.mkdir();
        }

        File config = new File(directory.getPath() + File.separator + "config.yml");
        if (config.exists()) {
            return;
        }

        try {
            InputStream input = ByteSectorsSystem.getInstance().getResourceAsStream("config.yml");
            byte[] buffer = new byte[input.available()];
            input.read(buffer);

            OutputStream output = new FileOutputStream(config);
            output.write(buffer);

            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Configuration load() {
        try {
            File config = new File(ByteSectorsSystem.getInstance().getDataFolder().getPath() + File.separator + "config.yml");
            if (!config.exists()) {
                return null;
            }

            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);
        } catch (IOException e) {
            Utils.log(e);
        }

        return null;
    }

    public static Configuration initConfig() {
        saveDefaultConfig();
        return load();
    }
}
