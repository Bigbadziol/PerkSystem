package badziol.perksystem.perk.Treser;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PerkTreser extends Perk implements Listener {
    //Uwaga! klucz- uuid gracza, dane konkretnego wilka
    public static HashMap<UUID, WilkData> wilkiBojowe = new HashMap<>();
    public PerkTreser(PerkSystem plugin) {
        super(plugin);
        nazwaId= PerkStale.PERK_TRESER;
        wyswietlanie="Treser";
        opis.add("Możesz przyzwac psa");
        opis.add("wspomagajacego cie w walce.");
        //https://minecraft-heads.com/custom-heads/animals/59115-blue-doge
        textura="eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cm" +
                "UvZGFkMjZjNWM4Y2NmNTgzYWM5ZWI0ZDBmN2ZhZjQ1ZjVlNGE5YTE2NzAwODYzMmZkM2I5NzkxYWMyNGVhOGI1NyJ9fX0";
        efektWidoczny = true;
        inicjujGlowke();
    }

    /**
     * Atak wilka, sposob działania.
     * Wilk atakuje tylko graczy. Poniżej określonego poziomu życia wilk zyskuje szansę zadawania
     * krytycznych ciosów. Każdy cios ma określoną szansę na bycie ciosem krytycznym.
     * Przykładowo poniżej 60% życia wilk ma 35% szansę na zadanie krytycznego ciosu.
     * @param e zdarzenie serwerowe wywołane spowodowaniem obrażeń bytowi przez byt.
     */
    @EventHandler
    public void onMojWilkHit(EntityDamageByEntityEvent e){
        //wstępnie wilk atakuje tylko graczy
        //System.out.println("-> Ofiara != player");
        if (!(e.getEntity() instanceof Player)) return;

        //System.out.println("-> Atakujacy wilkiem ?");
        if (!(e.getDamager() instanceof Wolf)) return;
        Wolf atakujacyWilk = (Wolf) e.getDamager(); //wilk jako entity
        Player wlascicielWilka = (Player) atakujacyWilk.getOwner();
        Player ofiara = (Player) e.getEntity();
        //System.out.println("-> wilk ma wlasciciela ?");
        if (wlascicielWilka == null) return; //wilk nie ma wlasciciela
        //System.out.println("-> wilk bojowy ?, ilosc wilkow : "+ wilkiBojowe.size());
        WilkData tenWilkData = wilkiBojowe.get(wlascicielWilka.getUniqueId()); //nasze dodatkowe dane
        if (tenWilkData == null) return; //to zaatakował inny wilk, bo nie jest na naszej liscie

        e.setCancelled(true);//przerywamy niejako domyslne działanie i nadpisujemy własne

        AttributeInstance maxHp = atakujacyWilk.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double aktualneHp = atakujacyWilk.getHealth();
        double procentZycia = aktualneHp / maxHp.getValue();
        double silaAtaku = e.getFinalDamage();
        if (procentZycia <= tenWilkData.poziomRan){//zaistniala mozliwosc critowania
            Random rand = new Random();
            double los = rand.nextDouble(0,1.001);
            if (los > tenWilkData.critSzansa){
                System.out.println("[Treser](onHit) - cios krytyczny (150%).");
                silaAtaku = silaAtaku + (silaAtaku/2);
            }
        }
        System.out.println("[Treser](onHit)"+wlascicielWilka.getName()+" -> Wilk hp :"
                +tenWilkData.infoHp()+ " obrazenia : "+silaAtaku);
        ofiara.damage(silaAtaku);
    }

    /**
     * Zdażenie sprawdza, czy byt, który zginął to nasz wilk. Jeśli tak to ustawiany jest czas śmierci oraz
     * niezbędna flaga do wskrzeszenia. Wskrzeszenie obsługuje zadanie treser.
     * @param e byt z gry, który zginął
     */
    @EventHandler
    public void onWilkDeath(EntityDeathEvent e){
        if (e.getEntityType() != EntityType.WOLF) return; //nie wilk
        WilkData tenWilk = wilkiBojowe.get(e.getEntity().getUniqueId()); //poszukujemy wilka na naszej liście
        if (tenWilk == null) return; // to nie nasz wilk.
        System.out.println("[Treser] - nasz wilk zginal.");
        tenWilk.zyje = false;
        tenWilk.czasSmierci = System.currentTimeMillis();
        wilkiBojowe.put(e.getEntity().getUniqueId(),tenWilk);
        System.out.println("[Treser] - uaktualniono dane martwego wilka.");
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event){
        Player gracz = event.getPlayer();
        WilkData wilk  = wilkiBojowe.get(gracz.getUniqueId());
        if (wilk != null){
            System.out.println("[Treser] - zabijam wilka gracza :"+gracz.getName());
            wilk.odwolaj();
        }
    }


    /**
     * Aktywuj działanie perka
     * @param gracz obiekt gracza
     */
    @Override
    public void aktywuj(Player gracz) {
        System.out.println("[AKTYWACJA]("+nazwaId+"): - start ");
        //teraz wilkData kontroluje czy wilk juz byl przyzwany
        wilkiBojowe.put(gracz.getUniqueId(),new WilkData(gracz));
        System.out.println("Ilosc wilkow : "+ wilkiBojowe.size());
        System.out.println("[AKTYWACJA]("+nazwaId+"): - stop ");
    }

    /**
     * Dzialania niezbedne na zdarzenie zdjecia perka
     * @param gracz obiekt gracza
     */
    @Override
    public  void dezaktywuj(Player gracz){
        System.out.println("[DEAKTYWACJA]("+nazwaId+"): - start ");
        WilkData wilk = wilkiBojowe.get(gracz.getUniqueId());
        if (wilk != null) wilk.odwolaj();
        wilkiBojowe.remove(gracz.getUniqueId());
        System.out.println("[DEAKTYWACJA]("+nazwaId+"): - stop ");
    }
}
