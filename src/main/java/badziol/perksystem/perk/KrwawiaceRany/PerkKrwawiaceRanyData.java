package badziol.perksystem.perk.KrwawiaceRany;

public class PerkKrwawiaceRanyData {
    /** STALA : czas trwania efektu wykrwawiania, czas wyrazony w sekundach*/
    public final int czasTrwania = 20;
    /** STALA : Ilosc obrazen zadana w okresalonym okresie czasu */
    public final int iloscTickow = 10;
    /** STALA :Ilość procetowa z bazowych obrazen - 0.3 doda 30% od krytycznego ciosu */
    public final double procetObrazen = 0.3D;

    /** Auktualny numer aktualnie nadanego obrazenia  */
    public int auktualnyTick = 0;
    /** Moment kiedy wystrtował perk na graczu */
    public long czasWystartowaniaPerka = 0l;
    /** Na podstawie tej wartosci beda obliczane wszystkie zmienne pomocnicze */
    public double bazoweObrazenie = 0D;
    /** WYLICZONE : ile obrazen ma zadac pojedynczy tick obrazen */
    public double tickObrazenie = 0D;
    /** WYLICZONE : przerwa czasowa pomiedzy tickami obrazen */
    public long tickCzas = 0L;

    /** tu zapamietany jest czas kiedy zadzialal ostatni tick obrazen */
    public long ostatniTickCzas = 0L;

    public PerkKrwawiaceRanyData(double cios, long czasSystemowy){
        czasWystartowaniaPerka = czasSystemowy;
        ostatniTickCzas = czasSystemowy;

        bazoweObrazenie = cios;
        tickObrazenie = (cios * procetObrazen) / iloscTickow;
        tickCzas = (czasTrwania * 1000) / iloscTickow;
    }
}
