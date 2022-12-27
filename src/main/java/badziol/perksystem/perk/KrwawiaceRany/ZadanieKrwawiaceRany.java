package badziol.perksystem.perk.KrwawiaceRany;

import badziol.perksystem.PerkSystem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static badziol.perksystem.PerkSystem.getPlugin;

public class ZadanieKrwawiaceRany extends BukkitRunnable {
    private PerkSystem plugin;
    private PerkKrwawiaceRany zadanie;

    public ZadanieKrwawiaceRany(PerkSystem plugin) {
        this.plugin = plugin;
        zadanie = (PerkKrwawiaceRany) plugin.perkLista.wezPerk("krwawiacerany");
    }

    @Override
    public void run() {
//        System.out.println("Krwawienie kontroler :"+zadanie.krwawiacy.size());
        if (zadanie.krwawiacy.size() == 0) return;
        zadanie.krwawiacy.forEach( (key, rannyData) -> { //NIE ROB returnow , breakow czy continue w tego typu pentli!!
            Player ja = Bukkit.getPlayer(key);
            if (ja != null) {
                long teraz =System.currentTimeMillis();
                if (rannyData.auktualnyTick < rannyData.iloscTickow){
                    if ((rannyData.ostatniTickCzas + rannyData.tickCzas) < teraz){
                        rannyData.ostatniTickCzas = teraz;
                        rannyData.auktualnyTick++;
 //                       System.out.println(ja.getName()+" tick : "+rannyData.auktualnyTick);
                    }
                }
                //W tej petli nie mozesz usunac z mapy- patrz : tu operujesz bezposrednio na danych
                //usuniecie po tej petli
            }
        });
        //usuniecie zadania
        zadanie.krwawiacy.entrySet().removeIf( entry -> entry.getValue().auktualnyTick == entry.getValue().iloscTickow);
    }
}
