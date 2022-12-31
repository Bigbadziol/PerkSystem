package badziol.perksystem.perk.Treser;

import badziol.perksystem.PerkSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KomendaTreser implements CommandExecutor {
    private final PerkSystem plugin;

    public KomendaTreser(PerkSystem plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player =(Player) sender;
            System.out.println("[Treser] komenda baza.");
        }
        return true;
    }
}
