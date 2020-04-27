package pl.mikigal.bytesectors.commons.packet;

import pl.mikigal.bytesectors.commons.data.PlayerInfo;
import pl.mikigal.bytesectors.commons.serializable.LocationSerializable;
import pl.mikigal.bytesectors.commons.serializable.PotionEffectSerializable;

import java.util.UUID;

public class PacketPlayerTransfer extends Packet {

    private PlayerInfo playerInfo;

    public PacketPlayerTransfer(String name, UUID uniqueId, String sectorId, String inventory, String armor, String enderChest, LocationSerializable location, PotionEffectSerializable[] potionEffects, int level, int exp, double health, int foodLevel, int fireTicks, boolean fly, boolean allowFlight, String gameMode, boolean inBoat) {
        super();
        this.playerInfo = new PlayerInfo(name, uniqueId, sectorId, inventory, armor, enderChest, location, potionEffects, level, exp, health, foodLevel, fireTicks, fly, allowFlight, gameMode, inBoat);
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public String getName() {
        return playerInfo.getName();
    }

    public UUID getUniqueId() {
        return playerInfo.getUniqueId();
    }

    public String getSectorId() {
        return playerInfo.getSectorId();
    }

    public String getInventory() {
        return playerInfo.getInventory();
    }

    public String getArmor() {
        return playerInfo.getArmor();
    }

    public String getEnderChest() {
        return playerInfo.getEnderChest();
    }

    public LocationSerializable getLocation() {
        return playerInfo.getLocation();
    }

    public PotionEffectSerializable[] getPotionEffects() {
        return playerInfo.getPotionEffects();
    }

    public int getLevel() {
        return playerInfo.getLevel();
    }

    public int getExp() {
        return playerInfo.getExp();
    }

    public double getHealth() {
        return playerInfo.getHealth();
    }

    public int getFoodLevel() {
        return playerInfo.getFoodLevel();
    }

    public int getFireTicks() {
        return playerInfo.getFireTicks();
    }

    public boolean isFly() {
        return playerInfo.isFly();
    }

    public boolean isAllowFlight() {
        return playerInfo.isAllowFlight();
    }

    public String getGameMode() {
        return playerInfo.getGameMode();
    }

    public boolean isInBoat() {
        return playerInfo.isInBoat();
    }
}
