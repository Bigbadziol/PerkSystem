package badziol.perksystem.komendy;

import badziol.perksystem.PerkSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static badziol.perksystem.perk.PerkStale.*;

public class TabPerk implements TabCompleter {
    private final PerkSystem plugin;
    private final ArrayList<String> nazwyPerkow = new ArrayList<>();//same nazwy perkow, uzupelnie w konstruktorze
    private final ArrayList<String> imionaGraczy = new ArrayList<>();//same imiona na podstawie graczy online
    private final ArrayList<String> podpowiedz = new ArrayList<>();//tu bedzie  generowana podpowiedz

    public TabPerk(PerkSystem plugin) {
        this.plugin = plugin;
        nazwyPerkow.addAll(plugin.perkLista.nazwyPerkow());
        nazwyPerkow.remove(PERK_NIEUSTAWIONY);//ukryj perk 'nieustawiony;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        //tutaj musi sie znalezc : lista imion graczy online oraz komenda 'lista'
        if (args.length == 1){
            ArrayList<Player> gracze = new ArrayList<>(Bukkit.getOnlinePlayers());
            imionaGraczy.clear();
            podpowiedz.clear();
            for (Player tenGracz : gracze){
                imionaGraczy.add(tenGracz.getName());
            }
            for (String imie : imionaGraczy){
                if (imie.toLowerCase().startsWith(args[0].toLowerCase())){
                    podpowiedz.add(imie);
                }
            }
            podpowiedz.add("lista");
           return podpowiedz;

        }
        // tutaj musi sie znalezc :limit, +,++ , -,--, napraw jesli wczesniejszy parametr to nie 'lista' a [imie]
        else if (args.length == 2){
            if (args[0].equalsIgnoreCase("lista")){
                podpowiedz.clear();
                return podpowiedz; //po lista nie chcemy podpowiedzi
            }else{
                podpowiedz.clear();
                podpowiedz.add("limit");
                podpowiedz.add("+");
                podpowiedz.add("++");
                podpowiedz.add("-");
                podpowiedz.add("--");
                podpowiedz.add("napraw");
                return  podpowiedz;
            }
        }else if (args.length == 3){
            //jesli wczesniejszy to 'limit' wypisz cyferki
           if (args[1].equalsIgnoreCase("limit")){
               podpowiedz.clear();
               for (int i = LIMIT_AKTYWNYCH_MIN; i <LIMIT_AKTYWNYCH_MAX +1 ; i++) {
                   podpowiedz.add(Integer.toString(i));
               }
               return podpowiedz;
           }
           //wczesniejsze to +,++, - , --
           //badamy + , - bo dla pozostałych null
           else if (args[1].equalsIgnoreCase("+") ||
                    args[1].equalsIgnoreCase("-") ){
                //na podstawie pisanej nazwy perka z listy wszystkich perkow dodaj tylko te
                // ktore sie zaczynaja sie od wpisanego ciagu
                podpowiedz.clear();
               for (String nazwa : nazwyPerkow){
                    if (nazwa.toLowerCase().startsWith(args[2].toLowerCase())){
                        podpowiedz.add(nazwa);
                    }
               }
               return podpowiedz;
           //++, -- , lub coś innego
           }else{
               podpowiedz.clear();
               return podpowiedz;
           }
        }
        podpowiedz.clear();
        return podpowiedz;
    }

}
