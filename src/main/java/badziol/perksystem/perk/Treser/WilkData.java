package badziol.perksystem.perk.Treser;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import java.util.Objects;

import static java.lang.Math.round;

public class WilkData {
    private  Wolf wilk; //realny minecraftowy wilk ukryty w naszym obiekcie
    private  Player wlasciciel; //by mieć dostęp do pewnych danych na skróty

    public final int czasRespawn = 30; //Po śmierci psa ten się odrodzi po tym czasie automatycznie. Czas w sekundach.
    public final int czasRegeneracja = 10; //Po jakim czasie wilk odzyska 1 punkt hp. Czas w sekundach.
    private final int hpBazowe = 25; // maxymalne hp naszego wilka, domyślna: 20
    public final double poziomRan =0.5d; // 0.5 , oznacza,że poniżej 50% maksymalnego życia wilk jest bardziej
    // niebezpieczny i zyskuje możliwość zadawania krytycznych obrażeń
    public final double critSzansa = 0.3d; //0.3 oznacza 30% szansę zadania krytycznego ciosu
    private final double atakBaza = 2; //na tej podstawie liczone są wszystkie obrażenia, domyśla : 2.0
    private final double knockbackRes = 1.0d; //odrzut na cios , domyślna : 0.0
    private final double szybkosc = 0.75d; // szybkosc poruszania sie naszego przyzwanca, domyślna 0.7

    public long czasSmierci = 0L; //zapamiętany czas ostatniej śmierci wilka
    public boolean zyje = true; // aktualny stan
    public boolean pierwszePrzyzwanie = true; //???? chodzi o glitcha z odnawianiem hp
    public double hpOstatnie = hpBazowe; // wstępna inicjalizacja.

    public long czasOstatniaRegeneracja = 0;// Czas ostatniego przyrostu punktu hp;


    /**
     * Metoda tworzy obiekt (minecraftowe entity) wilka nadając mu zdefiniowane cechy.
     * @param wlascicielWilka - gracz, któremu ma zostać przypisany wilk bojowy
     * @return false- kiedy wilk istnieje (nie jest nullem). <br>
     * true - nie napodkano  problemów.
     */
    private boolean utworzWilka(Player wlascicielWilka){
        if (wilk != null){
            System.out.println("[Wilk](utworz) - ten wilk juz istnieje.");
            return false;
        }
        this.wlasciciel = wlascicielWilka;
        Location loc = wlasciciel.getLocation();
        wilk = (Wolf) wlasciciel.getWorld().spawnEntity(loc,EntityType.WOLF,false);
        wilk.setAngry(false);
        wilk.setTamed(true);
        wilk.setOwner(wlasciciel);
        wilk.setAdult();
        wilk.setCustomName(ChatColor.DARK_AQUA+wlasciciel.getName()+" "+ChatColor.AQUA+"obronca");
        wilk.setCustomNameVisible(true);
        wilk.setGlowing(true);
        wilk.setCollarColor(DyeColor.LIME);
        wilk.setBreed(false); //nie może się rozmnażać

        //Nowe atrybuty wilka
        //https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html
        Attributable wilkAtrybuty = wilk;
        AttributeInstance wilkMaxHp = wilkAtrybuty.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (wilkMaxHp!= null) {
            wilkMaxHp.setBaseValue(hpBazowe);
            if (pierwszePrzyzwanie) {
                wilk.setHealth(wilkMaxHp.getValue());
            }else{
                wilk.setHealth(hpOstatnie);
            }
        }

        AttributeInstance wilkDmg = wilkAtrybuty.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (wilkDmg != null) wilkDmg.setBaseValue(atakBaza);

        AttributeInstance wilkSzybkosc = wilkAtrybuty.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (wilkSzybkosc!= null) wilkSzybkosc.setBaseValue(szybkosc);

        AttributeInstance wilkKnockRes = wilkAtrybuty.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        if (wilkKnockRes != null) wilkKnockRes.setBaseValue(knockbackRes);

        pierwszePrzyzwanie = false;
        czasSmierci = 0L;
        czasOstatniaRegeneracja = System.currentTimeMillis();

        System.out.println("[Wilk](utworz)- utworzono nowego wilka.");
        return true;
    }
    public WilkData(Player wlascicielWilka){
        utworzWilka(wlascicielWilka);
    }

    public void odwolaj(){
        if (wilk != null) {
            hpOstatnie = wilk.getHealth();
            wilk.setHealth(0);
            //wilk.remove();
            wilk = null;
            System.out.println("[Wilk] - nastapilo odwolanie wilka");
        }
    }

    public void atakuj(Player player){
        System.out.println("[Wilk] - wlasciciel:"+
                Objects.requireNonNull(wilk.getOwner()).getName()+
                " --> "+player.getName());
        wilk.attack(player);
    }


    /**
     * Metoda "odtwarza" martwego wilka. Metoda dedykowana zadaniu obsługującemu wilki bojowe.<br>
     * Następuje wywołanie metody utworzWilka (metoda prywatna)
     * @return false- nie został wcześniej zdefiniowany właściciel lub wilk aktualnie żyje.<br>
     * true/false- w zależności od wyniku utworzWilka(gracz)
     */
    public boolean respawnWilka(){
        if (wlasciciel == null){
            System.out.println("[Wilk] - brak wskazanego wlasciciela.");
            return false;
        }
        if (zyje) return false;
        if (czasSmierci + (czasRespawn* 1000) < System.currentTimeMillis()) return false;
        else{
            System.out.println("[Wilk] - respawn wilka.");
            czasSmierci = System.currentTimeMillis();
            return utworzWilka(wlasciciel);
        }
    };

    /**
     * Metoda przywraca istniejącemu wilkowi 1 punkt życia co określoną jednostkę czasu.
     * @return false- wilk jako (byt) nie istnieje, nie upłynął minimalny czas potrzebny na regenerację.<br>
     * true- odnowiono punkt życia lub wilk jest pełen sił
     */
    public boolean regenerujHp(){
        if (wilk == null) {
//            System.out.println("[Wilk] - regeneracja , wilk jest nullem!");
            return false;
        }
        //najpierw sprawdzamy czas
        if (System.currentTimeMillis()> czasOstatniaRegeneracja + (czasRegeneracja * 1000)){
            AttributeInstance wilkMaxHp = wilk.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            assert wilkMaxHp != null;
            if (wilk.getHealth()+1 <= wilkMaxHp.getValue()){
                wilk.setHealth(wilk.getHealth()+1);
                System.out.println("[Wilk](regen) - uleczono o 1 : " + infoHp());
            }else{
                wilk.setHealth(wilkMaxHp.getValue());
                System.out.println("[Wilk](regen) - w pelni sil  : "+ infoHp());
            }
            czasOstatniaRegeneracja = System.currentTimeMillis()  + (czasRegeneracja * 1000);
            return true;
        }else return false;
    }

    public String infoHp(){
        String tmp = "";
        if (wilk == null){
            tmp +="(infoHp) wilk=null";
            return tmp;
        }
        double hp = wilk.getHealth();
        AttributeInstance maxHp = wilk.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double proc = Math.round(hp/maxHp.getValue() *100);
        tmp = String.format("%.2f",hp);
        tmp +="/";
        tmp += String.format("%.2f",maxHp.getValue());
        tmp +=" ";
        tmp += proc;
        tmp += "%";
        return tmp;
    }
    /**
     * Jak nazwa wskazuje, metoda do wyświetlania informacji pośrednich na temat tej klasy.
     */
    public void debugInfo(){
    /*
        //chcemy sie dowiedziec jakie bazowe wartosci ma wilk interesujacych nas parametrów
        Attributable wilkAtrybuty = wilk;
        System.out.println("------------WILK GENERYCZNE -------");
        AttributeInstance wilkMaxHp = wilkAtrybuty.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        System.out.println("HP :"+wilkMaxHp.getDefaultValue()+" / "+wilk.getHealth());
        AttributeInstance wilkSzybkosc = wilkAtrybuty.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        System.out.println("Szybkosc :"+ wilkSzybkosc.getDefaultValue());
        AttributeInstance wilkKnockRes = wilkAtrybuty.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        System.out.println("Knockback res :"+wilkKnockRes.getDefaultValue());
        AttributeInstance wilkAtak =  wilkAtrybuty.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        System.out.println("Atak :"+wilkAtak.getDefaultValue());
*/
    }


}
