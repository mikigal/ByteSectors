package pl.mikigal.bytesectors.client.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.util.Utils;
import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;

public class BorderTerrainModifyListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        this.handleModifyTerrain(event.getPlayer(), event.getBlockPlaced().getLocation(), event);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        this.handleModifyTerrain(event.getPlayer(),event.getBlock().getLocation(), event);
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent event) {
        this.handleModifyTerrain(event.getPlayer(), event.getBlock().getLocation(), event);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        this.handleModifyTerrain(event.getPlayer(), event.getItemDrop().getLocation(), event);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        this.handleModifyTerrain(null, event.getBlock().getLocation(), event);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        this.handleModifyTerrain(null, event.getLocation(), event);
    }

    private void handleModifyTerrain(Player player, Location location, Cancellable cancellable) {
        Sector sector = SectorManager.getCurrentSector();
        int distance = sector.getDistanceToBorder(location.getBlockX(), location.getBlockZ());
        if (distance <= Configuration.getNearBorderTerrainModifyBlockDistance()) {
            cancellable.setCancelled(true);

            if (player != null) {
                Utils.sendMessage(player, Configuration.getNearBorderTerrainModifyMessage());
            }
        }
    }
}
