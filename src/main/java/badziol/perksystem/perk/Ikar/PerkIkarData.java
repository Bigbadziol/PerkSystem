package badziol.perksystem.perk.Ikar;
// dla testów zmieniono: blokadę z 120 -> 40
// glodTick z 3 -> 6

public class PerkIkarData {
    public final int lotBaza = 15; //bazowa wartość czasu lotu w sekundach
    public final int lotModyfikator = 10; // z tego zakresu zostanie doliczona wartość do ostatecznego czasu lotu w sekundach
    public final int blokada = 40; //raz uruchomiona opcja lotu nie może byc użyta przez ten czas (sekundy)
    public final int zdrowieMin = 5; //Minimalny stan zdrowia, by moc rozpocząć lot (domyślnie 10 int)
    public final boolean zdrowieUwzglednij = true; //czy uwzględnić minimalny stan zdrowia potrzebny do lotu
    public final double glodMin = 10; //Minimalny stan głodu, by rozpocząć lot (domyślne 20 double)
    public final boolean glodUwzglednij = true;
    public final int startLotZabierzZdrowie = 2; //rozpoczęcie lotu kosztuje cię tyle zdrowia
    public final int startLotZabierzGlod = 2;// rozpoczęcie lotu spowoduje zwiększenie głodu o tę wartość
    public  long lotCzas = 0L; //całkowity czas lotu: lotBaza + lotModyfikator
    public long lotRozpoczecie = 0L; //czas ms od ostatniego uruchomienia komendy
    public boolean wyswietlonoOstrzezenie = false; //czy wyświetlono ostrzeżenie o przekroczeniu bezpiecznego czasu lotu

    public final int glodTick = 6; //co ile sekund lotu gracz glodnieje
    public long glodTickCzas = 0L; // ostatni czas w ms, kiedy gracz zgłodniał podczas lotu
    public boolean aktywny = false; // czy w danej chwili perk działa na rzecz konkretnego gracza

    public FazaLotu fazaLotu = FazaLotu.BEZPIECZNA; // nie ruszać! wartość tej zmiennej ustawia zadanieIkar
    public boolean maSkrzydla = false; //nie ruszać! chodzi o proces animacji skrzydeł w danym momencie

}
