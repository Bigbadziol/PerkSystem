package badziol.perksystem.komendyTestowe;
import badziol.perksystem.PerkSystem;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class komendaLot implements CommandExecutor {
    private final ArrayList<Player> listaLotnikow = new ArrayList<>();
    private final PerkSystem plugin;

    public komendaLot(PerkSystem plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            String onMessage = "Mozesz frowac.";
            String offMessage="Nie możesz frowac.";

            if (listaLotnikow.contains(player)){
                listaLotnikow.remove(player);
                player.sendMessage(ChatColor.GOLD+offMessage);
                player.setAllowFlight(false);
            }else{
                listaLotnikow.add(player);
                player.sendMessage(ChatColor.DARK_GREEN+ onMessage);
                player.setAllowFlight(true);
            }
        }
        return true;
    }
}
