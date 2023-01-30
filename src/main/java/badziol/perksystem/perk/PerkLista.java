package badziol.perksystem.perk;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.CiezkaLapa.PerkCiezkaLapa;
import badziol.perksystem.perk.Ikar.PerkIkar;
import badziol.perksystem.perk.Kevlar.PerkKevlar;
import badziol.perksystem.perk.KrwawiaceRany.PerkKrwawiaceRany;
import badziol.perksystem.perk.Lowca.PerkLowca;
import badziol.perksystem.perk.Niejadek.PerkNiejadek;
import badziol.perksystem.perk.Nieustawiony.PerkNiesustawiony;
import badziol.perksystem.perk.Sprinter.PerkSprinter;
import badziol.perksystem.perk.Treser.PerkTreser;
import badziol.perksystem.perk.Wampir.PerkWampir;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static badziol.perksystem.perk.PerkStale.*;

public class PerkLista {
    private final PerkSystem plugin;
    private final ArrayList<Perk> wszystkiePerki = new ArrayList<>(); // wszystkie dostepne perki w grze
    private final ArrayList<PosiadaczPerkow> posiadaczePerkow = new ArrayList<>(); //lista graczy wraz z ich dostepnymi i aktywnymi perkami

    /**
     * Konstruktor dadaje wszystkie zdefiniowane perki do swojej wewnetrznej listy.
     */
    public PerkLista(PerkSystem plugin){
        this.plugin = plugin; //dalej do przekazania referencji plugina do klas perkow
        System.out.println("[PL] - tworze liste bazowa perkow.");
        wszystkiePerki.add(new PerkNiesustawiony(this.plugin));
        wszystkiePerki.add(new PerkNiejadek(this.plugin));
        wszystkiePerki.add(new PerkSprinter(this.plugin));
        wszystkiePerki.add(new PerkIkar(this.plugin));
        wszystkiePerki.add(new PerkCiezkaLapa(this.plugin));
        wszystkiePerki.add(new PerkWampir(this.plugin));
        wszystkiePerki.add(new PerkKevlar(this.plugin));
        wszystkiePerki.add(new PerkKrwawiaceRany(this.plugin));
        wszystkiePerki.add(new PerkTreser(this.plugin));
        wszystkiePerki.add(new PerkLowca(this.plugin));
    }

    /**
     * Metoda przeszukuje liste wszystkich dostepnych perkow.
     * Wyszukuje perka o wskazanej nazwie.
     * @return Obiekt perk - jesli znaleziono , null w przeciwnym przypadku.
     */
    public Perk wezPerk(String nazwaId){
        for (Perk tenPerk : wszystkiePerki){
            if (tenPerk.nazwaId.equalsIgnoreCase(nazwaId)) return tenPerk;
        }
        return null;
    }
    /**
     * Pobierz wszystkie dane gracza(jako posiadacza perkow) o wskazanym imieniu
     * Posiadacz przechowuje klucze (nazwaId) perka nie zas same perki
     * @param imie szukanego gracza
     * @return obiekt gracza lub null jesli nie znaleziono o wskazanym imieniu
     */
    public PosiadaczPerkow pobierzPosiadacza(String imie){
        for (PosiadaczPerkow tenGracz : posiadaczePerkow){
            if (tenGracz.imie.equalsIgnoreCase(imie))  return tenGracz;
        }
        return null;
    }

    /**
     * Wypisz najistotniejsze dane dotyczace posiadacza perkow.
     * @param imie posiadacza
     */
    public void wypiszPosiadacza(String imie){
        PosiadaczPerkow posiadacz = pobierzPosiadacza(imie);
        if (posiadacz != null){
            System.out.println("------------DANE POSIADACZA PERKOW ------------");
            System.out.println(posiadacz.wypisz());
            System.out.println("-----------------------------------------------");
        }else{
            System.out.println("[PL](wypisz posiadacza) - nie udalo sie znalezc gracza o imieniu :"+imie);
        }
    }

    /**
     *  Wyszukuje posiadacza perkow  po imieniu.
     *  Sprawdzam liste jego aktywnych perkow na podstawie klucza - nazwaId
     *  Tworze liste samych przedmiotow/glowek na tej podstawie
     *  Metoda potrzebna do budowy gui.
     * @param imie gracza
     * @return lista glowek lub null jesli nie znajde gracza
     */
    public ArrayList<ItemStack> pobierzAktywneGlowki(String imie){
        PosiadaczPerkow posiadacz = pobierzPosiadacza(imie);
        if (posiadacz != null) {
            ArrayList<ItemStack> tmp = new ArrayList<>();
            for (String nazwaKlucz : posiadacz.aktywnePerki) {
                Perk tenPerk = wezPerk(nazwaKlucz);
                tmp.add(tenPerk.wezGlowke());
            }
            return tmp;
        }
        return null;
    }

    /**
     * Wyszukuje posiadacza perkow po imieniu.
     * Sprawdzam liste jego zdobytych perkow. Na podstawie kluczy (nazwaId)
     * Tworze liste przedmiotow glowek reprezentujacych perka.
     * Metoda potrzebna do tworzenia gui.
     * @param imie gracza na liscie osiadaczy perkow
     * @return lista glowek , lub null jesli nie znaleziono gracza.
     */
    public ArrayList<ItemStack> pobierzZdobyteGlowki(String imie){
        PosiadaczPerkow posiadacz = pobierzPosiadacza(imie);
        if (posiadacz != null){
            ArrayList<ItemStack> tmp = new ArrayList<>();
            for (String nazwaKlucz : posiadacz.zdobytePerki) {
                Perk tenPerk = wezPerk(nazwaKlucz);
                tmp.add(tenPerk.wezGlowke());
            }
            return tmp;
        }
        return null;
    }

    /**
     *  Metoda wyszukuje posiadacza perkow o wskazanym imieniu.
     *  Na podstawie zdobytych perkow i aktywnych perkow tworzy liste
     *  ktora jest roznica pomiedzy nimi. Motoda uzywana przy zamianie wybranego perka na inny w GUI.
     *  Zapobiega dupikacji perka w aktywnych perkach, jednoczesnie wyklucza mozliwosc zamiany
     *  perka na takiego samego.
     * @param imie posiadacza perka
     * @return lista - roznica pomiedzy zdobytymi a aktywnymi perkami, null jesli nie odnaleziono gracza
     */
    public ArrayList<ItemStack> pobierzGlowkiWybor(String imie){
        ArrayList<ItemStack> zdobyte;
        ArrayList<ItemStack> aktywne;
        zdobyte = pobierzZdobyteGlowki(imie);
        aktywne = pobierzAktywneGlowki(imie);
        if (zdobyte != null && aktywne != null) {
            zdobyte.removeAll(aktywne);
            Perk tmpPerk = new PerkNiesustawiony(plugin);
            zdobyte.add(tmpPerk.wezGlowke());
            return zdobyte;
        }else{
            return null;
        }
    }

    /**
     * Lista wszystkich dostępnych perkow w grze,
     * @return zwroc stringa przypominajacego tabele
     */
    public String listaPerkow(){
        StringBuilder ret= new StringBuilder();
        ret.append("---------------------------------\n");
        ret.append("Lista dostepnych perkow w grze.\n");
        for (Perk perk: wszystkiePerki){
            ret.append(perk.nazwaId);
            ret.append(" \n");
        }
        ret.append("---------------------------------\n");
        return ret.toString();
    }

    /**
     * @return Metoda zwraca ArrayList zawierajace nazwyId (klucze) wszystkich dostepnych
     * perkow w grze.
     */
    public ArrayList<String> nazwyPerkow(){
        ArrayList<String> ret = new ArrayList<>();
        for (Perk tenPerk : wszystkiePerki){
            ret.add(tenPerk.nazwaId);
        }
        return ret;
    }

    /**
     * Pobierz index gracza z listy raczow na podstawie imienia
     * @param imie - poszukiwane imie
     * @return - index w tablicy , -1 jeśli gracza nie odnaleizono o podanym imieniu
     */
    public int indexPosiadacza(String imie){
        for (int i = 0; i < posiadaczePerkow.size(); i++) {
            if (posiadaczePerkow.get(i).imie.equalsIgnoreCase(imie)) return i;
        }
        return -1;
    }

    /**
     * Dodaj nowego gracza jako posiadacza listy perkow
     * Sprawdz czy gracz nie wystepuje juz na liscie
     * @param imie - nowego graacza
     * @return true jesli sie udalo
     */
    public boolean dodajPosiadacza(String imie){
        int indGracza = indexPosiadacza(imie);  // czy gracz jest na naszej prywatnej liscie - NIE ONLINE,
        // nie wszyscy gracze musza miec perki
        //nie ma gracza na liscie , trzeba go utworzyc
        if (indGracza == -1){
            System.out.println("[PL](dodajGracza) - nie znalazlem gracza na mojej liscie, proboje utworzyc");
            PosiadaczPerkow nowyGracz = new PosiadaczPerkow();
            Player p = Bukkit.getPlayerExact(imie);
            if (p == null){
                System.out.println("[PL](dodajGracza) - serwer nie znalazl gracza :"+imie+" BLAD KRYTYCZNY");
                return false;
            }
            nowyGracz.imie = imie;
            nowyGracz.uuid = p.getUniqueId();
            //reszte parametrow zostawiamy domyslna
            posiadaczePerkow.add(nowyGracz);
            System.out.println("[PL](dodajGracza) - gracz "+imie+" dodany do listy posiadaczy perkow.");
            return true;
        }else{
            System.out.println("[PL](dodajGracza) - gracz "+imie+" juz wystepuje na liscie.");
            return false;
        }
    }

    /**
     *  Sprawdz czy gracz o konkretnym imieniu zdobyl perka o wskazanej nazwie
     * @param imie - poszukiwanego gracza
     * @param perkNazwa - nazwa klucz perka
     * @return >=0 index posiadanego perka , -1 lista graczy pusta , -2 nie znaleziono gracza ,
     * -3 gracz nie posiada zdobytego perka , -4 nieznany blad
     */
    public int czyPosiadaZdobyty(String imie , String perkNazwa){
        if (posiadaczePerkow.size()== 0) return -1; // lista graczy pusta
        for (PosiadaczPerkow tenGracz : posiadaczePerkow){ //bierz po kolei wszystkich graczy
            if (tenGracz.imie.equalsIgnoreCase(imie)){ //znaleziono gracza o imieniu....
                int index = tenGracz.czyPosiadaPerkaZdobyty(perkNazwa); //sprawdz z poziomu konkretnego gracza
                if (index>= 0){
                    System.out.println("[PL](czyPosiadaZdobyty) - "+imie+" perka "+perkNazwa+" , index : "+index);
                    return index;
                }else{
                    System.out.println("[PL](czyPosiadaZdobyty) -"+imie+" nie posiada : "+perkNazwa);
                    return -3;
                }
            }else{
                System.out.println("[PL](czyPosiadaZdobyty) - nie znaleziono posiadacza o imieniu :"+imie);
                return -2;
            }
        }
        return -4;
    }

    /**
     * Metoda sprawdza liste wszystkich dostepnych perkow pod kontem wystepowania
     * okreslonej nazwy - klucza.
     * @param nazwa -szukana nazwa perka
     * @return true - jesli perk wystepuje
     */
    public boolean czyPerkWystepuje(String nazwa){
        for (Perk tenPerk : wszystkiePerki){
            if (tenPerk.nazwaId.equalsIgnoreCase(nazwa)) return true;
        }
        return false;
    }

    /**
     *  Proba dodania perka graczowi , sprawdzane są następujące rzeczy :
     *  Czy gracz wystepuje na liscie osob posiadajacych perki , czy udalo sie utworzyc taki obiekt,
     *  Czy podana nazwa perka jest zdefiniowana(wystepuje),
     *  Czy przypadkiem nie jest to zastrzezony perk 'nieustawiony'

     * @param imie gracza ktoremu ma byc przypisany perk
     * @param perkNazwa nazwa dodawanego perka
     * @return true jesli sukces
     */
    public boolean dodajPerkaZdobyty(String imie , String perkNazwa){
        if (perkNazwa.equalsIgnoreCase(PERK_NIEUSTAWIONY)){
            System.out.println("[PL](dodajPerkaGraczowi) - odrzucona proba dodania perka 'nieustawiony' ");
            return false;
        }
        int indGracza = indexPosiadacza(imie);  // czy gracz jest na naszej prywatnej liscie - NIE ONLINE,
        // nie wszyscy gracze musza miec perki
        //nie ma gracza na liscie , trzeba go utworzyc
        if (indGracza == -1){
            if (!dodajPosiadacza(imie)) return false; //wystapil blad , nie udalo sie utworzyc gracza
        }
        // sprawdzamy czy podana nazwa dla perka jest poprawna
        if (!czyPerkWystepuje(perkNazwa)){
            System.out.println("[PL](dodajPerkaGraczowi) - nie znaleziono perka o nazwie :"+perkNazwa);
            return false;
        }

        //sprawdzamy czy gracz juz posiada tego perka
        int indPerka = czyPosiadaZdobyty(imie,perkNazwa);
        if (indPerka == -3){ //gracz nie ma tego perka , zatem mozemy go dodac
            System.out.println("[PL](dodajPerkaGraczowi) - dodaje perka....");
            PosiadaczPerkow tenGracz = posiadaczePerkow.get(indexPosiadacza(imie));
            return tenGracz.dodajPerkaZdobyty(perkNazwa); //tak czy siak ta metoda sprawdza czy nazwa jest unikalna
        }else{
            System.out.println("[PL](dodajPerkaGraczowi) - "+imie+" - nie moge dodac perka  powod :"+indPerka);
            return false;
        }
    }

    /**
     *  Dodaj wszystkie mozliwe perki graczowi
     * @param imie szczesliwego gracza
     * @return true jesli wszystko poszlo ok
     */
    public boolean dodajWszystkiePerkiGraczowi(String imie){
        int indGracza = indexPosiadacza(imie);  // czy gracz jest na naszej prywatnej liscie - NIE ONLINE,
        // nie wszyscy gracze musza miec perki
        //nie ma gracza na liscie , trzeba go utworzyc
        if (indGracza == -1){
            if (!dodajPosiadacza(imie)) return false; //wystapil blad , nie udalo sie utworzyc gracza
        }
        PosiadaczPerkow tenGracz = posiadaczePerkow.get(indexPosiadacza(imie));
        tenGracz.usunZdobytePerki();
        for (Perk perk : wszystkiePerki){
            tenGracz.dodajPerkaZdobyty(perk.nazwaId);
        }
        return true;
    }

    /**
     *  Usun perka graczowi o wskazanym imieniu z listy zdobytych prekow
     *  Metoda sprawdza czy gracz wystepuje na liscie posiadaczy perkow
     *  Czy gracz faktycznie posiada perka o wskazanej nazwie
     *  Czy przypadkiem nie jest to zastrzezony perk 'nieustawiony'
     * @param imie gracza
     * @param perkNazwa -nazwaId perka
     * @return true - jesli perk zostal usuniety
     */
    public  boolean usunPerkaZdobyty(String imie, String perkNazwa){
        if (perkNazwa.equalsIgnoreCase(PERK_NIEUSTAWIONY)){
            System.out.println("[PL](usunPerkaGraczowi) - odrzucona proba usuniecia perka 'nieustawiony' ");
            return false;
        }

        //czy gracz wystepuje
        int indGracza = indexPosiadacza(imie);  // czy gracz jest na naszej prywatnej liscie - NIE ONLINE,
        if (indGracza == -1){
            System.out.println("[PL](usunPerkaGraczowi) - brak gracza :"+imie);
            return false;
        }
        PosiadaczPerkow tenGracz = posiadaczePerkow.get(indexPosiadacza(imie));
        if (tenGracz.czyPosiadaPerkaZdobyty(perkNazwa)>=0){
            return tenGracz.usunPerkaZdobyty(perkNazwa); //true / false
        }else{
            System.out.println("[PL](usunPerkaGraczowi) - "+imie+ " nie posiadal perka : "+perkNazwa);
            return false;
        }
    }

    /**
     * Metoda usuwa wszystkie zdobyte perki graczowi o wskazanym imieniu
     * @param imie nieszczesnego gracza
     * @return true - jesli usunieto poprawnie wszystkie perki zdobyte
     */
    public boolean usunWszystkieZdobyte(String imie){
        int indGracza = indexPosiadacza(imie);  // czy gracz jest na naszej prywatnej liscie - NIE ONLINE,
        if (indGracza == -1){ //nie znaleziono gracza
            System.out.println("[PL](usunWszystkie) - brak gracza :"+imie);
            return false; //wystapil blad , nie udalo sie utworzyc gracza
        }
        PosiadaczPerkow tenGracz = posiadaczePerkow.get(indexPosiadacza(imie));
        tenGracz.usunZdobytePerki();
        return true;
    }

    /**
     * Metoda sprawdza czy gracz o wskazanym imieniu wystepuje na liscie.
     * Metoda sprawdza czy nowy limit miesci sie w zakresie LIMIT_MIN...LIMIT_MAX
     * @param imie poszukiwanego gracza
     * @param nowyLimit aktywnych perkow
     * @return true jesli wszystko jest w porzadku
     */
    public boolean ustawLimit(String imie, int nowyLimit){
        if (nowyLimit< LIMIT_AKTYWNYCH_MIN || nowyLimit > LIMIT_AKTYWNYCH_MAX){
            System.out.println("[PL](ustawLimit) - limit, poza zakresem(1..5)");
            return false;
        }
        int index = indexPosiadacza(imie);
        if (index == -1){
            System.out.println("[PL](ustawLimit) - nie odnaleziono gracza o imieniu : "+imie);
            return false;
        }
        PosiadaczPerkow posiadacz = posiadaczePerkow.get(indexPosiadacza(imie));
        if (posiadacz.limit == nowyLimit){
            System.out.println("[PL](ustawLimit) - limit nie ulegl zmianie.");
            return true;
        }
        // kontrola poprawnosci wypelnienia tablicy aktywnePerki
        if (nowyLimit > posiadacz.limit){ //trzeba dodac perk/perki "nieustawiony"
            for (int i = 0; i <(nowyLimit - posiadacz.limit) ; i++) {
                posiadacz.aktywnePerki.add(PerkStale.PERK_NIEUSTAWIONY);
            }
        }else { //mniejsza lista aktywnych perkow - trzeba obciac
            //najpierw dezaktywacja :
            Player tmpGracz = Bukkit.getPlayerExact(imie);
            if (tmpGracz == null){
                System.out.println("[PL](ustawLimit) - Uwaga ! serwer nie odlanazl gracz o imieniu :"+imie);
                return  false;
            }
            for (int i= nowyLimit ; i< posiadacz.limit; i++){
                Perk tenPerk = wezPerk(posiadacz.aktywnePerki.get(i));
                if (tenPerk != null){
                    tenPerk.dezaktywuj(tmpGracz);
                }
            }
            // obciecie listy perkow
            while (posiadacz.aktywnePerki.size() > nowyLimit){
                posiadacz.aktywnePerki.remove(posiadacz.aktywnePerki.size() - 1);
            }
        }

        posiadacz.limit = nowyLimit;
        posiadaczePerkow.set(index,posiadacz); //uaktualnij dane na liscie
        return true;
    }

    /**
     * Sprawdz czy gracz (jako posiadacz perkow) o wskazanym imieniu posiada perk
     * @param imie gracza
     * @param nazwaPerka zgodna z nazwaId
     * @return true jesli posiada
     */
    public boolean czyPosiadaAktywny(String imie, String nazwaPerka){
        PosiadaczPerkow posiadacz = pobierzPosiadacza(imie);
        if (posiadacz != null){
            int index = posiadacz.czyPerkJestWaktywnych(nazwaPerka);
            return index >= 0; //zwroci true jesli index >=0
        }
        return false;
    }


    /**
     *  Zdejmij aktywne perki pezez dezaktywacje dzialania ich wszystkich
     *  W przypadku wygrycia nieobslugiwanego klucza ustaw klucz na 'nieustawiony'
     * @param imie gracza
     * @return true jesli caly proces przebiegl poprawnie
     */
    public boolean zdejmijAktywnePerki(String imie){
        PosiadaczPerkow posiadacz = pobierzPosiadacza(imie);
        if (posiadacz == null) {
            System.out.println("[PL](zdejmij aktywne) - uwaga ! nie udalo sie znalezc gracza po imieniu.");
            return false;
        }
        Player gracz = Bukkit.getPlayerExact(imie);
        if (gracz == null){
            System.out.println("[PL](zdejmij aktywne) - uwaga ! serwer nie odnalazl gracza : "+imie);
            return false;
        }

        for (int i = 0; i <posiadacz.aktywnePerki.size() ; i++) {
            String perkId = posiadacz.aktywnePerki.get(i);
            if (perkId.equalsIgnoreCase(PERK_NIEUSTAWIONY)){
                System.out.println("[PL](zdejmij aktywne) - perk 'nieustawiony' - pomijam");
                continue;
            }

            Perk tenPerk = wezPerk(perkId);
            if (tenPerk == null){
                System.out.println("[PL](zdejmij aktywne) - uwaga! wykryto nieobslugiwanego perka : "+perkId+
                        " ustawiam klucz :"+PERK_NIEUSTAWIONY);
                posiadacz.ustawPerkAktywny(i,PERK_NIEUSTAWIONY);
            }else{
                tenPerk.dezaktywuj(gracz);
                posiadacz.ustawPerkAktywny(i,PERK_NIEUSTAWIONY);
            }
        }
        return true;
    }


    /**
     * Metoda sprawdza liste wszystkich posiadaczy perkow pod katem :
     * Czy ilosc aktywnych perkow jest zgodna z limitem
     * Czy aktywne perki wystepuja na liscie wszystkich perkow, jesli nie , ustaw  perk na 'nieustawiony'
     * Czy zdobyte perki wystepuja na liscie wszystkich perkow, jesli nie , ustaw perk na 'nieustawiony'
     *
     * @return true - jesli nie wykrto zadnych problemow , false - jesli zaszla potrzeba dokonania zmian
     */
    public boolean kontrolaIntegralnosciDanych(){
        boolean tmp = true;
        for (PosiadaczPerkow posiadacz : posiadaczePerkow){
            //limit
            if (posiadacz.limit != posiadacz.aktywnePerki.size()){
                System.out.println("[PL](kontrola) - niezgodny limit z stanem faktycznym u gracza :"+posiadacz.imie);
                ustawLimit(posiadacz.imie,posiadacz.limit);
                tmp = false;
            }
            //test aktywne
            for (int i = 0; i <posiadacz.aktywnePerki.size() ; i++) {
                String nazwa = posiadacz.aktywnePerki.get(i);
                if (!czyPerkWystepuje(nazwa)){
                    System.out.println("[PL](kontrola) - gracz "+posiadacz.imie +" posiadal aktywny nieznany perk :"+nazwa);
                    posiadacz.ustawPerkAktywny(i,PERK_NIEUSTAWIONY);
                    tmp = false;
                }
            }
            //test zdobyte
            for(int i = 0; i<posiadacz.zdobytePerki.size(); i++){
                String nazwa = posiadacz.zdobytePerki.get(i);
                if (!czyPerkWystepuje(nazwa)){
                    System.out.println("[PL](kontrola) - gracz "+posiadacz.imie +"posiadal zdobyty nieznany perk:"+nazwa);
                    posiadacz.usunPerkaZdobyty(nazwa);
                    tmp = false;
                }
            }
        }
        return tmp;
    }
    // ========================================================================================
    /**
     * Wczytaj dane z pliku
     * @throws IOException bledy odczytu pliku
     */
    public boolean wczytajPlik()throws IOException {
        Gson gson = new Gson();
        File file = new File(PerkSystem.getPlugin().getDataFolder().getAbsolutePath() + NAZWA_PLIKU);
        if (file.exists()){
            Reader reader = new FileReader(file);
            PosiadaczPerkow[] nowePerki = gson.fromJson(reader, PosiadaczPerkow[].class);
            posiadaczePerkow.addAll(Arrays.asList(nowePerki));
            System.out.println("[PL] Perki wczytane.");
            return true;
        }else{
            System.out.println("[PL] - plik z perkami graczy  nie istnieje.");
            return false;
        }
    }

    /**
     * Zapisz dane do pliku
     * @throws IOException bledy zapisu pliku
     */
    public void zapiszPlik()throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        //Gson gson = new Gson();
        File file = new File(PerkSystem.getPlugin().getDataFolder().getAbsolutePath() + NAZWA_PLIKU);
        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);

        gson.toJson(posiadaczePerkow, writer);
        writer.flush();
        writer.close();
        System.out.println("[PL] Perki zapisane.");
    }

    // ========================================================================================
    /**
     * Informacje do debugowania co w pamieci siedzi. Wszystkie istotne dane posiadaczy perkow
     * Zrzut kompletnej listy
     */
    public void dane(){
        System.out.println("--------PerkLista dane-------------------");
        for(PosiadaczPerkow posiadaczPerkow : posiadaczePerkow){
            System.out.println(posiadaczPerkow.wypisz());
            System.out.println("---");
        }
        System.out.println("-----------------------------------------");
    }

    public void daneAktywneZdobyte(String imie){
        for(PosiadaczPerkow posiadaczPerkow : posiadaczePerkow){
            if (posiadaczPerkow.imie.equalsIgnoreCase(imie)){
                System.out.print("Aktywne :");
                System.out.println(posiadaczPerkow.aktywnePerki);
                System.out.print("Zdobyte :");
                System.out.println(posiadaczPerkow.zdobytePerki);
                return;
            }
        }
        System.out.println("[PL](aktywneZdobyte) - nie znalazlem posiadacza o imieniu : "+imie);
    }
}