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

import java.util.UUID;

public class WilkData {
    private final Wolf wilk; //realny minecraftowy wilk
    private final Player wlasciciel; //by mieć dostęp do pewnych danych na skróty

    private final int czasRespawn = 30; //Po śmierci psa ten się odrodzi po tym czasie automatycznie. Czas w sekundach.
    private final int hpBazowe = 25; // maxymalne hp naszego wilka, domyślna: 20
    public final double poziomRan =0.5d; // 0.5 , oznacza,że poniżej 50% maksymalnego życia wilk jest bardziej
    // niebezpieczny i zyskuje możliwość zadawania krytycznych obrażeń
    public final double critSzansa = 0.3d; //0.3 oznacza 30% szansę zadania krytycznego ciosu
    private final double atakBaza = 2; //na tej podstawie liczone są wszystkie obrażenia, domyśla : 2.0
    private final double knockbackRes = 1.0d; //odrzut na cios , domyślna : 0.0
    private final double szybkosc = 0.75d; // szybkosc poruszania sie naszego przyzwanca, domyślna 0.7

    public long czasSmierci = 0l; //zapamiętany czas ostatniej śmierci wilka
    public boolean zyje = true; // aktualny stan
    public boolean pierwszePrzyzwanie = true; //???? chodzi o glitcha z odnawianiem hp
    public double hpOstatnie = hpBazowe; // wstępna inicjalizacja.


    public UUID wilkUniqueId(){
        return wilk.getUniqueId();
    }

    public UUID wlascicielUniqueId(){
        return wilk.getOwner().getUniqueId();
    }

    public WilkData(Player wlascicielWilka){
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

    }

    public void odwolaj(){
        hpOstatnie = wilk.getHealth();
        wilk.remove();
        System.out.println("[Wilk] - nastapilo odwolanie wilka");
    }

    public void atakuj(Player player){
        System.out.println("[Wilk] - wlasciciel:"+wilk.getOwner().getName()+" --> "+player.getName());
        wilk.attack(player);
    }

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
