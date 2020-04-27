package pl.mikigal.bytesectors.client.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import pl.mikigal.bytesectors.client.ByteSectorsClient;
import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.data.Direction;
import pl.mikigal.bytesectors.client.data.User;
import pl.mikigal.bytesectors.client.data.UserManager;
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

    private static void transfer(Player player, Location to, Sector sector, boolean inBoat) {
        SectorChangeEvent event = new SectorChangeEvent(player, SectorManager.getCurrentSector(), sector);
        ByteSectorsClient.getInstance().getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        PacketPlayerTransfer packet = new PacketPlayerTransfer(
                player.getUniqueId(),
                SerializationUtils.serializeItemstacks(player.getInventory().getContents()),
                SerializationUtils.serializeItemstacks(player.getInventory().getArmorContents()),
                SerializationUtils.serializeItemstacks(player.getEnderChest().getContents()),
                SerializationUtils.serializeLocation(to),
                SerializationUtils.serializePotionEffects(player.getActivePotionEffects()),
                player.getLevel(),
                player.getTotalExperience(),
                player.getHealth(),
                player.getFoodLevel(),
                player.getFireTicks(),
                player.isFlying(),
                player.getAllowFlight(),
                player.getGameMode().toString(),
                inBoat);

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
        player.setLevel(packet.getLevel());
        player.setTotalExperience(packet.getExp());
        player.setHealth(packet.getHealth());
        player.setFoodLevel(packet.getFoodLevel());
        player.setFireTicks(packet.getFireTicks());
        player.setAllowFlight(packet.isAllowFlight());
        player.setFlying(packet.isFly());
        player.setGameMode(GameMode.valueOf(packet.getGameMode()));

        if (packet.isInBoat()) {
            Bukkit.getScheduler().runTaskLater(ByteSectorsClient.getInstance(), () -> {
                Entity vehicle = player.getWorld().spawn(player.getLocation(), Boat.class);
                vehicle.setPassenger(player);
            }, 15);
        }

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        Bukkit.getScheduler().runTaskLater(ByteSectorsClient.getInstance(), () -> {
            for (PotionEffect effect : SerializationUtils.deserializePotionEffects(packet.getPotionEffects())) {
                player.addPotionEffect(new PotionEffect(effect.getType(), effect.getAmplifier(), effect.getDuration()));
            }
        }, 20);

        transferQueue.remove(packet.getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(ByteSectorsClient.getInstance(), () -> RedisUtils.set(player.getUniqueId().toString(), SectorManager.getCurrentSectorId()));
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

    public static void handlePlayerMove(Player player, Location from, Location to, boolean naturalMove, Event event) {
        Location location = player.getLocation();
        Sector currentSector = SectorManager.getSector(location.getBlockX(), location.getBlockZ(), location.getWorld().getName());
        Sector newSector = SectorManager.getSector(to.getBlockX(), to.getBlockZ(), to.getWorld().getName());
        User user = UserManager.getUser(player.getUniqueId());

        if (currentSector != null && currentSector.equals(newSector)) {
            return;
        }

        if (event instanceof Cancellable) { // VehicleMoveEvent isn't Cancellable
            Cancellable cancellable = (Cancellable) event;
            cancellable.setCancelled(true);
        }

        if (!(event instanceof PlayerTeleportEvent)) {
            player.teleport(from);
        }

        if (user.getNextSectorChange() > System.currentTimeMillis()) {
            return;
        }

        user.setNextSectorChange(System.currentTimeMillis() + 1000);
        if (newSector == null) {
            Utils.sendMessage(player, Configuration.getOutOfBorderMessage());
            return;
        }

        if (newSector.isOffline()) {
            Utils.sendMessage(player, Configuration.getSectorOfflineMessage());
            return;
        }

        Location transferLocation = naturalMove ? Direction.fromLocation(from, to).add(to) : to;
        Entity vehicle = player.getVehicle();

        if (vehicle == null) { // Temporary workaround, getVehicle() sometimes return null if player is in Boat
            for (Entity entity : player.getLocation().getChunk().getEntities()) {
                if (entity instanceof Boat && entity.getLocation().distance(player.getLocation()) <= 1) {
                    vehicle = entity;
                    break;
                }
            }
        }

        if (vehicle != null) {
            player.leaveVehicle();
            vehicle.remove();
        }

        PlayerTransferUtils.transfer(player, transferLocation, newSector, vehicle != null);
    }

    private static void connectToSector(Player player, Sector sector) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(sector.getId());

        player.sendPluginMessage(ByteSectorsClient.getInstance(), "BungeeCord", output.toByteArray());
    }
}
