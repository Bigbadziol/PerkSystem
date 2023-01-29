package badziol.perksystem.perk.Lowca;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KomendaLowca implements TabExecutor {
    private final PerkSystem plugin;
    private final ArrayList<String> podpowiedz = new ArrayList<>();//tu bedzie  generowana podpowiedz


    public KomendaLowca(PerkSystem plugin) {
        this.plugin = plugin;
        podpowiedz.add("daj");
        podpowiedz.add("gracz");
        podpowiedz.add("smierc");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return podpowiedz;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        boolean maPerka = plugin.perkLista.czyPosiadaAktywny(player.getName(), PerkStale.PERK_LOWCA);
        if (!maPerka) {
            System.out.println("[Lowca] - nie posiadasz aktywnego perka");
            return true;
        }
        PerkLowca perkLowca = (PerkLowca) plugin.perkLista.wezPerk(PerkStale.PERK_LOWCA);
        if (perkLowca == null) {
            System.out.println("[Lowca] - perk nie odnaleziony na liscie, wlaczony na liscie ?");
        }

        if (args.length == 1){
            if (args[0].equalsIgnoreCase("daj")){
                System.out.println("[Lowca] - daj");
                perkLowca.dodajKompas(player);
                //plugin.posiadaczeKompasow.dodajKompas(player);
            }else if (args[0].equalsIgnoreCase("gracz")){
                System.out.println("[Lowca] - gracz");
                //KompasLowcy  kompas = plugin.posiadaczeKompasow.wezKompas(player);
                KompasLowcy  kompas = perkLowca.wezKompas(player);
                if (kompas != null)  kompas.celGracz();
            }else if (args[0].equalsIgnoreCase("smierc")){
                System.out.println("[Lowca] - smierc");
                KompasLowcy kompas = perkLowca.wezKompas(player);
                if (kompas != null)  kompas.celSmierc();
            }else if (args[0].equalsIgnoreCase("debug")){
                System.out.println("[Lowca] - debug");
                KompasLowcy kompas = perkLowca.wezKompas(player);
                if (kompas != null) {
                    boolean res = kompas.wEkwipunku();
                    System.out.println("[Kompas] - jestem w ekwipunku :" + res);
                }
            }else {
                System.out.println("[Lowca] - nieznany parametr dla komendy");
            }
        }
        return true;
    }
}