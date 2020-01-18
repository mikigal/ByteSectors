package pl.mikigal.bytesectors.system;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import pl.mikigal.bytesectors.commons.data.SectorManager;

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

    private static void load() {
        File config = new File(ByteSectorsSystem.getInstance().getDataFolder().getPath() + File.separator + "config.yml");
        if (!config.exists()) {
            return;
        }

        try {
            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);

            for (String key : configuration.getSection("sectors").getKeys()) {
                Configuration sectorSection = configuration.getSection("sectors." + key);
                SectorManager.createSector(key, sectorSection.getInt("min_x"), sectorSection.getInt("max_x"), sectorSection.getInt("min_z"), sectorSection.getInt("max_z"), sectorSection.getString("world"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        saveDefaultConfig();
        load();
    }
}
