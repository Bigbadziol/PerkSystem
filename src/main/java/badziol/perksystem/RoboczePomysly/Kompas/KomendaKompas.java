package badziol.perksystem.RoboczePomysly.Kompas;

import badziol.perksystem.PerkSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KomendaKompas implements TabExecutor {
    private final PerkSystem plugin;
    private final ArrayList<String> podpowiedz = new ArrayList<>();//tu bedzie  generowana podpowiedz


    public KomendaKompas(PerkSystem plugin) {
        this.plugin = plugin;
        podpowiedz.add("daj");
        podpowiedz.add("gracz");
        podpowiedz.add("smierc");
        podpowiedz.add("debug");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return podpowiedz;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length == 1){
            if (args[0].equalsIgnoreCase("daj")){
                System.out.println("[Kompas] - daj");
                plugin.posiadaczeKompasow.dodajKompas(player);

            }else if (args[0].equalsIgnoreCase("gracz")){
                Kompas kompas = plugin.posiadaczeKompasow.wezKompas(player);
                kompas.celGracz();

            }else if (args[0].equalsIgnoreCase("smierc")){
                Kompas kompas = plugin.posiadaczeKompasow.wezKompas(player);
                kompas.celSmierc();

            }else if (args[0].equalsIgnoreCase("debug")){
                System.out.println("[Kompas] - debug");
                Kompas k = plugin.posiadaczeKompasow.wezKompas(player);
                boolean res = k.wEkwipunku();
                System.out.println("[Kompas] - jestem w ekwipunku :"+res);
            }

        }
        return true;
    }
}
