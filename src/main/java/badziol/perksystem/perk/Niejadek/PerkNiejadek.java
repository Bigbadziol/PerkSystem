package badziol.perksystem.perk.Niejadek;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

//6) głód nie spada
public class PerkNiejadek extends Perk implements Listener {
    public PerkNiejadek(PerkSystem plugin) {
        super(plugin);
        nazwaId= PerkStale.PERK_NIEJADEK;
        opis.add("Nie odczuwasz glodu.");
        opis.add("Wystarczy ci energia kosmosu");
        wyswietlanie="Niejadek!";
        textura ="eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3"+
                "JhZnQubmV0L3RleHR1cmUvYTNiZDlmNzZjMjhjZDI0MjEzM2MxYzNjNjYzM2UzZTU4"+
                "NDc3NjhjODk0YTQwODljZjBmN2E3ZGI3MjM5ZGMyYyJ9fX0=";
        inicjujGlowke();
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (e.getEntityType () != EntityType.PLAYER) return;
        Player glodnyGracz = (Player) e.getEntity();

        if (plugin.perkLista.czyPosiadaAktywny(glodnyGracz.getName(),nazwaId)){
            glodnyGracz.sendMessage("Zasales porcje energii z kosomosu.");
            System.out.println("[Niejadek] zadzialal na "+glodnyGracz.getName());
            e.setCancelled(true);
        }
    }
}