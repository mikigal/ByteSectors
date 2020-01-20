package pl.mikigal.bytesectors.client.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.util.Utils;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != null && inventory.getName().equals(Utils.fixColors(Configuration.getSectorsGuiName()))) {
            event.setCancelled(true);
        }
    }
}
