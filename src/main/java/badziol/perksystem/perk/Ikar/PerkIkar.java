package badziol.perksystem.perk.Ikar;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

//Perk kozysta z PerkIkarTask - taki kontroler lotow naszych ikarow
public class PerkIkar extends Perk implements Listener{
    private PerkIkarData pid = new PerkIkarData(); //tu przechowywane sa wszystkie dane naszego lotnika
    public final HashMap<UUID, PerkIkarData> ikarzy = new HashMap<>();
    /**
     * Konstruktor klasy
     * @param plugin - referencja do pluginu, by miec dostep do perkLista
     */
    public PerkIkar(PerkSystem plugin) {
        super(plugin); //wywolaj konstruktor nadrzędny
        nazwaId="ikar";
        opis.add("Od tej chwili zyskujesz mozliwosc");
        opis.add("krotkotrwalego latania");
        wyswietlanie="Ikar";
        //https://minecraft-heads.com/custom-heads/animals/25787-fly
        textura="eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3J"+
                "hZnQubmV0L3RleHR1cmUvMTVmZjkyNGY3YjQ0NGYzOTE2MmJhZDMwMWZkYzE3Nzk3Yz"+
                "YxNmUxODcxZGRmODJjYTdmMWQyMzFhNTczOTRlIn19fQ==";
        inicjujGlowke();
    }

    /**
     *  Pobierz konkretnego ikara po uuid
     * @param uuid gracz
     * @return obiekt danych ikara , null - jesli nie znaleziono
     */
    public PerkIkarData pobierzLotnika(UUID uuid) {
        return ikarzy.get(uuid);
    }

    /**
     *  Usun lotnika z listy
     * @param uuid - gracza
     */
    public void usunLotnika(UUID uuid){
        ikarzy.remove(uuid);
    }
    /**
     * Metoda generuje losowy czas lotu, ustawia zmienna lotCzas w milisekundach.
     * @return - czas lotu w ms
     */
    private long ustawCzasLotu(){
        Random random = new Random();
        int losowyCzas = random.nextInt(pid.lotModyfikator) + 1;
        int czasSekundy = pid.lotBaza + losowyCzas;
        return (czasSekundy * 1000);
    }

    /**
     *  Metoda sprawdza ze wzgledu na zdrowie mozna latac.
     * @param tenGracz obiekt gracza
     * @return wynik sprawdzenia
     */
    private boolean mogeBoZdrowie(Player tenGracz){
        if (!pid.zdrowieUwzglednij){  // nie uwzgledniaj stanu zdrowia - czyli moge latac
            return true;
        }else{
            double  zdrowie = tenGracz.getHealth();
            //maksymalne ustawione zdrowie w grze mozna pobrac :
            // AttributeInstance maxZdrowieGra = tenGracz.getAttribute(Attribute.GENERIC_MAX_HEALTH); //domyslnie 20
            // double maxZdrowie = maxZdrowieGra.getDefaultValue();
            return zdrowie > pid.zdrowieMin; //true jesli .....
        }
    }

    /**
     * Metoda sprawdza czy mozna latac z wzgledu na stan glodu
     * @param tenGracz obiekt gracza
     * @return wynik sprawdzenia
     */
    private boolean mogeBoGlod(Player tenGracz){
        if (!pid.glodUwzglednij){
            return true; // nie uwzgledniaj stanu glodu
        }else{
            int glod = tenGracz.getFoodLevel();
            return glod > pid.glodMin;
        }
    }

    /**
     * dla sprwdzenia czy jest nalozona blokada czasowa
     * @param tenGracz obiekt gracza
     * @return wynik operacji
     */
    private boolean mogeBoBlokadaCzasowa(Player tenGracz){
        UUID uuid = tenGracz.getUniqueId();
        if (!ikarzy.containsKey(uuid) ||
                (System.currentTimeMillis() - pid.lotRozpoczecie) > (pid.blokada * 1000)) {
            pid.lotRozpoczecie = System.currentTimeMillis(); //aktualizacja czasu
            ikarzy.put(uuid, pid);
            System.out.println("[Ikar] - dodanie do listy lub czas minal.");
            return true;
        } else {
            long uplynelo  = (System.currentTimeMillis() - pid.lotRozpoczecie) / 1000;
            long czasBlokady = pid.blokada -  uplynelo;
            System.out.println("[Ikar] - blokada potrwa : "+czasBlokady+ "sek.");

            return false;
        }
    };

    /**
     * Aktywuj dzialanie perka
     *  Metoda dodaje lotnika jesli jest taka potrzeba. Czy sa spelnione warunki
     *  dotyczace zdrowi , glodu oraz ograniczen czasowych.
     * @param gracz obiekt gracza
     */
    @Override
    public void aktywuj(Player gracz) {
        System.out.println("[AKTYWACJA]: "+nazwaId+" - start");
        pid = pobierzLotnika(gracz.getUniqueId());
        if (pid == null){
            ikarzy.put(gracz.getUniqueId(), new PerkIkarData());
            pid = ikarzy.get(gracz.getUniqueId());
            System.out.println("[Ikar] - dodano nowego ikara.");
        }
        if (!mogeBoZdrowie(gracz)){
            gracz.sendMessage("Jestes zbyt oslabiony by latac.");
            System.out.println("[Ikar] - jestes zbyt oslabiony.");
            return;
        }
        if (!mogeBoGlod(gracz)){
            gracz.sendMessage("Jestes zbyt glodny by latac.");
            System.out.println("[Ikar] - jestes zbyt glodny.");
            return;
        }
        if (pid.aktywny){
            gracz.sendMessage("Nadal mozesz latac...");
            System.out.println("[Ikar] - nadal mozesz latac.");
            return;
        }
        if (!mogeBoBlokadaCzasowa(gracz)){
            long uplynelo  = (System.currentTimeMillis() - pid.lotRozpoczecie) / 1000;
            long czasBlokady = pid.blokada -  uplynelo;
            gracz.sendMessage("Mozes podjac probe latania za : "+czasBlokady+ " sekund.");
            return;
        }

        gracz.setFoodLevel(gracz.getFoodLevel() - pid.startLotZabierzGlod);
        gracz.setHealth(gracz.getHealth() - pid.startLotZabierzZdrowie);
        pid.lotCzas = ustawCzasLotu();
        pid.aktywny = true;
        ikarzy.put(gracz.getUniqueId(),pid);
        gracz.setAllowFlight(true);
        gracz.sendMessage("Latanie to kosztowna czynnosc.");
        System.out.println("[Ikar] - aktywacja dla : "+gracz.getName()+" sukces.");
    }

    /**
     * Dzialania niezbedne na zdarzenie zdjecia perka
     * @param gracz obiekt gracza
     */
    @Override
    public  void dezaktywuj(Player gracz){
        System.out.println("[DEAKTYWACJA](Ikar): "+nazwaId+" - start ");
        pid = pobierzLotnika(gracz.getUniqueId());
        if (pid != null){
            pid.aktywny = false;
            ikarzy.put(gracz.getUniqueId(),pid);
        }
        gracz.setAllowFlight(false);
        gracz.setFlying(false);
        System.out.println("[DEAKTYWACJA](Ikar) - dezaktywacja sukces.");
        gracz.sendMessage("Uraciles moc latania.");
    }

}
