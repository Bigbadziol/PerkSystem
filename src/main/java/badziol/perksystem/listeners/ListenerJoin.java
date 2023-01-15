package badziol.perksystem.listeners;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import badziol.perksystem.perk.PosiadaczPerkow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ListenerJoin implements Listener {
    private final PerkSystem plugin;

    public ListenerJoin(PerkSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        PosiadaczPerkow posiadacz = plugin.perkLista.pobierzPosiadacza(event.getPlayer().getName());
        if (posiadacz == null) return;

        System.out.println("[PerkSystem](onJoin) - Aktywacja  dla : "+event.getPlayer().getName());
        for (int i=0; i <posiadacz.aktywnePerki.size(); i++){
            String nazwaPerka = posiadacz.aktywnePerki.get(i);
            Perk perk = plugin.perkLista.wezPerk(nazwaPerka);
            if (perk != null){
                perk.aktywuj(event.getPlayer());
            }else{
                System.out.println("[PerkSystem](onJoin) - nie znaleziono perka.");
            }

        }

    }
}
