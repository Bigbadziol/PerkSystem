package badziol.perksystem.perk.Ikar;

public class PerkIkarData {
    public final int lotBaza = 15; //bazowa wartosc czasu lotu w sekundach
    public final int lotModyfikator = 10; // z tego zakresu zostanie doliczona wartosc do ostatecznego czasu lotu w sekundach
    public final int blokada = 120; //raz uruchomiona opcja lotu nie moze byc urzyta przez ten czas (sekundy)
    public final int zdrowieMin = 5; //Minimany stan zdrowia by moc rozpaczac lot (domyslnie 10 int)
    public final boolean zdrowieUwzglednij = true; //czy uwzglednic minimalny stan zdrowia potrzebny do lotu
    public final double glodMin = 10; //Minimalny stan glodu by rozpaczac lot (domyslne 20 double)
    public final boolean glodUwzglednij = true;
    public final int startLotZabierzZdrowie = 2; //rozpoczecie lotu kosztuje cie tyle zdrowia
    public final int startLotZabierzGlod = 2;// rozpoczecie lotu spowoduje zwiekszenie glodu o
    public  long lotCzas = 0L; //calkowity czas lotu : lotBaza + lotModyfikator
    public long lotRozpoczecie = 0L; //czas ms od ostatniego uruchomienia komendy
    public boolean wyswietlonoOstrzezenie = false; //czy wyswietlono ostrzezenie o przekroczeniu bezpiecznego czasu lotu

    public boolean aktywny = false; // czy w danej chwili perk dziala na rzecz konkretnego gracza

}
