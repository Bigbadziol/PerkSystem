package badziol.perksystem.perk.Kevlar;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class PerkKevlar extends Perk implements Listener {
    private PerkKevlarData pkd = new PerkKevlarData(); //tu przechowywane sa ws// zystkie dane osob z kevlarem
    public final HashMap<UUID, PerkKevlarData> opancezeni = new HashMap<>();

    public  PerkKevlar(PerkSystem plugin){
        super(plugin);
        nazwaId= PerkStale.PERK_KEVLAR;
        wyswietlanie="Kevlar";
        opis.add("Najlepszą obroną jest zbroja");
        opis.add(", dlatego warto ją posiadać");
        opis.add("");
        opis.add("Kevlar generuje 5 serc, kiedy");
        opis.add("HP zpodnie poniżej 40%.");
        opis.add("Ten efekt odnawia się 5min");
        textura = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV" +
                "0L3RleHR1cmUvYzQ2ZDg3NzY4MGM5ODQ3MTc2Mzc3YTFhMTQ1YmYwZGMyM2RhM2Q0MGRiNzQ0YmIyMmRlND" +
                "ljNzUyMzQwZWU4MSJ9fX0=";
        inicjujGlowke();
    }

    @EventHandler
    public void onKevlar (EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player ofiara = (Player) event.getEntity();
            if (plugin.perkLista.czyPosiadaAktywny(ofiara.getName(),nazwaId)) {
                AttributeInstance maxHp = ofiara.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                double  granicaLeczenia = maxHp.getDefaultValue() * 0.4;
                System.out.println("[Kevlar] granica leczenia :  "+granicaLeczenia);
                if (ofiara.getHealth() <= granicaLeczenia) {
                    aktywuj(ofiara);
                }
            }
        }
    }

    public PerkKevlarData pobierzOpencezonego(UUID uuid) {
        return opancezeni.get(uuid);
    }
         boolean mogeBoBlokadaCzasowa(Player tenGracz){
        UUID uuid = tenGracz.getUniqueId();
        if (!opancezeni.containsKey(uuid) ||
                (System.currentTimeMillis() - pkd.czasAktywacji) > (pkd.blokada * 1000)) {
            pkd.czasAktywacji = System.currentTimeMillis(); //aktualizacja czasu
            opancezeni.put(uuid, pkd);
            System.out.println("[Kevlar] - dodanie do listy lub czas minal.");
            return true;
        } else {
            long uplynelo  = (System.currentTimeMillis() - pkd.czasAktywacji) / 1000;
            long czasBlokady = pkd.blokada -  uplynelo;
            System.out.println("[Kevlar] - blokada potrwa : "+czasBlokady+ "sek.");

            return false;
        }
    }

    @Override
    public void aktywuj(Player gracz) {
        System.out.println("[AKTYWACJA]: "+nazwaId+" - start");
        pkd = pobierzOpencezonego(gracz.getUniqueId());
        if (pkd == null){
            opancezeni.put(gracz.getUniqueId(), new PerkKevlarData());
            pkd = opancezeni.get(gracz.getUniqueId());
            System.out.println("[Kevlar] - dodano nowego opancezonego.");
        }
        if (!mogeBoBlokadaCzasowa(gracz)){
            long uplynelo  = (System.currentTimeMillis() - pkd.czasAktywacji) / 1000;
            long czasBlokady = pkd.blokada -  uplynelo;
            return;
        }
        opancezeni.put(gracz.getUniqueId(),pkd);
        PotionEffect potion = new PotionEffect(PotionEffectType.ABSORPTION, (30 * 20), pkd.jakiKevlar-1, true, false);
        gracz.addPotionEffect(potion);
        if (pkd.minusJednoSerceZKevlara == true){
            gracz.damage(2);
        }
        System.out.println("[Kevlar] - aktywacja dla : "+gracz.getName()+" sukces.");
    }

    /**
     * Dzialania niezbedne na zdarzenie zdjecia perka
     * @param gracz obiekt gracza
     */
    @Override

    public  void dezaktywuj(Player gracz){
        System.out.println("[DEAKTYWACJA](Kevlar): "+nazwaId+" - start ");

        System.out.println("[DEAKTYWACJA](Kevlar) - dezaktywacja sukces.");
    }
}
