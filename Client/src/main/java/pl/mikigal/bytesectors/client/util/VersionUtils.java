package pl.mikigal.bytesectors.client.util;

import org.bukkit.Bukkit;

public class VersionUtils {

//    // TODO: complete the rest of the versions
//    private static Map<String, Integer> protocolVersions = Maps.newHashMap();
//
//    static {
//        protocolVersions.put("1.15.2", 578);
//        protocolVersions.put("1.15.1", 575);
//        protocolVersions.put("1.15", 573);
//        protocolVersions.put("1.14.4", 498);
//        protocolVersions.put("1.14.3", 490);
//        protocolVersions.put("1.14.2", 485);
//        protocolVersions.put("1.14.1", 480);
//        protocolVersions.put("1.14", 477);
//        protocolVersions.put("1.13.2", 403);
//        protocolVersions.put("1.13.1", 401);
//        protocolVersions.put("1.13", 393);
//        protocolVersions.put("1.12.2", 340);
//        protocolVersions.put("1.12.1", 338);
//        protocolVersions.put("1.12", 335);
//        protocolVersions.put("1.11.2", 316);
//        protocolVersions.put("1.11.1", 316);
//        protocolVersions.put("1.11", 315);
//    }

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
    }
}
