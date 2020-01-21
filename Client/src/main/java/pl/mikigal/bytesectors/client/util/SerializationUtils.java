package pl.mikigal.bytesectors.client.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import pl.mikigal.bytesectors.commons.serializable.ItemBuilderSerializable;
import pl.mikigal.bytesectors.commons.serializable.LocationSerializable;
import pl.mikigal.bytesectors.commons.serializable.PotionEffectSerializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SerializationUtils {

    public static LocationSerializable serializeLocation(Location location) {
        return new LocationSerializable(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), location.getWorld().getName());
    }

    public static Location deserializeLocation(LocationSerializable location) {
        return new Location(Bukkit.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public static PotionEffectSerializable serializePotionEffect(PotionEffect potionEffect) {
        return new PotionEffectSerializable(potionEffect.getType().getName(), potionEffect.getAmplifier(), potionEffect.getDuration());
    }

    public static PotionEffect deserializePotionEffect(PotionEffectSerializable serializable) {
        return new PotionEffect(PotionEffectType.getByName(serializable.getPotionType()), serializable.getAmplifier(), serializable.getDuration());
    }

    public static PotionEffectSerializable[] serializePotionEffects(Collection<PotionEffect> potionEffects) {
        List<PotionEffectSerializable> serialized = new ArrayList<>();
        for (PotionEffect potionEffect : potionEffects) {
            serialized.add(serializePotionEffect(potionEffect));
        }

        return serialized.toArray(new PotionEffectSerializable[0]);
    }

    public static List<PotionEffect> deserializePotionEffects(PotionEffectSerializable[] serialized) {
        List<PotionEffect> potionEffects = new ArrayList<>();
        for (PotionEffectSerializable effect : serialized) {
            potionEffects.add(deserializePotionEffect(effect));
        }

        return potionEffects;
    }

    public static ItemBuilder deserializeIemBuilder(ItemBuilderSerializable serializable) {
        return new ItemBuilder(Material.getMaterial(serializable.getMaterial()), serializable.getAmount(), serializable.getDurability())
                .setName(serializable.getName())
                .setLore(serializable.getLore());
    }

    public static String serializeItemstacks(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(items);
            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack[] deserializeItemstacks(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack[] items = (ItemStack[]) dataInput.readObject();
            dataInput.close();

            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
