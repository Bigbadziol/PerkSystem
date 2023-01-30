package badziol.perksystem.perk.Ikar;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ZadanieIkar extends BukkitRunnable {
    private final PerkSystem plugin;
    private final PerkIkar zadanieIkar;


    public ZadanieIkar(PerkSystem plugin) {
        this.plugin = plugin;
        zadanieIkar = (PerkIkar)plugin.perkLista.wezPerk(PerkStale.PERK_IKAR);
    }
    @Override
    public void run() {
//        System.out.println("Ikar kontroler :"+zadanie.ikarzy.size());
        zadanieIkar.ikarzy.forEach((key, ikarData) -> { //NIE ROB returnów, breaków czy continue w tego typu pętli!!
            Player ja = Bukkit.getPlayer(key);
            if (ja == null) return;
            if (!ja.isOnline()){
                zadanieIkar.usunLotnika(key);
                System.out.println("[Ikar](zadanie) - gracz usuniety z listy ikarow.");
                return;
            }

            long teraz = System.currentTimeMillis();
            //komunikat bezpieczeństwa
            if (!ikarData.wyswietlonoOstrzezenie &&
                (teraz  > (ikarData.lotRozpoczecie + (ikarData.lotBaza * 1000)))
            ) {
                ja.sendMessage("Opadasz z sil...");
                System.out.println("[Ikar](zadanie) - bepieczny czas przekroczony");
                ikarData.wyswietlonoOstrzezenie = true;
                ikarData.fazaLotu = FazaLotu.NIEBEZPIECZNA;
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
            if (zadanieIkar.efektWidoczny && ja.isFlying()) {
                if (!ikarData.maSkrzydla) {
                    zadanieIkar.dodajGraczaSkrzydla(ja);
                    ikarData.maSkrzydla = true;
                }
            }
            //usuń skrzydła, bo mimo aktywnego perka, gracz nie fruwa
            if (ikarData.maSkrzydla && !ja.isFlying()) {
                ikarData.maSkrzydla = false;
                zadanieIkar.usunGraczaSkrzydla(ja);
            }
            //przerwij lot
            if (ikarData.aktywny) {
                if (teraz > (ikarData.lotRozpoczecie + ikarData.lotCzas)) {
                    ikarData.maSkrzydla = false;
                    zadanieIkar.usunGraczaSkrzydla(ja);
                    zadanieIkar.dezaktywuj(ja);
                }
            }


        });
    }
}
