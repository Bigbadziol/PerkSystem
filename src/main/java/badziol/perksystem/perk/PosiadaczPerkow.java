package badziol.perksystem.perk;

import java.util.ArrayList;
import java.util.UUID;

import static badziol.perksystem.perk.PerkStale.PERK_NIEUSTAWIONY;

/**
 * Wazne zalozenie , przychodzone nazwy perkow musza byc juz poprawne, dotycza fizycznie zdefiniowanych.
 */
public class PosiadaczPerkow {
    public UUID uuid= UUID.randomUUID(); //scmieciowa wartosc na poczatek
    public String imie="";
    public int limit=3;
    public ArrayList<String> aktywnePerki = new ArrayList<>();
    public ArrayList<String> zdobytePerki = new ArrayList<>();

    //konstruktor
    public PosiadaczPerkow(){
        //wypelnij aktywne perki wstepnie na nieustawione zgodnie z limitem
        for (int i = 0; i <limit ; i++) {
            aktywnePerki.add(PERK_NIEUSTAWIONY);
        }
    }

    /**
     * Metoda zwraca stringa z akualnymi danymi posiadacza perka.
     * @return String z danymi
     */
    public String wypisz(){
        String ret="";
        StringBuilder pPerki = new StringBuilder(); //budowa stringa posiadanych perkow
        StringBuilder aPerki = new StringBuilder(); //budowa stringa aktywnych perkow
        ret += "uuid : "+uuid+"\n";
        ret += "imie : "+imie+"\n";
        ret += "limit: "+limit+"\n";
        ret += "posiada :";
        for (String tenPerk : zdobytePerki){
            pPerki.append(tenPerk);
            pPerki.append(" ");
        }
        ret += pPerki.toString();
        ret += "\n";
        ret += "aktywne : ";

        for (String tenPerk : aktywnePerki){
            aPerki.append(tenPerk);
            aPerki.append(" ");
        }
        ret += aPerki.toString();
        return ret;
    }

    /**
     * Spawdza czy gracz posiada perka o wskazanej nazwie
     * Zakladamy , ze na tym etapie nazwa wlasciwa perka jest poprawna, fizycznie jest zdefiniowany
     * @param nazwa - szukanego perka
     * @return - index na liscie , jesli blad  : -1
     */
    int czyPosiadaPerkaZdobyty(String nazwa){
        for (int i = 0; i < zdobytePerki.size() ; i++) {
            if (zdobytePerki.get(i).equalsIgnoreCase(nazwa) ) return i;
        }
        return -1;
    }

    /**
     * Dodaje perka graczowi do listy zdobytych perkow. Perk musi być unikalny na liście
     * Zakladamy , ze na tym etapie nazwa wlasciwa perka jest poprawna, fizycznie jest zdefiniowany
     * Blokowana jest proba usuniecia perka 'nieustawiony'
     * @param perkNazwa - nazwa perka
     * @return - jesli to nowy perk to : True , jesli jest juz na liscie : False
     */
    boolean dodajPerkaZdobyty(String perkNazwa){
        if (perkNazwa.equalsIgnoreCase(PERK_NIEUSTAWIONY)) return false;
        if (czyPosiadaPerkaZdobyty(perkNazwa) > 0) return false; //perk juz na liscie
        zdobytePerki.add(perkNazwa);
        return true;
    }

    /**
     *  Usuwa perka graczowi z listy zdobytych perkow. Funkcja sprawdza czy perk wystepuje na liscie.
     * Zakladamy , ze na tym etapie nazwa wlasciwa perka jest poprawna, fizycznie jest zdefiniowany
     * Blokowane jest usuniecie perka 'nieustawiony'
     * @param perkNazwa - nazwa perka
     * @return jesli wystepowal i usunieto to True , Jesli gracz nie posiadal to false
     */
    boolean usunPerkaZdobyty(String perkNazwa){
        if (perkNazwa.equalsIgnoreCase(PERK_NIEUSTAWIONY)) return false;
        int indexPerka = czyPosiadaPerkaZdobyty(perkNazwa);
        if (indexPerka == -1) return false; // tego perka nie ma na liscie
        zdobytePerki.remove(indexPerka);
        return true;
    }

    /**
     * Usun wszystkie zdobyte perki
     * Dodaj do listy 1 - 'nieustawiony'
     */
    void usunZdobytePerki(){
        zdobytePerki.clear();
        zdobytePerki.add(PERK_NIEUSTAWIONY);
    }


    /**
     * Metoda sprawdza i zwraca index perku o wskazanej nazwie w liscie aktywnych perkow
     * Na podstawie indeksu mozemy okreslic unikalnosc perka
     * @param nazwa szukanego perka
     * @return index perka  ,  jesli nie znaleziono to -1
     */
    int czyPerkJestWaktywnych(String nazwa){
        for (int i = 0; i <aktywnePerki.size() ; i++) {
            if (aktywnePerki.get(i).equalsIgnoreCase(nazwa)) return i;
        }
        return -1;
    }

    /**
     * Dodaj perka to listy aktywnych perkow
     * Sprawdz czy podany perk nie wystepuje na liscie (perk ma byc unikalny)
     * Zakladamy , ze na tym etapie nazwa wlasciwa perka jest poprawna, fizycznie jest zdefiniowany
     * Metoda pozwala na ustawienie 'nieustawionego' w dowolnym miejscu
     * @param nazwa perka
     * @return -1 perk juz jest na liscie, -2 przekroczony limit aktywnych perkow
     */
    public boolean ustawPerkAktywny(int index ,String nazwa){
        if (index <0 || index >= aktywnePerki.size()){
            System.out.println("[PP] - index poza rozmiarem tablicy.");
            return false;
        }
        aktywnePerki.set(index,nazwa);
        return true;
    };

    /**
     * Medoda podmienia klucz perka szukany za klucz zamiennik
     * Zakladamy , ze na tym etapie "wartosc szukany" i "zamiennik" poprawna, fizycznie jest zdefiniowany.
     * Ignorowany jest klucz "nieustawiony" poniewaz tych moze byc wiecej na liscie
     * @param szukany klucz na liscie
     * @param zamiennik podmieniany klucz na liscie
     * @return 1 - operacja zakonczona sukcesem, -1 -nie znaleziono szukanego , -2 zamiennik jest juz na liscie
     */
    public int zamienPerkAktywny(String szukany , String zamiennik){
        int indexSzukany = czyPerkJestWaktywnych(szukany);
        int indxZamiennik = czyPerkJestWaktywnych(zamiennik);
        if (indexSzukany == -1) return -1; //szukany nie wystepuje na liscie
        if (zamiennik.equalsIgnoreCase(PERK_NIEUSTAWIONY)){//zignoruj "nieustawiony moze byc 0...n
            aktywnePerki.set(indexSzukany,zamiennik);
            return 1;
        }else{
            if (indxZamiennik >= 0) return -2; //zamiennik jest juz na liscie
            aktywnePerki.set(indexSzukany,zamiennik);
            return 1;
        }
    }
}
