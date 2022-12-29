package badziol.perksystem.perk.KrwawiaceRany;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

/*
    Bardzo wazne !!
    Za jasna cholere nie mam pojecia dla czego w Ikarze wszystko smiga jak potrzeba a tu
    aby miec dostep do krwawiacych , hashmapa musi byc statyczna !!
    czy to dlatego , ze Event handler opisany w tej klasie i trzeba go wypchnac  na zewnatrz ???
 */
public  class PerkKrwawiaceRany extends Perk implements Listener {

    private PerkKrwawiaceRanyData krd = new PerkKrwawiaceRanyData(0d,0l);
    public static final HashMap<UUID,PerkKrwawiaceRanyData>  krwawiacy = new HashMap();

    public PerkKrwawiaceRany(PerkSystem plugin) {
        super(plugin);
        nazwaId = PerkStale.PERK_KRWAWIACE_RANY;
        wyswietlanie = "Krwawiące rany";
        opis.add("Dość nie etyczna zabawa ta,");
        opis.add("ludzi wykrwawiasz każdego dnia");
        opis.add("");
        opis.add("Twoji przeciwnicy wykrwawiają sie przez 2s");
        opis.add("o 30% zadanych obrażeń. Jedynie kiedy");
        opis.add("zostały zadane crytikalsem");
        textura = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3J" +
                "ZnQubmV0L3RleHR1cmUvOTNhMzk2MGM4Nzk0NzQwMTdjMGNhM2M4MGY2ZWU3M2NmODg2ZTAwZTg5" +
                "YzkwMmEzZWU4OTNkZDI4NDk1MzVjMCJ9fX0=";
        efektWidoczny = true;
        inicjujGlowke();
    }


    public PerkKrwawiaceRanyData pobierzKrwawiacego(UUID uuid){
        return krwawiacy.get(uuid);
    }
    @EventHandler
    public void onCiosKrwawienie(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player ofiara) {
            double finalDamage = event.getFinalDamage();
            double damage = event.getDamage();// używane w debugu NIE RUSZAĆ

            if (event.getDamager() instanceof Player atakujacy) {
                if (plugin.perkLista.czyPosiadaAktywny(atakujacy.getName(), nazwaId)) {

/*
                    Teoretyczne zalozenia  crita :
                    The player must be falling.
                    The player must not be on the ground.  -isOnGround() - ta metoda depreciated
                    The player must not be on a ladder/vine etc.
                    The player must not be in water.
                    The player must not be affected by blindness.
                    The player must not be riding an entity.
                    The player must not be sprinting.
                    The base attack must not be reduced to 84.8% damage or lower due to cooldown.
 */
/*
                    magiczna liczba -wg taty - 0.4952
                    magiczna liczba -wg filipa - 0.2497
                    wersja roznica wysokosci graczy + 0
                    minecraft na wersje 1.19.2 - interpretuje krita w "noge" oznacza to :ze zamiast warunku:
                    atakujacy.getLocation().getY() > (ofiara.getLocation().getY() + [magiczna liczba]) zastosowalem:
                    atakujacy.getLocation().getY() > (ofiara.getLocation().getY()

*/
                    //nasz wyzwalacz krwawienia
                    boolean crit1 =
                            (atakujacy.getLocation().getY() != ofiara.getLocation().getY())  &&
                                    atakujacy.getFallDistance()> 0 &&
                                    //ladder/vine ??
                                    !atakujacy.isSwimming() &&
                                    !atakujacy.isSprinting() &&
                                    atakujacy.getVehicle() == null &&
                                    !atakujacy.hasPotionEffect(PotionEffectType.BLINDNESS);
                    if (!crit1) return;
/*
                    //Debug w przypadku pomyslu na inny wyzwalacz wykrwawiania
                    System.out.println("(onCiosKrwawienie) "+ atakujacy.getName() + " zaatakowal " +ofiara.getName()+
                            " damage :"+damage+"   final :"+finalDamage);
                    System.out.println("Zlap -> atakujacy: "+atakujacy.getLocation().getY() +
                                                "  ofiara: "+ofiara.getLocation().getY()+
                                                "  fall : "+ atakujacy.getFallDistance());
                    System.out.println("Crit 1 : "+crit1);
                    System.out.println();
*/
                    krd = pobierzKrwawiacego(ofiara.getUniqueId());
                    if (krd == null){
                        krwawiacy.put(ofiara.getUniqueId(),new PerkKrwawiaceRanyData(finalDamage,System.currentTimeMillis()));
                        krd = pobierzKrwawiacego(ofiara.getUniqueId());
                        System.out.println(ofiara.getName()+" dodany do listy krwawiacych.");

                    }else{
                        krd.auktualnyTick = 0;
                        krwawiacy.put(ofiara.getUniqueId(),krd);
                        System.out.println(ofiara.getName()+" odnowiono krwawienie");
                        //co robimy z nieszczesnikiem jesli juz krwawi???
                    }
                }
            }
        }
    }
    @EventHandler
    public void onKrwawiacyGinie(PlayerDeathEvent event) {
        UUID graczaUid = event.getEntity().getUniqueId();
        krwawiacy.remove(graczaUid);
        System.out.println("Wylaczono posmiertne krwawienie");
    }

    /**
     * Uwaga! Prawdziwa aktywacja zachodzi w EventHandlerze tej klasy
     * @param gracz obiekt gracza
     */
    @Override
    public void aktywuj(Player gracz) {
        System.out.println("[AKTYWACJA]: "+nazwaId+" - uwaga! aktywacja w -> handlerze klasy");
    }

    /**
     * Uwaga! prawdziwa dezaktywacja - czyli usuniecie z listy krwawiacych osob zachodzi w zadaniu:
     * ZadanieKrwawiaceRany.
     * @param gracz obiekt gracza
     */
    @Override
    public  void dezaktywuj(Player gracz){
        System.out.println("[DEAKTYWACJA](Krwawiacy): "+nazwaId+" - uwaga! deaktywacja -> ZadanieKrwawiaceRany ");
    }
}
