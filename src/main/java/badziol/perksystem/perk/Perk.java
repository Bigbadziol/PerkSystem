package badziol.perksystem.perk;

import badziol.perksystem.PerkSystem;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

//Na podstawie strony wskazanej przez Filipa - textury
// https://minecraft-heads.com/custom-heads

public abstract class Perk {
    protected PerkSystem plugin; // by miec dostep do perkLista
    protected ItemStack glowka; // obiekt reprezentujacy perka
    public String nazwaId="";
    public String wyswietlanie="";
    public ArrayList<String> opis = new ArrayList<>();
    public long czasAktywnosci = 0; //bo domyslnie pasywny
    public String textura="";
    public boolean efektWidoczny = false; //efekt w postaci czastek ma byc nalozony

    public Perk(PerkSystem plugin){
        this.plugin = plugin;
    }

    /**
     * Utworz reprezentacje graficzna w postaci glowki gracza
     * Ustaw opis glowki oraz dyswietlane informacje
     */
    public void inicjujGlowke(){
        glowka = new ItemStack(Material.PLAYER_HEAD,1);
        ItemMeta perkMeta = glowka.getItemMeta();
        if (perkMeta != null) {
            perkMeta.setDisplayName(wyswietlanie);
            perkMeta.setLore(opis);
        }
        glowka.setItemMeta(perkMeta);
        System.out.println("[perk.temlate] - init :"+wyswietlanie);
    }

    /**
     * Fizyczna reprezentacja perka jest glowka gracza klasy Itemstack
     * Dodatkowo metoda "zaszywa" wewnatrz przedmiotu dodatkowe informacje , takie jak :
     * (String)nazwaId-unikalna nazwa perka,
     * (Long) czasAktywności
     * Sensowniej to opisac, lub zmienic nazwe metody......
     * @return Itemstack glowka - reprezentujacy perka
     */
    public ItemStack wezGlowke(){
        //dodaj wlasne ukryte dane
        NBT.modify(glowka, nbt -> {
            nbt.setString("nazwaId", nazwaId);
            nbt.setLong("czasAktywnosci",czasAktywnosci);
            //nbt.setDouble("Doubletest", 1.5d);
            //nbt.setBoolean("pasywny", true);
        });

        //zmodyfikuj texture glowki
        //ciekawe czy to podejscie powoduje - mieszanie sie graficzne glowej ???
        SkullMeta meta = (SkullMeta) glowka.getItemMeta();
        assert meta != null;

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", textura));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        glowka.setItemMeta(meta);
        //zwroc obiekt
        return glowka;
    }


    public void aktywuj(Player gracz){
        System.out.println("[AKTYWACJA]: "+nazwaId+" [template].");
    }

    public void dezaktywuj(Player gracz){
        System.out.println("[DEAKTYWACJA]: "+nazwaId+" [template].");
    }

}