package badziol.perksystem;

import badziol.perksystem.perk.PosiadaczPerkow;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static badziol.perksystem.perk.PerkStale.POZYCJA_ZAMIENIANEJ_GLOWKI;

public class PerkGui {
    private final PerkSystem plugin;
    public final String menuGlowneNaglowek = ChatColor.BLUE+""+ChatColor.BOLD+"Aktywne perki";
    public final String menuWyborPerkaNaglowek = ChatColor.BLUE+""+ChatColor.BOLD+"Wybierz perka";

    public PerkGui(PerkSystem plugin){
        this.plugin = plugin;    // referencja
    }

    public void menuLimit1(Inventory inv, String imie){
        inv.clear();
        ArrayList<ItemStack> glowkiAktywne = plugin.perkLista.pobierzAktywneGlowki(imie);
        inv.setItem(8+5, glowkiAktywne.get(0));
    }

    public void menuLimit2(Inventory inv,String imie){
        inv.clear();
        ArrayList<ItemStack> glowkiAktywne = plugin.perkLista.pobierzAktywneGlowki(imie);
        inv.setItem(8+4, glowkiAktywne.get(0));
        inv.setItem(8+6, glowkiAktywne.get(1));
    }

    public void menuLimit3(Inventory inv,String imie){
        inv.clear();
        ArrayList<ItemStack> glowkiAktywne = plugin.perkLista.pobierzAktywneGlowki(imie);
        inv.setItem(8+3, glowkiAktywne.get(0));
        inv.setItem(8+5, glowkiAktywne.get(1));
        inv.setItem(8+7, glowkiAktywne.get(2));
    }

    public void menuLimit4(Inventory inv,String imie){
        inv.clear();
        ArrayList<ItemStack> glowkiAktywne = plugin.perkLista.pobierzAktywneGlowki(imie);
        inv.setItem(8+2, glowkiAktywne.get(0));
        inv.setItem(8+4, glowkiAktywne.get(1));
        inv.setItem(8+6, glowkiAktywne.get(2));
        inv.setItem(8+8, glowkiAktywne.get(3));
    }

    public void menuLimit5(Inventory inv, String imie){
        inv.clear();
        ArrayList<ItemStack> glowkiAktywne = plugin.perkLista.pobierzAktywneGlowki(imie);
        inv.setItem(8+1, glowkiAktywne.get(0));
        inv.setItem(8+3, glowkiAktywne.get(1));
        inv.setItem(8+5, glowkiAktywne.get(2));
        inv.setItem(8+7, glowkiAktywne.get(3));
        inv.setItem(8+9, glowkiAktywne.get(4));
    }

    /**
     * Zbuduj menu glowne , wyswietl je graczowi
     * @param player -gracz
     */
    public void menuGlowne(Player player){
        System.out.println("------------------------");
        System.out.println("[GUI](mg) -  Menu glowne");
        Inventory inventory = Bukkit.createInventory(player, 27, menuGlowneNaglowek);
        String imieGracza = player.getName();
        PosiadaczPerkow gracz = plugin.perkLista.pobierzPosiadacza(imieGracza);
        if (gracz == null){
            System.out.println("[GUI](mg) - gracz nie wystepuje na liscie posiadaczy perkow - BLAD KRYTYCZNY");
            return;
        }
        System.out.println("[GUI](mg) aktywne : "+gracz.aktywnePerki);
        System.out.println("[GUI](mg) zdobyte : "+gracz.zdobytePerki);

        switch (gracz.limit) {
            case 1 -> menuLimit1(inventory,imieGracza);
            case 2 -> menuLimit2(inventory,imieGracza);
            case 3 -> menuLimit3(inventory,imieGracza);
            case 4 -> menuLimit4(inventory,imieGracza);
            case 5 -> menuLimit5(inventory,imieGracza);
            default -> {
                System.out.println("[GUI](mg)-limit z poza zakresu 1...5. BLAD");
                return;
            }
        }
        System.out.println("------------------------");

        //Otwarcie inwentarza
        player.openInventory(inventory);
    }


    public void menuWyborPerka(Player player, ItemStack zmienianaGlowka) {
        System.out.println("------------------------");
        System.out.println("[GUI](wp) - Wybor perka");
        Inventory inv = Bukkit.createInventory(player, 54, menuWyborPerkaNaglowek);
        inv.clear();
        inv.setItem(POZYCJA_ZAMIENIANEJ_GLOWKI, zmienianaGlowka); //ustaw na środku

        String imieGracza = player.getName();
        PosiadaczPerkow posiadacz = plugin.perkLista.pobierzPosiadacza(imieGracza);
        if (posiadacz == null) {
            System.out.println("[GUI](wp) - player nie wystepuje na liscie posiadaczy perkow - BLAD KRYTYCZNY");
            return;
        }

        ItemStack linia = new ItemStack(Material.COAL_BLOCK);
        ItemMeta liniaMeta = linia.getItemMeta();
        if (liniaMeta != null){
            liniaMeta.setDisplayName("-------");
        }
        linia.setItemMeta(liniaMeta);

        for (int i=9;i<18;i++){
            inv.setItem(i,linia);
        }
        ArrayList<ItemStack> glowkiWybor = plugin.perkLista.pobierzGlowkiWybor(imieGracza);
        for (int i = 0; i <glowkiWybor.size() ; i++) {
            ItemStack taGlowka = glowkiWybor.get(i);
            inv.setItem(18+i,taGlowka);
        }
        player.openInventory(inv);
    }


}
