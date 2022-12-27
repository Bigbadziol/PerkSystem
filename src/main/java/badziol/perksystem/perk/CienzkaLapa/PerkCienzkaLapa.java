package badziol.perksystem.perk.CienzkaLapa;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PerkCienzkaLapa extends Perk implements Listener {
    public PerkCienzkaLapa(PerkSystem plugin) {
        super(plugin);
        nazwaId="cienzkalapa";
        wyswietlanie="Ciężka Łapa";
        opis.add("Pudzian przy tobie to");
        opis.add("chlopiec do bicia");
        textura ="eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEyMW" +
                "JmYjEzYjhlODY4NTIyN2JhMWExN2JlN2QzMGJmZjBiN2UyNDc2ZDVlMWU4YjRlMzg3MTdiYzViZWFlZCJ9fX0=";
        inicjujGlowke();
    }

    @EventHandler
    public void onCienzkaLapaCios (EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player) {
            double finalDamage = event.getFinalDamage();
            Player ofiara = (Player) event.getEntity();

            if (event.getDamager() instanceof Player) {
                double modyfikator = finalDamage * 0.1D;
                Player atakujacy = (Player) event.getDamager();
                if (plugin.perkLista.czyPosiadaAktywny(atakujacy.getName(),nazwaId)) {
                    ofiara.damage(modyfikator);
                    System.out.println("[Cienzka lapa ] (cios) " + atakujacy.getName() + " atakuje " + ofiara.getName() + ", ostateczny :"
                            + finalDamage + " zwiekszony o: " + modyfikator);
                }
            }
        }
    }
}
