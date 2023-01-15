package badziol.perksystem.listeners;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import badziol.perksystem.perk.PosiadaczPerkow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenerQuit implements Listener {
    private final PerkSystem plugin;

    public ListenerQuit(PerkSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit (PlayerQuitEvent event){
        PosiadaczPerkow posiadacz = plugin.perkLista.pobierzPosiadacza(event.getPlayer().getName());
        if (posiadacz == null) return;

        System.out.println("[PerkSystem](onQuit) - Dezaktywacja  dla : "+event.getPlayer().getName());
        for (int i=0; i <posiadacz.aktywnePerki.size(); i++){
            String nazwaPerka = posiadacz.aktywnePerki.get(i);
            Perk perk = plugin.perkLista.wezPerk(nazwaPerka);
            if (perk != null){
                perk.dezaktywuj(event.getPlayer());
            }else{
                System.out.println("[PerkSystem](onQuit) - nie znaleziono perka.");
            }
        }
    }
}
