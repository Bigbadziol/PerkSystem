package badziol.perksystem.perk.Ikar.Skrzydla;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/*
    W tej klasie Filip nic nie zmieniaj, przechowuje ona dane pojedynczej czastki składającej się na
    skrzydlo.
 */
public class SkrzydloCzastka {
    public String symbol;
    public Particle czastka;
    public Color kolor;
    public double szybkosc;
    //zmienne defakto prywatne odpowiadające za przechowywanie lokalizacji cząstki w konkretym momencie animacji.
    public double dystans = 0;
    public double wysokosc = 0;
    public int kat = 0;

    /**
     * Konstruktor
     * @param symbol - "symbol cząstki" - dzięki któremu możesz namalować skrzydło
     * @param czastka - minecraftowy typ cząstki
     * @param kolor - brany pod uwagę tylko dla cząstek: SPELL_MOB, SPELL_MOB_AMBIENT
     * @param szybkosc - brany pod uwagę dla wszystkich czątek z wykluczeniem: SPELL_MOB, SPELL_MOB_AMBIENT
     */
    public SkrzydloCzastka(String symbol, Particle czastka, Color kolor, double szybkosc) {
        this.symbol = symbol;
        this.czastka = czastka;
        this.kolor = kolor;
        this.szybkosc = szybkosc;
    }

    /**
     *  Stwórz pojedyńczą cząstkę wraz z jej lustrzanym odbiciem
     * @param loc - lokalizacja cząstki w przestrzeni
     * @param player - gracz dla którego, ma być utworzona cząstka
     * @param skrzydloStrona - strona skrzydła
     */
    public void rysujCzastke(Location loc, Player player , SkrzydloStrona skrzydloStrona){
        double kierunek = loc.getYaw();
        double x, y, z, extra;

        if (skrzydloStrona == SkrzydloStrona.LEWA) kierunek = (kierunek + kat);
        if (skrzydloStrona == SkrzydloStrona.PRAWA) kierunek = (kierunek - kat);

        if (czastka == Particle.SPELL_MOB || czastka == Particle.SPELL_MOB_AMBIENT) {
            x = kolor.getRed() / 255D;
            y = kolor.getGreen() / 255D;
            z = kolor.getBlue() / 255D;
            extra = 1;
        } else {
            kierunek = Math.toRadians(kierunek);
            x = dystans * Math.cos(kierunek);
            y = wysokosc;
            z = dystans * Math.sin(kierunek);
            extra = szybkosc;
        }
        player.getWorld().spawnParticle(czastka, loc, 0, x, y, z, extra, null);
    }
}

