package pl.mikigal.bytesectors.client.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mikigal.bytesectors.client.util.Utils;

import java.util.Arrays;
import java.util.List;

public abstract class Command extends org.bukkit.command.Command {

    private String name;
    private String permission;
    private String usage;
    private String[] aliases;
    private boolean onlyPlayer;
    private int argsMinLength;

    public Command(String name, boolean onlyPlayer, int argsMinLength, String usage, String... aliases) {
        super(name, "", "&cPoprawne uzycie: " + (usage == null ? "/" + name : usage), Arrays.asList(aliases));

        this.name = name;
        this.permission = "bytesectors." + name;
        this.usage = "&cPoprawne uzycie: " + (usage == null ? "/" + name : usage);
        this.aliases = aliases;
        this.argsMinLength = argsMinLength;
        this.onlyPlayer = onlyPlayer;
    }

    public Command(String name, boolean onlyPlayer, String... aliases) {
        this(name, onlyPlayer, 0, null, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (this.permission != null && !sender.hasPermission(this.permission)) {
            Utils.sendMessage(sender, "&cNie masz do tego uprawnien!");
            return false;
        }

        if (this.onlyPlayer && !(sender instanceof Player)) {
            Utils.sendMessage(sender, "&cMusisz byc graczem zeby tego uzyc!");
            return false;
        }

        if (args.length < this.argsMinLength) {
            Utils.sendMessage(sender, this.usage);
            return false;
        }

        this.execute(sender, args);
        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(this.aliases);
    }
}
