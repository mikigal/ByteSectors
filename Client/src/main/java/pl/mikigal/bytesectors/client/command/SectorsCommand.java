package pl.mikigal.bytesectors.client.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.util.ItemBuilder;
import pl.mikigal.bytesectors.client.util.Utils;
import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.util.TimeUtils;

import java.util.List;

public class SectorsCommand extends Command {

    public SectorsCommand() {
        super("sectors", true, "sector", "sektory", "sektor", "bytesectors");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Inventory inventory = Bukkit.createInventory(null, this.calcInventorySize(), Utils.fixColors(Configuration.getSectorsGuiName()));

        int j = 0;
        for (Sector sector : SectorManager.getSectors()) {
            ItemBuilder item = (sector.isOffline() ? Configuration.getOfflineItem() : Configuration.getOnlineItem()).clone();
            List<String> lore = item.getLore();
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, replaceVariables(lore.get(i), sector));
            }

            item.setLore(lore);
            item.setName(replaceVariables(item.getName(), sector));
            inventory.setItem(j, item.toItemStack());
            j++;
        }

        ((Player) sender).openInventory(inventory);
    }

    private String replaceVariables(String line, Sector sector) {
        return line.replace("{ID}", sector.getId())
                .replace("{ONLINE}", String.valueOf(sector.getOnline()))
                .replace("{PERFORMANCE}", (sector.getPerformance() == null ? "OFFLINE" : sector.getPerformance()))
                .replace("{LAST_ONLINE}", TimeUtils.millisToText(System.currentTimeMillis() - sector.getLastPerformancePacket()));
    }

    private int calcInventorySize() {
        int sectors = SectorManager.getSectors().size();
        for (int i = 9; i <= 54; i += 9) {
            if (i > sectors) {
                return i;
            }
        }

        return -1;
    }
}
