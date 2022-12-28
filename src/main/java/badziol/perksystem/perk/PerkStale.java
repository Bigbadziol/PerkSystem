package badziol.perksystem.perk;

public class PerkStale {
    //zmiana tego to obowiazkowe usuniecie pliku NAZWA_PLIKU(/perki.json) lub
    //reczna podmiana starej nazwy zastrzezonej dla nieustawiony na nowa
    //jest to bardzo istotny klucz jak nazwaId w perku
    public static final String PERK_NIEUSTAWIONY ="nieustawiony";
    public static final int LIMIT_AKTYWNYCH_MIN = 1;
    public static final int LIMIT_AKTYWNYCH_MAX = 5;
    public static final String NAZWA_PLIKU = "/perki.json";
    //nazwy perkow

    /*
          Objaśnienie : Wszystkie reprezentacje perka to przedmiot tego typu czyli PLAYER_HEAD
          Rodzi sie problem z rozroznianiem perkow. Rozwiazaniem jest scisla lokalizacja
          perka. MenuWyborPerka ustawia ja wlasnie w tej lokalizacji.
          Z tej lokalizacji listener odczytuje dane jako perka modyfikowanego
     */
    public static final int POZYCJA_ZAMIENIANEJ_GLOWKI = 4;
    public static final float  PREDKOSC_CHODZENIA_DOMYSLNA = 0.2f;
    public static final float PREDKOSC_CHODZENIA_SPRINTER = 0.3f; //modyfikuje perk Sprinter
    public static final boolean LOT = false; // modyfikuje perk Ikar

    public static final String INSTRUKCJA_AUTOR ="Filipppos";
    public static final String INSTRUKCJA_TYTUL ="Jak zameczyc tate.";
    public static final String[] INSTRUKCJA_TRESC = new String[] {
            "Kazda linia to defakto \n strona.\n Limit to 100(java) 50(badrock i dokumentacja) stron po 798 znakow na " +
                    "strone(internety) lub 256(dokumentacja)\n ",
            "Jako bezbek - na nastepnej stronie suchar od taty :",
            "Na lekcji programowania obiektowego student łapie koleżankę obok za pierś." +
                    " Na to ona: \"To prywatne!!!\", a on odpowiada: \"Przeciez jestesmy w tej samej klasie! \""
    };
}
