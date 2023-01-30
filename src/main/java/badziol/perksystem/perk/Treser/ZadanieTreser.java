package badziol.perksystem.perk.Treser;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class ZadanieTreser extends BukkitRunnable {
    private final PerkSystem plugin;
    private final PerkTreser zadanie;
    ArrayList<UUID> doUsuniecia = new ArrayList<>();

    public ZadanieTreser(PerkSystem plugin) {
        this.plugin = plugin;
        zadanie = (PerkTreser)plugin.perkLista.wezPerk(PerkStale.PERK_TRESER);
    }

    @Override
    public void run() {
        if (zadanie.wilkiBojowe.size() == 0) return;
        doUsuniecia.clear();
        zadanie.wilkiBojowe.forEach((key, wilkData) -> {
            Player gracz = Bukkit.getPlayer(key);
            if (gracz == null){
                System.out.println("[Treser](zadanie) - serwer nie odnalazl gracza.");
                doUsuniecia.add(key);
            }else{
                if (!gracz.isOnline()){
                    doUsuniecia.add(gracz.getUniqueId());
                }else{
                    //Obsługa respawnu
                    wilkData.respawnWilka();
                    //Obsługa regeneracji życia
                    wilkData.regenerujHp();
                }
            }
        });
        //usuwamy zbedne zadania
        for (UUID key : doUsuniecia){
            zadanie.wilkiBojowe.remove(key);
        }
    }
}
