package pl.mikigal.bytesectors.client.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private static Map<UUID, User> users = new HashMap<>();

    public static void createUser(UUID uuid) {
        users.put(uuid, new User(uuid));
    }

    public static User getUser(UUID uuid) {
        return users.get(uuid);
    }
}
