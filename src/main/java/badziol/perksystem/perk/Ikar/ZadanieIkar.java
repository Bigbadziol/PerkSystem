package badziol.perksystem.perk.Ikar;

import badziol.perksystem.PerkSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ZadanieIkar extends BukkitRunnable {
    private PerkSystem plugin;
    private PerkIkar zadanie;


    public ZadanieIkar(PerkSystem plugin) {
        this.plugin = plugin;
        zadanie = (PerkIkar)plugin.perkLista.wezPerk("ikar");
    }
    @Override
    public void run() {
//        System.out.println("Ikar kontroler :"+zadanie.ikarzy.size());
        zadanie.ikarzy.forEach((key, ikarData) -> { //NIE ROB returnow , breakow czy continue w tego typu pentli!!
            Player ja = Bukkit.getPlayer(key);
            if (ja != null) {
                if (!ja.isOnline()){
                    zadanie.usunLotnika(key);
                    System.out.println("[Ikar](zadanie) - gracz usuniety z listy ikarow.");
                }else {
                    long teraz = System.currentTimeMillis();
                    if (!ikarData.wyswietlonoOstrzezenie &&
                        (teraz  > (ikarData.lotRozpoczecie + (ikarData.lotBaza * 1000)))
                    ) {
                        ja.sendMessage("Opadasz z sil...");
                        System.out.println("[Ikar](zadanie) - bepieczny czas przekroczony");
                        ikarData.wyswietlonoOstrzezenie = true;
                    }
                    if (ikarData.aktywny) {
                        if (teraz > (ikarData.lotRozpoczecie + ikarData.lotCzas)) {
                            zadanie.dezaktywuj(ja);
                        }
                    }
                }
            }
        });
    }
}
