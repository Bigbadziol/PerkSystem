package badziol.perksystem.perk.KrwawiaceRany;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ZadanieKrwawiaceRany extends BukkitRunnable {
    private PerkSystem plugin;
    private final PerkKrwawiaceRany zadanie;

    public ZadanieKrwawiaceRany(PerkSystem plugin) {
        this.plugin = plugin;
        zadanie = (PerkKrwawiaceRany) plugin.perkLista.wezPerk(PerkStale.PERK_KRWAWIACE_RANY);
    }

    @Override
    public void run() {
//        System.out.println("Krwawienie kontroler :"+zadanie.krwawiacy.size());
        if (zadanie.krwawiacy.size() == 0) return;
        zadanie.krwawiacy.forEach( (key, rannyData) -> { //NIE ROB returnow, breakow czy continue w tego typu pętli!!
            Player ja = Bukkit.getPlayer(key); //ja-jako krwawiący
            if (ja != null) {
                long teraz =System.currentTimeMillis();
                if (rannyData.auktualnyTick < rannyData.iloscTickow){
                    if ((rannyData.ostatniTickCzas + rannyData.tickCzas) < teraz){
                        rannyData.ostatniTickCzas = teraz;
                        rannyData.auktualnyTick++;
                        ja.damage(rannyData.tickObrazenie);
                        if (zadanie.efektWidoczny){
                            Location pozycjaEfektu = ja.getLocation();
                            pozycjaEfektu.setY(pozycjaEfektu.getY() + (ja.getHeight() / 1.5));
                            Random random = new Random();
                            int iloscKrwi = 15 + random.nextInt(15);
                            double zasieg = 0.4;
                            ja.getWorld().spawnParticle(Particle.BLOCK_CRACK, pozycjaEfektu, iloscKrwi,
                                    zasieg, zasieg, zasieg, Material.REDSTONE_BLOCK.createBlockData());

                        }
                        System.out.println("zadano obrazenia "+ ja.getName() + "w wysokosci :" + rannyData.tickObrazenie);
                        System.out.println(ja.getName()+" tick : "+rannyData.auktualnyTick);
                    }
                }
                //W tej pętli nie możesz usunąc z mapy- patrz: tu operujesz bezpośrednio na danych
                //usunięcie nastąpi po tej pętli
            }
        });
        //usunięcie zadania z listy
        zadanie.krwawiacy.entrySet().removeIf( entry -> entry.getValue().auktualnyTick == entry.getValue().iloscTickow);
    }
}
