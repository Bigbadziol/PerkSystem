package badziol.perksystem.perk.Ikar;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ZadanieIkar extends BukkitRunnable {
    private PerkSystem plugin;
    private PerkIkar zadanie;


    public ZadanieIkar(PerkSystem plugin) {
        this.plugin = plugin;
        zadanie = (PerkIkar)plugin.perkLista.wezPerk(PerkStale.PERK_IKAR);
    }
    @Override
    public void run() {
//        System.out.println("Ikar kontroler :"+zadanie.ikarzy.size());
        zadanie.ikarzy.forEach((key, ikarData) -> { //NIE ROB returnów, breaków czy continue w tego typu pętli!!
            Player ja = Bukkit.getPlayer(key);
            if (ja != null) {
                if (!ja.isOnline()){
                    zadanie.usunLotnika(key);
                    System.out.println("[Ikar](zadanie) - gracz usuniety z listy ikarow.");
                }else {
                    long teraz = System.currentTimeMillis();
                    //komunikat bezpieczeństwa
                    if (!ikarData.wyswietlonoOstrzezenie &&
                        (teraz  > (ikarData.lotRozpoczecie + (ikarData.lotBaza * 1000)))
                    ) {
                        ja.sendMessage("Opadasz z sil...");
                        System.out.println("[Ikar](zadanie) - bepieczny czas przekroczony");
                        ikarData.wyswietlonoOstrzezenie = true;
                    }
                    //głód rośnie podczas lotu
                    if (ja.isFlying() && (ikarData.glodTickCzas + (ikarData.glodTick * 1000) < teraz)){
                        int glod = ja.getFoodLevel();
                        if (glod >= 1){
                            ja.setFoodLevel(glod - 1);
                            //System.out.println("[Ikar](zadanie) - glod podczas lotu : "+ (glod -1));
                        }
                    }
                    //wyświetl efekt tylko podczas lotu
                    if (zadanie.efektWidoczny && ja.isFlying()){
                        Location pozycjaEfektu = ja.getLocation();
                        pozycjaEfektu.setY(pozycjaEfektu.getY() + 0.5d);
                        //System.out.println("[Ikar](zadanie) - efekt");
                        ja.spawnParticle(Particle.BUBBLE_COLUMN_UP,pozycjaEfektu,20,0.5d,1.5d,0.5d);
                    }
                    //przerwij lot
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
