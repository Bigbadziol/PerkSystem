package badziol.perksystem.perk.Ikar;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Ikar.PerkIkar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class komendaIkar implements CommandExecutor {
    private final PerkSystem plugin;

    public komendaIkar(PerkSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player gracz = (Player) sender;
            boolean maPerka = plugin.perkLista.czyPosiadaAktywny(gracz.getName(),"ikar");
            if (maPerka){
                PerkIkar perkIkar = (PerkIkar)plugin.perkLista.wezPerk("ikar");
                perkIkar.aktywuj(gracz);
            }else{
                System.out.println("[Ikar](komenda) - "+gracz.getName()+" nie ma aktywnego perka.");
                gracz.sendMessage("Zaloz najpierw perka.");
            }
        }
        return true;
    }
}
