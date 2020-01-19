package pl.mikigal.bytesectors.client.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import pl.mikigal.bytesectors.client.ByteSectorsClient;
import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.event.SectorChangeEvent;
import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.PacketPlayerTransfer;
import pl.mikigal.bytesectors.commons.redis.RedisUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerTransferUtils {

    private static Map<UUID, CompletableFuture<Player>> transferQueue = new HashMap<>();

    public static void transfer(Player player, Location to, Sector sector) {
        SectorChangeEvent event = new SectorChangeEvent(player, SectorManager.getSector(Configuration.getSectorId()), sector);
        ByteSectorsClient.getInstance().getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        Entity vehicle = player.getVehicle();
        Location location = to.clone();
        location.setY(location.getY() + 2);

        PacketPlayerTransfer packet = new PacketPlayerTransfer(
                Configuration.getSectorId(),
                player.getUniqueId(),
                SerializationUtils.serializeItemstacks(player.getInventory().getContents()),
                SerializationUtils.serializeItemstacks(player.getInventory().getArmorContents()),
                SerializationUtils.serializeItemstacks(player.getEnderChest().getContents()),
                SerializationUtils.serializeLocation(location),
                SerializationUtils.serializePotionEffects(player.getActivePotionEffects()),
                player.getLevel(),
                player.getTotalExperience(),
                player.getHealth(),
                player.getFoodLevel(),
                player.getFireTicks(),
                player.isFlying(),
                player.getAllowFlight(),
                player.getGameMode().toString(),
                vehicle == null ? null : vehicle.getType().getName(),
                vehicle == null ? null : SerializationUtils.serializeLocation(vehicle.getLocation()));

        packet.send(sector);
        connectToSector(player, sector);
    }

    public static void acceptTransfer(Player player) {
        CompletableFuture<Player> future = transferQueue.get(player.getUniqueId());
        if (future != null) {
            future.complete(player);
        }
    }

    public static void acceptTransfer(PacketPlayerTransfer packet) {
        Player player = Bukkit.getPlayer(packet.getUniqueId());
        player.getInventory().setContents(SerializationUtils.deserializeItemstacks(packet.getInventory()));
        player.getInventory().setArmorContents(SerializationUtils.deserializeItemstacks(packet.getArmor()));
        player.getEnderChest().setContents(SerializationUtils.deserializeItemstacks(packet.getEnderChest()));
        player.teleport(SerializationUtils.deserializeLocation(packet.getLocation()));
        player.getActivePotionEffects().clear();
        player.getActivePotionEffects().addAll(SerializationUtils.deserializePotionEffects(packet.getPotionEffects()));
        player.setLevel(packet.getLevel());
        player.setTotalExperience(packet.getExp());
        player.setHealth(packet.getHealth());
        player.setFoodLevel(packet.getFoodLevel());
        player.setFireTicks(packet.getFireTicks());
        player.setAllowFlight(packet.isAllowFlight());
        player.setFlying(packet.isFly());
        player.setGameMode(GameMode.valueOf(packet.getGameMode()));

        if (packet.hasVehicle()) {
            Location vehicleLocation = SerializationUtils.deserializeLocation(packet.getVehicleLocation());
            Entity vehicle = vehicleLocation.getWorld().spawnEntity(vehicleLocation, EntityType.fromName(packet.getVehicleType()));
            vehicle.setPassenger(player);
        }

        transferQueue.remove(packet.getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(ByteSectorsClient.getInstance(), () -> RedisUtils.set(player.getUniqueId().toString(), Configuration.getSectorId()));
    }

    public static void addTransferToQueue(PacketPlayerTransfer packet) {
        CompletableFuture<Player> future = new CompletableFuture<>();
        future.thenAccept((player) -> acceptTransfer(packet));

        Player player = Bukkit.getPlayer(packet.getUniqueId());
        if (player != null) {
            future.complete(player);
            return;
        }

        transferQueue.put(packet.getUniqueId(), future);
    }

    public static void handlePlayerMove(Player player, Location to, Event event) {
        Location location = player.getLocation();
        Sector currentSector = SectorManager.getSector(location.getBlockX(), location.getBlockZ(), location.getWorld().getName());
        Sector newSector = SectorManager.getSector(to.getBlockX(), to.getBlockZ(), to.getWorld().getName());

        if (newSector == null) {
            player.teleport(location);
            Utils.sendMessage(player, Configuration.getOutOfBorderMessage());

            if (event instanceof Cancellable) {
                Cancellable cancellable = (Cancellable) event;
                cancellable.setCancelled(true);
            }
            return;
        }

        if (!currentSector.equals(newSector)) {
            player.teleport(location);

            if (newSector.isOffline()) {
                Utils.sendMessage(player, Configuration.getSectorOfflineMessage());
                return;
            }

            PlayerTransferUtils.transfer(player, to, newSector);
        }
    }

    private static void connectToSector(Player player, Sector sector) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(sector.getId());

        player.sendPluginMessage(ByteSectorsClient.getInstance(), "BungeeCord", output.toByteArray());
    }
}
