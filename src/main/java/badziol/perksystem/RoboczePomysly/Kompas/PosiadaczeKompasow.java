package badziol.perksystem.RoboczePomysly.Kompas;

import badziol.perksystem.PerkSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PosiadaczeKompasow   implements Listener {
    private final PerkSystem plugin;
    private BukkitTask zadanieKompasy;
    private static final HashMap <UUID,Kompas> listaKompasow = new HashMap<>();

    public PosiadaczeKompasow(PerkSystem plugin){
        this.plugin = plugin;
    }

    /**
     * Dodaj nowego gracza do listy posiadaczy kompasow namierzających najbliższego gracza
     * @param nowyWlasciciel - jak nazwa wskazuje
     */
    public void dodajKompas(Player nowyWlasciciel){
        if (nowyWlasciciel == null) {
            System.out.println("[KompasLista](dodaj) - nowy wlasciciel to null");
            return;
        }
        Kompas nowyKompas = new Kompas(plugin,nowyWlasciciel);
        nowyKompas.utworzKompas(); //czyli ma sie pojawic w ekwipunku
        listaKompasow.put(nowyWlasciciel.getUniqueId(), nowyKompas);
        if (listaKompasow.size() == 1) zadanieKompasyStart();
    }

    /**
     * Usun posiadacza 'perkowego kompasu' z listy
     */
    public void usunKompas(Player wlasciciel){
        listaKompasow.remove(wlasciciel.getUniqueId());
    }

    /**
     * Metoda na podstawie tagu NBT sprawdza, czy to 'perkowy kompas'
     * @param przedmiot  testowanty przedmiot
     * @return true- jeśli to nasz przedmiot
     */
    public boolean perkowyKompas(ItemStack przedmiot){
        Kompas tmpKompas = new Kompas(plugin,null);
        return tmpKompas.perkowyKompas(przedmiot);
    }

    /**
     * Pobierz pełne dane kompasu dla wskazanego właściciela
     * @param wlasciciel kompasu
     * @return jego kompas lub null, jeśli nie znaleziono właściciela
     */
    public Kompas wezKompas(Player wlasciciel){
        return listaKompasow.get(wlasciciel.getUniqueId());
    }


    /**
     *  Rozpocznij zadanie obsługi kompasów.
     *  Jeśli nie ma kompasów na liście, zadanie zostanie zatrzymane.
     *  Samo zadanie sprawdza, czy 'perkowy kompas' występuje w ekwipunku. Na tej podstawie określa cel jaki ma
     *  być wskazywany: najbliższy gracz lub miejsce ostatniej śmierci.
     */
    public void zadanieKompasyStart(){
        zadanieKompasy = new BukkitRunnable() {
            @Override
            public void run() {
               if (listaKompasow.isEmpty())  zadanieKompasy.cancel(); // nie ma 'perkowych kompasow' - zadanie nie potrzebne
               listaKompasow.forEach((klucz,kompas)->{
                   Player tenGracz = plugin.getServer().getPlayer(klucz);
                   if (tenGracz == null) return;
                    if (kompas.wEkwipunku()){
                        kompas.celGracz();
                    }else {
                        kompas.celSmierc();
                    }
                });
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20L);
    }

    /**
     * Obsługa zdarzenia wyrzucenia 'perkowego kompasu'.
     * @param e - zdarzenie
     */
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e){
        ItemStack przedmiot = e.getItemDrop().getItemStack();
        boolean res = perkowyKompas(przedmiot);
        System.out.println("Wypadl perkowy kompas  : "+ res);
        if (res){
            e.getItemDrop().remove();
            usunKompas(e.getPlayer());
            if (e.getPlayer().getLastDeathLocation() != null) {
                e.getPlayer().setCompassTarget(e.getPlayer().getLastDeathLocation());
            }
        }
    }
}
