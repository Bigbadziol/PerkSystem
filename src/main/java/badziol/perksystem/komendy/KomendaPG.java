package badziol.perksystem.komendy;

import badziol.perksystem.PerkSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KomendaPG implements CommandExecutor {
    private final PerkSystem plugin; //referencja do naszego glownego pluginu

    public KomendaPG(PerkSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            System.out.println("Przyszla komenda PerkGui");
            Player player = (Player)sender;
            plugin.gui.menuGlowne(player);
        }
        return true;
    }
}
