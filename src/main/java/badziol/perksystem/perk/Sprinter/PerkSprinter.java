package badziol.perksystem.perk.Sprinter;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import badziol.perksystem.perk.PerkStale;
import org.bukkit.entity.Player;

//5) zwirkszona predkosc biegania o 15%
public class PerkSprinter extends Perk {
    public  PerkSprinter(PerkSystem plugin){
        super(plugin);
        nazwaId="sprinter";
        wyswietlanie="Sprinter!";
        opis.add("Jestes szybszy niz...");
        opis.add("emeryt na promocji karkowki.");
        textura = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3J"+
                "hZnQubmV0L3RleHR1cmUvMjIwYzJiNzkxYzJmYmY0NTFhYmVlZjIzNTI1ZjMxM2FmMT"+
                "QzN2QyZmNhYjY5YzMyNzM0ZDM3OWI2NTZhZjE1ZiJ9fX0=";
        inicjujGlowke();
    }
/*
    public void run(){
        PotionEffect potion = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false);
    }

 */

    @Override
    public void aktywuj(Player gracz) {
        System.out.println("[AKTYWACJA](Sprinter): "+nazwaId+" - szybciej chodze.");
        gracz.setWalkSpeed(PerkStale.PREDKOSC_CHODZENIA_SPRINTER);
    }

    @Override
    public  void dezaktywuj(Player gracz){
        System.out.println("[DEAKTYWACJA](Sprinter): "+nazwaId+" - normalnie chodze.");
        gracz.setWalkSpeed(PerkStale.PREDKOSC_CHODZENIA_DOMYSLNA);
    }
}
