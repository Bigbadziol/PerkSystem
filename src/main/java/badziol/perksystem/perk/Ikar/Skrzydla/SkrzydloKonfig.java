package badziol.perksystem.perk.Ikar.Skrzydla;

import org.bukkit.Color;
import org.bukkit.Particle;
import java.util.ArrayList;
import java.util.HashMap;

public class SkrzydloKonfig {
    public double poczatekPion = -0.1;          // offset początkowy (pion) do rysowania skrzydeł
    public double poczatekPoziom = 0;           // offset początkowy (poziom) do rysowania skrzydeł
    public double dystansDoGracza = 0.3;        // dystans od gracza
    public double dystansMiedzyCzastkami = 0.1; // ...
    public int zadanieCzas = 1;                 // ticki dla BuckkitRunnable
    public boolean animacjaSkrzydel = true;     // ...
    public int animacjaSzybkosc = 4;            // Szybkość w zakresie 1..10, powiązane z zadanieCzas
    public int katPoczatek = 30;                // Kąt początkowy (dolny) rozwarcia skrzydeł
    public int katKoniec = 70;                  // Kąt ostateczny (górny) rozwarcia skrzydeł

    private final ArrayList<SkrzydloCzastka> listaCzastek = new ArrayList<>(); // lista dostepnych wzorów cząstek

    // klucz - [dystans,wysokosc](wartość relatywna) , dane- pelne dane cząstki
    // klucz wyliczany na podstawie zadeklarowanych offsetów (poczatekPion/poczatekPoziom) oraz dystansów
    public HashMap<double[], SkrzydloCzastka> czastkiBezpiecznyLot;
    public HashMap<double[], SkrzydloCzastka> czastkiNiebezpiecznyLot;

    public SkrzydloKonfig() {
        //Przeczytaj opis konstruktora
        listaCzastek.add(new SkrzydloCzastka("x", Particle.CRIT_MAGIC, Color.fromRGB(0,0,0) ,0));
        listaCzastek.add(new SkrzydloCzastka("o", Particle.CRIT, Color.fromRGB(0,0,0), 0));
        listaCzastek.add(new SkrzydloCzastka("h", Particle.CAMPFIRE_COSY_SMOKE, Color.fromRGB(0,0,0), 0));
        // rysujemy prawe skrzydło
        // wzor dla bezpiecznej fazy lotu
        ArrayList<String> skrzydloWzorBezpiecznyLot = new ArrayList<>();
        skrzydloWzorBezpiecznyLot.add("-,-,-,-,x,x,x,-,-,-");
        skrzydloWzorBezpiecznyLot.add("-,-,-,x,x,x,x,x,-,-");
        skrzydloWzorBezpiecznyLot.add("-,-,x,x,x,x,x,x,x,-");
        skrzydloWzorBezpiecznyLot.add("-,x,x,x,x,x,x,x,x,-");
        skrzydloWzorBezpiecznyLot.add("x,x,x,x,x,x,x,x,x,x");
        skrzydloWzorBezpiecznyLot.add("x,x,x,x,x,x,x,x,x,x");
        skrzydloWzorBezpiecznyLot.add("x,x,x,x,x,x,x,x,x,x");
        skrzydloWzorBezpiecznyLot.add("x,x,x,x,x,x,x,x,x,x");
        skrzydloWzorBezpiecznyLot.add("-,-,x,x,x,x,x,x,x,x");
        skrzydloWzorBezpiecznyLot.add("-,-,-,x,x,x,x,x,x,x");
        skrzydloWzorBezpiecznyLot.add("-,-,-,x,x,x,x,x,x,x");
        skrzydloWzorBezpiecznyLot.add("-,-,-,-,x,x,x,x,x,x");
        skrzydloWzorBezpiecznyLot.add("-,-,-,-,x,x,x,x,x,x");
        skrzydloWzorBezpiecznyLot.add("-,-,-,-,-,x,x,x,x,-");
        skrzydloWzorBezpiecznyLot.add("-,-,-,-,-,x,x,x,x,-");
        skrzydloWzorBezpiecznyLot.add("-,-,-,-,-,-,x,x,x,-");
        skrzydloWzorBezpiecznyLot.add("-,-,-,-,-,-,o,x,x,-");
        skrzydloWzorBezpiecznyLot.add("-,-,-,-,-,-,-,x,x,-");
        skrzydloWzorBezpiecznyLot.add("-,-,-,-,-,-,-,-,o,-");
        czastkiBezpiecznyLot = parsujWzorSkrzydla(skrzydloWzorBezpiecznyLot);
        //Wzór dla niebezpiecznej fazy lotu
        ArrayList<String> skrzydloWzorNiebezpiecznyLot = new ArrayList<>();
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,-,x,x,x,-,-,-");
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,x,x,x,x,x,-,-");
        skrzydloWzorNiebezpiecznyLot.add("-,-,x,x,x,x,x,x,x,-");
        skrzydloWzorNiebezpiecznyLot.add("-,x,x,x,x,x,x,x,x,-");
        skrzydloWzorNiebezpiecznyLot.add("x,x,x,x,x,x,x,x,x,x");
        skrzydloWzorNiebezpiecznyLot.add("x,x,x,x,x,x,x,x,x,x");
        skrzydloWzorNiebezpiecznyLot.add("x,x,x,x,x,x,x,x,x,x");
        skrzydloWzorNiebezpiecznyLot.add("-,x,x,x,x,x,x,x,x,x");
        skrzydloWzorNiebezpiecznyLot.add("-,-,o,x,x,x,x,x,x,x");
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,o,x,x,x,x,x,o");
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,o,x,x,x,x,x,x");
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,-,o,x,x,x,x,o");
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,-,o,x,x,x,o,x");
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,-,-,o,x,x,o,-");
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,-,-,o,x,h,x,-");
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,-,-,-,o,x,x,-");
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,-,-,-,o,x,x,-");
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,-,-,-,-,o,x,-");
        skrzydloWzorNiebezpiecznyLot.add("-,-,-,-,-,-,-,-,x,-");
        czastkiNiebezpiecznyLot = parsujWzorSkrzydla(skrzydloWzorNiebezpiecznyLot);
    }

    /**
     *  Pobierz pełne dane cząstki na podstawie symbolu
     * @param symbol czątki
     * @return odnaleziona cząstka lub null, jeśli jej brak
     */
    public SkrzydloCzastka pobierzSymbolCzastki(String symbol) {
        for (SkrzydloCzastka skrzydloCzastka : listaCzastek) {
            if (skrzydloCzastka.symbol.equals(symbol)) {
                return skrzydloCzastka;
            }
        }
        return null;
    }

    /**
     * Zamień narysowany wzór na mapę cząstek
     * @param wzor wskazany wzor skrzydła
     * @return mapa cząstek
     */
    private HashMap<double[], SkrzydloCzastka> parsujWzorSkrzydla(ArrayList<String> wzor) {
        HashMap<double[], SkrzydloCzastka> czastkiWszystkie = new HashMap<>();
        double dystans;
        double wysokosc = poczatekPion + (wzor.size() * dystansMiedzyCzastkami); // Highest vertical point of the wing

        for (String liniaCzastek : wzor) {
            wysokosc = wysokosc - dystansMiedzyCzastkami;
            dystans = dystansDoGracza;
            String[] liniaCzastekDane = liniaCzastek.split(",");

            for (String czastkaID : liniaCzastekDane) {
                if (czastkaID.equals("-")) { // '-' - traktuj jako puste pole
                    dystans = dystans + dystansMiedzyCzastkami;
                    continue;
                }
                SkrzydloCzastka czastkaDane = pobierzSymbolCzastki(czastkaID);
                if (czastkaDane == null){
                    dystans = dystans + dystansMiedzyCzastkami;
                    continue;
                }
                //dodaj cząstkę do listy
                double[] wspolzedne = new double[2];
                wspolzedne[0] = dystans;
                wspolzedne[1] = wysokosc;
                czastkiWszystkie.put(wspolzedne, czastkaDane);
                dystans = dystans + dystansMiedzyCzastkami;
            }
        }
        return czastkiWszystkie;
    }
}

