package badziol.perksystem.perk.Ikar;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class komendaIkar implements TabExecutor {
    private final PerkSystem plugin;
    private final ArrayList<String> pustaLista = new ArrayList<>();
    public komendaIkar(PerkSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player gracz = (Player) sender;
            boolean maPerka = plugin.perkLista.czyPosiadaAktywny(gracz.getName(), PerkStale.PERK_IKAR);
            if (maPerka){
                PerkIkar perkIkar = (PerkIkar)plugin.perkLista.wezPerk(PerkStale.PERK_IKAR);
                perkIkar.aktywuj(gracz);
            }else{
                System.out.println("[Ikar](komenda) - "+gracz.getName()+" nie ma aktywnego perka.");
                gracz.sendMessage("Zaloz najpierw perka.");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return pustaLista;
    }
}
