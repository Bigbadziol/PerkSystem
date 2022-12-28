package badziol.perksystem.perk.Wampir;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

//7) zabicie gracza regeneruje 5serc
public class PerkWampir extends Perk implements Listener {
    public PerkWampir(PerkSystem plugin) {
        super(plugin);
        nazwaId= PerkStale.PERK_WAMPIR;
        wyswietlanie="Wampir";
        opis.add("Nie możesz się doczekać smaku");
        opis.add("krwi przeciwnika.");
        opis.add("");
        opis.add("Regeneruje 20% hp z zadanych obrażeń");
        textura = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQu" +
                "bmV0L3RleHR1cmUvOGQ0NDc1NmUwYjRlY2U4ZDc0NjI5NmEzZDVlMjk3ZTE0MTVmNGJhMTc2NDdmZmUyMjgzOD" +
                "UzODNkMTYxYTkifX19";
        inicjujGlowke();
    }
    @EventHandler
    public void onWampir (EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player) {
            double finalDamage = event.getFinalDamage();
            Player ofiara = (Player) event.getEntity();

            if (event.getDamager() instanceof Player) {
                double leczenie = finalDamage * 0.2D;
                Player atakujacy = (Player) event.getDamager();
                if (plugin.perkLista.czyPosiadaAktywny(atakujacy.getName(),nazwaId)) {
                    AttributeInstance maxHp = atakujacy.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    if ((atakujacy.getHealth()+leczenie) > maxHp.getDefaultValue()){
                        System.out.println("[Wampir] uleczono do maxa");
                        atakujacy.setHealth(maxHp.getDefaultValue());
                    }else {
                        atakujacy.setHealth(atakujacy.getHealth() + leczenie);
                        System.out.println("[Wampir] "+atakujacy.getName() + " udezyl " + ofiara.getName() + "i uleczyl sie o : " + leczenie);
                    }
                }
            }
        }
    }
}
