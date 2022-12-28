package badziol.perksystem.komendy;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.PerkStale;
import badziol.perksystem.perk.PosiadaczPerkow;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;


public class komendaPerk implements CommandExecutor {
    private final PerkSystem plugin;

    public komendaPerk(PerkSystem plugin){
        this.plugin = plugin;
    }

    void wyswietlPomoc(Player p){
        p.sendMessage("Perk pomoc:");
        p.sendMessage("Perk lista - dostepne perki w grze.");
        p.sendMessage("Perk debug  - !!robocze!!");
        p.sendMessage("Perk [gracz] limit 1..5 - ustaw aktywne perki");
        p.sendMessage("Perk [gracz] + [nazwa perka] - dodaj graczowi perka");
        p.sendMessage("Perk [gracz] ++ - dodaj wszystkie dostepne perki");
        p.sendMessage("Perk [gracz] - [nazwa perka] - odbierz graczowi perka");
        p.sendMessage("Perk [gracz] -- odbierz wszystkie perki");
        p.sendMessage("Perk [gracz] napraw - domyslne ustawienia dla gracza");
    }

    void listaPerkow(Player p){
        String lista = plugin.perkLista.listaPerkow();
        p.sendMessage("Dostepne perki :");
        p.sendMessage(lista);
        System.out.println(lista);
    }
    void zapisz(){
        try {
            plugin.perkLista.zapiszPlik();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("[Perk] - Blad zapisu do pliku");
        }
    }

    /**
     * Metoda przywracajaca domyslne wartosci parametrow modyfikowanych przez perki
     */
    void napraw(Player gracz){
        System.out.println("[Perk] Uruchomiono tryb naprawy dla "+gracz.getName());
        gracz.closeInventory();

        boolean status = plugin.perkLista.zdejmijAktywnePerki(gracz.getName());
        System.out.println("Operacja dezaktywacji perkow gracza "+gracz.getName()+" -> "+status);
        plugin.perkLista.wypiszPosiadacza(gracz.getName());
        gracz.setWalkSpeed(PerkStale.PREDKOSC_CHODZENIA_DOMYSLNA);
        gracz.setAllowFlight(PerkStale.LOT);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;
            String imieGracza="";   // imie gracza na ktorym beda wykonywane operacje
            String akcja="";        // Co mam wykonac : +,++,-,--
            String nazwaPerka="";   // Tu będzie przechowywana nazwa perka , z ktorym bedziemy cos robic
            boolean status = false; // status powodzenia danej operacji

            //aby bylo czystelniej badamy przez pryzmat ilosci podanych parametrow:
            // 1) - brak parametrow - wyswietl pomoc
            if (args.length == 0){
                wyswietlPomoc(p);
                return true;
            }
            //2) - 1 parametr
            if (args.length == 1){

                if (args[0].equalsIgnoreCase("lista")) {
                    listaPerkow(p);
                }else if (args[0].equalsIgnoreCase("debug")) {
                    System.out.println("=======GRA DEBUG============");
                }else{
                    System.out.println("[Perk] - bledny pojedynczy parametr");
                }
                return true;
            }
            //3) - 2 parametry
            // mozliwe warianty :
            // [imie] ++    - dodaj wszystkie mozliwe perki graczowi
            // [imie] --    - usun graczowi wszystkie jego zdobyte perki
            if (args.length == 2){
                imieGracza = args[0];
                akcja = args[1];
                Player gracz = Bukkit.getServer().getPlayerExact(imieGracza); //to zwraca gracza online?
                //Player gracz = Bukkit.getServer().getPlayer(imieGracza); //NIE KASOWAC

                if(gracz == null){
                    p.sendMessage("Nie odnaleziono gracza.");
                    System.out.println("[Perk](2) - serwer nie ondalazl gracza o imieniu : "+imieGracza);
                    return true;
                }
                //dodanie wszystkich perkow
                if (akcja.equalsIgnoreCase("++")){
                    System.out.println("[Perk] - Proba dodania wszystkich perkow graczowi :"+imieGracza);
                    status = plugin.perkLista.dodajWszystkiePerkiGraczowi(imieGracza);
                    System.out.println("[Perk](dodaj wszystkie) status : "+status);
                    plugin.perkLista.daneAktywneZdobyte(imieGracza); //do debugowania
                    if (status) zapisz();
                }
                //usuniecie wszystkich perkow
                else if (akcja.equalsIgnoreCase("--")){
                    System.out.println("[Perk] - Proba usuniecia wszystkich graczowi : "+imieGracza);
                    status = plugin.perkLista.usunWszystkieZdobyte(imieGracza);
                    System.out.println("[Perk](usun wszystkie) status : "+status);
                    plugin.perkLista.daneAktywneZdobyte(imieGracza); //do debugowania
                    if (status) zapisz();
                }
                //przywrocenie domyslnych ustawien dla gracza
                else if(akcja.equalsIgnoreCase("napraw")){
                    napraw(p);
                }
                //nieznany parametr
                else{
                    System.out.println("[Perk] - nieznana akcja dla 2 parametrow.");
                }
                return true;
            }
            //4) - 3 parametry
            // mozliwe warianty :
            // [imie] + [nazwa perka]   - dodaj wybranego perka wskazanej osobie
            // [imie] - [nazwa perka]   - usun wskazanego perka danej osobie
            // [imie] limit 1..5        - ustaw nowy limit aktywnych perkow
            if (args.length == 3){
                imieGracza = args[0];
                akcja = args[1];
                nazwaPerka = args[2];

                Player gracz = Bukkit.getServer().getPlayerExact(imieGracza); //to zwraca gracza online?
                if(gracz == null){
                    p.sendMessage("Nie odnaleziono gracza.");
                    System.out.println("[Perk](3) - serwer nie ondalazl gracza o imieniu : "+imieGracza);
                    return true;
                }

                // dodanie pojedynczego perka
                if (akcja.equalsIgnoreCase("+")){
                    System.out.println("[Perk] - proba dodania perka graczowi.");
                    status = plugin.perkLista.dodajPerkaZdobyty(imieGracza, nazwaPerka);
                    System.out.println("[Perk](dodaj) status : "+status);
                    plugin.perkLista.daneAktywneZdobyte(imieGracza); //do debugowania
                    if (status) zapisz();
                }
                //usuniecie pojedynczego perka
                else if (akcja.equalsIgnoreCase("-")){
                    System.out.println("[Perk] - proba usuniecia perka graczowi.");
                    status = plugin.perkLista.usunPerkaZdobyty(imieGracza,nazwaPerka);
                    System.out.println("[Perk](usun) status : "+status);
                    plugin.perkLista.daneAktywneZdobyte(imieGracza); //do debugowania
                    if (status) zapisz();
                }
                //ustawienie nowego limitu perkow dla gracza
                else if (akcja.equalsIgnoreCase("limit")){
                    String limitStr = args[2];
                    boolean blad = false;
                    int nowyLimit = -1; //wstepnie ustawiony na blad
                    try{
                         nowyLimit = Integer.parseInt(limitStr);
                    }catch (Exception e){
                        blad = true;
                    }
                    if(!blad){
                        PosiadaczPerkow posiadacz = plugin.perkLista.pobierzPosiadacza(imieGracza);
                        if (posiadacz == null){
                            System.out.println("[PERK](limit) - nie udalo sie pobrac posiadacza o imieniu :"+imieGracza);
                            return true;
                        }
                        status = plugin.perkLista.ustawLimit(imieGracza,nowyLimit);
                        if (status) zapisz();
                    }else{
                        System.out.println("[PERK](limit) blad konwersji parametru na liczbe.");
                    }
                }
                //nieznany parametr
                else{
                    System.out.println("[Perk] - niezana akcja dla 3 parametrow");
                }
                return true;
            }
            System.out.println("[Perk] - zbyt wiele parametrow.");

        } //konsola
        else if (sender instanceof ConsoleCommandSender){
            if (args.length ==1){
                if (args[0].equalsIgnoreCase("debug")){
                    System.out.println("-----MAIN DEBUG ----");
                }
                else if (args[0].equalsIgnoreCase("debug2")){
                    System.out.println("-----HELP DEBUG 2 ----");
                }
                else{
                    System.out.println("Konsola - cos nie tak z parametrami");
                }
            }
        }
        return true;
    }
}