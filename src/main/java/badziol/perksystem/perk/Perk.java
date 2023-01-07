package badziol.perksystem.perk;

import badziol.perksystem.PerkSystem;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

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

    protected long efektStartCzas = 0;
    protected long efektStopCzas = 0;
    public boolean efektWidoczny = false; //efekt w postaci czastek ma byc nalozony
    public BukkitRunnable efekt;  //?? zdefiniowane w kazdym perku?


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

    @SuppressWarnings("deprecation")
    public ItemStack ustawTexture(ItemStack glowka, String value) {
        UUID id = UUID.nameUUIDFromBytes(value.getBytes());
        int less = (int) id.getLeastSignificantBits();
        int most = (int) id.getMostSignificantBits();
        return Bukkit.getUnsafe().modifyItemStack(glowka,"{SkullOwner:{Id:[I;" + (less * most) + "," + (less >> 23) + "," + (most / less) + "," + (most * 8731) + "],Properties:{textures:[{Value:\"" + value + "\"}]}}}");
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
        //glowka =ustawTexture(glowka,textura);
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

    public void uruchomEfekt(long czas){
        if (efekt != null){
            if (czas==0){
                efekt.runTaskTimer(plugin,0L,10L);
            }else{

            }
        }
    }
    public void zatrzymajEfekt(){
        if (efekt != null) efekt.cancel();
    }

    public void aktywuj(Player gracz){
        System.out.println("[AKTYWACJA]: "+nazwaId+" [template].");
    }

    public void dezaktywuj(Player gracz){
        System.out.println("[DEAKTYWACJA]: "+nazwaId+" [template].");
    }

}