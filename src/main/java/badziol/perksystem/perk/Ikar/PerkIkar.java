package badziol.perksystem.perk.Ikar;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Ikar.Skrzydla.SkrzydloCzastka;
import badziol.perksystem.perk.Ikar.Skrzydla.SkrzydloKonfig;
import badziol.perksystem.perk.Ikar.Skrzydla.SkrzydloStrona;
import badziol.perksystem.perk.Perk;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

//Perk korzysta z PerkIkarTask-taki kontroler lotów naszych ikarów.
public class PerkIkar extends Perk implements Listener{
    private PerkIkarData pid = new PerkIkarData(); //tu przechowywane są wszystkie dane naszego lotnika
    public final HashMap<UUID, PerkIkarData> ikarzy = new HashMap<>();

    private final SkrzydloKonfig skrzydloKonfig;
    private final ArrayList<Player> skrzydlaciGracze;
    private BukkitTask zadanieSkrzydla;
    /**
     * Konstruktor klasy
     * @param plugin - referencja do pluginu, by miec dostep do perkLista
     */
    public PerkIkar(PerkSystem plugin) {
        super(plugin); //wywolaj konstruktor nadrzędny
        nazwaId= PerkStale.PERK_IKAR;
        wyswietlanie="Ikar";
        opis.add("Od tej chwili zyskujesz mozliwosc");
        opis.add("krotkotrwalego latania");
        //https://minecraft-heads.com/custom-heads/animals/25787-fly
        textura="eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3J"+
                "hZnQubmV0L3RleHR1cmUvMTVmZjkyNGY3YjQ0NGYzOTE2MmJhZDMwMWZkYzE3Nzk3Yz"+
                "YxNmUxODcxZGRmODJjYTdmMWQyMzFhNTczOTRlIn19fQ==";
        efektWidoczny = true;
        inicjujGlowke();
        this.skrzydloKonfig = new SkrzydloKonfig();
        this.skrzydlaciGracze = new ArrayList<>();
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
    }

    /**
     * Metoda pomocnicza pozwala nam sprawdzić, w jakiej fazie lotu znajduje się wskazany gracz.
     * @param uuid - wyszukiwany gracz na liście ikarów
     * @return aktualna faza lotu, jeśli gracza nie odnaleziono na liście, zawsze zostanie zwrócona
     * 'bezpieczna' faza.
     */
    private FazaLotu jakaFazaLotu(UUID uuid){
        PerkIkarData tenIkar = ikarzy.get(uuid);
        if (tenIkar != null) return tenIkar.fazaLotu;
        else{
            System.out.println("[Ikar](jakaFazaLotu) nie znaleziono ikara o wskazanym uuid.");
            return FazaLotu.BEZPIECZNA;
        }
    }

    /**
     * Dodaj gracza do listy posiadaczy skrzydeł.
     * Pojawienie się na liście choćby jednego gracza uruchomi proces (zadanie) obsługi animacji
     * @param player - docelowy gracz
     */
    public void dodajGraczaSkrzydla(Player player) {//CWPlayer bylo
        skrzydlaciGracze.add(player);
        System.out.println("[Ikar](skrzydla) - dodano gracza : "+player.getName()+"  , ilu :"+skrzydlaciGracze.size());
        if (skrzydlaciGracze.size() == 1){
            System.out.println("Uruchamiam proces odpowiedzialny za animacje skrzydel.");
            this.zadanieStrzydlaStart();
        }
    }

    /**
     * Usuń gracza z listy posiadaczy skrzydeł
     * @param player - docelowy gracz
     */
    public void usunGraczaSkrzydla(Player player) {
        System.out.println("[Ikar](skrzydla) - usunieto gracza : "+player.getName());
        skrzydlaciGracze.remove(player);
    }

    /**
     * Zadanie odpowiedzialne za ...
     * W przypadku braku osób ze skrzydłami, zadanie samo się zatrzyma.
     */
    private void zadanieStrzydlaStart() {
        zadanieSkrzydla = new BukkitRunnable() {
            boolean zmienKierRuchu = false; //zmiana kierunku ruchu skrzydel
            int animStan = skrzydloKonfig.katPoczatek;
            @Override
            public void run() {
                if (skrzydlaciGracze.isEmpty()) this.cancel(); //nikt nie ma skrzydeł to zatrzymaj zadanie
                if (skrzydloKonfig.animacjaSkrzydel) {
                    animStan = zmienKierRuchu ? animStan - skrzydloKonfig.animacjaSzybkosc
                            : animStan + skrzydloKonfig.animacjaSzybkosc;
                    // zmien kierunek przy skrajnych pozycjach skrzydła
                    if (animStan >= skrzydloKonfig.katKoniec)  zmienKierRuchu = true;
                    if (animStan <= skrzydloKonfig.katPoczatek) zmienKierRuchu = false;
                }
                // Uaktualnij animację wszystkim graczom ze skrzydłami
                skrzydlaciGracze.forEach((posiadacz) ->
                        rysujSkrzydla(posiadacz,jakaFazaLotu(posiadacz.getUniqueId()), animStan)
                );
            }
        }.runTaskTimerAsynchronously(plugin, 0, skrzydloKonfig.zadanieCzas);
    }

    private void rysujSkrzydla(Player posiadacz, FazaLotu faza,  int animStan) {
        Location pozycjaSkrzydel = posiadacz.getLocation();
        if (!mogePokazacSkrzydla(posiadacz)) return;
        float bodyYaw = wezObrotCiala(posiadacz);
        pozycjaSkrzydel.setYaw(bodyYaw);
        if (posiadacz.isSneaking() && !posiadacz.isFlying()) {
            pozycjaSkrzydel = pozycjaSkrzydel.add(0, -0.25, 0);
        }
        double yawRad = Math.toRadians(pozycjaSkrzydel.getYaw());
        double xOffset = Math.cos(yawRad) * skrzydloKonfig.poczatekPoziom;
        double zOffset = Math.sin(yawRad) * skrzydloKonfig.poczatekPoziom;
        pozycjaSkrzydel = pozycjaSkrzydel.add(xOffset, 0, zOffset);

        //Na podstawie współrzędnych narysuj skrzydła
        if (faza == FazaLotu.BEZPIECZNA) {
            for (double[] wspolrzedne : skrzydloKonfig.czastkiBezpiecznyLot.keySet()) {
                SkrzydloCzastka skrzydloCzastka = skrzydloKonfig.czastkiBezpiecznyLot.get(wspolrzedne);
                double x = wspolrzedne[0];
                double y = wspolrzedne[1];
                //odrysuj lewe skrzydlo
                Location czastkaLewo = obliczCzastkaPrzestrzen(pozycjaSkrzydel, x, y, SkrzydloStrona.LEWA, animStan);
                skrzydloCzastka.rysujCzastke(czastkaLewo, posiadacz, SkrzydloStrona.LEWA);
                //odrysuj prawe skrzydlo
                Location czastkaPrawo = obliczCzastkaPrzestrzen(pozycjaSkrzydel, x, y, SkrzydloStrona.PRAWA, animStan);
                skrzydloCzastka.rysujCzastke(czastkaPrawo, posiadacz, SkrzydloStrona.PRAWA);
            }
        }else{ //z automatu niebezpieczna
            for (double[] wspolrzedne : skrzydloKonfig.czastkiNiebezpiecznyLot.keySet()) {
                SkrzydloCzastka skrzydloCzastka = skrzydloKonfig.czastkiNiebezpiecznyLot.get(wspolrzedne);
                double x = wspolrzedne[0];
                double y = wspolrzedne[1];
                //odrysuj lewe skrzydlo
                Location czastkaLewo = obliczCzastkaPrzestrzen(pozycjaSkrzydel, x, y, SkrzydloStrona.LEWA, animStan);
                skrzydloCzastka.rysujCzastke(czastkaLewo, posiadacz, SkrzydloStrona.LEWA);
                //odrysuj prawe skrzydlo
                Location czastkaPrawo = obliczCzastkaPrzestrzen(pozycjaSkrzydel, x, y, SkrzydloStrona.PRAWA, animStan);
                skrzydloCzastka.rysujCzastke(czastkaPrawo, posiadacz, SkrzydloStrona.PRAWA);
            }
        }
    }

    /**
     *  Określ wszystkie warunki wykluczające pokazanie skrzydeł np:
     *  czy gracz żyje, jest widoczny, śpi , pływa, itp.
     * @param posiadacz - badany gracz
     * @return true- można wyświetlić
     */
    private boolean mogePokazacSkrzydla(Player posiadacz) {
        // Gracz jest martwy
        if (posiadacz.isDead()) return false;
        // Gracz w trybie spectatora.
        if (posiadacz.getGameMode().equals(GameMode.SPECTATOR)) return false;
        // Gracz jest w 'inny' sposób niewidoczny.
        if (czyGraczZniknal(posiadacz)) return false;
        // Gracz jest niewidoczny
        if (posiadacz.isInvisible()) return false;
        // Gracz ma na sobie potiona niewidzialności
        if (posiadacz.hasPotionEffect(PotionEffectType.INVISIBILITY)) return false;
        // Gracz śpi
        if (posiadacz.isSleeping()) return false;
        // Gracz jest na zwierzęciu / pojeździe.
        if (posiadacz.isInsideVehicle()) return false;
        // Gracz płynie
        if (posiadacz.isSwimming()) return false;
        // Gracz szybuje- to coś innego niż lot.
        if (posiadacz.isGliding()) return false;
        return true;
    }

    /**
     *  Minecraft posiada jeszcze inny wariant niewidzialności, określany jako 'zniknięcie'.
     *  Sprawdzamy ten parametr.
     * @param player badany gracz
     * @return 'zniknięty' true
     */
    public static boolean czyGraczZniknal(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    /**
     * Na podstawie metadanych pobierz oś obrotu ciała. Uwaga! Plugin pobiera pole 'aY', które przechowuje tę wartość.
     * W wersjach 1.19 oraz 1.18 to jest właśnie to pole. Dla przykładu : w 1.17 to 'aX' , z kolei w 1.16.3 to 'aX' ,
     * 1.16.2 to 'aA', a 1.16.1 to 'aH'.
     * @param livingEntity - żywy byt
     * @return oś obrotu gracza lub null
     */
    public static Float wezObrotCiala(LivingEntity livingEntity) {
        try {
            String version = PerkSystem.getInstance().getServerVersion();
            Class<?> entity = Class.forName("org.bukkit.craftbukkit." + version+ ".entity.CraftLivingEntity");
            Method handle = entity.getMethod("getHandle");
            Object nmsEntity = handle.invoke(livingEntity);
            Field rotacja = nmsEntity.getClass().getField("aY");
            return (Float) rotacja.get(nmsEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Oblicz faktyczną pozycję cząstki w przestrzeni na podstawie danych
     */
    private Location obliczCzastkaPrzestrzen(Location loc, double x, double y, SkrzydloStrona skrzydloStrona, int animStan) {
        Location czastkaPozycja = loc.clone();
        float yaw = czastkaPozycja.getYaw();
        if (skrzydloStrona == SkrzydloStrona.LEWA)  yaw = yaw - animStan;
        if (skrzydloStrona == SkrzydloStrona.PRAWA)  yaw = yaw + 180 + animStan;
        double yawRad = Math.toRadians(yaw);
        Vector vector = new Vector(Math.cos(yawRad) * x, y, Math.sin(yawRad) * x);
        czastkaPozycja.add(vector);
        czastkaPozycja.setYaw((float) Math.toDegrees(yawRad));
        return czastkaPozycja;
    }

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
