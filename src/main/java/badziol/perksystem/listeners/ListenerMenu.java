package badziol.perksystem.listeners;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import badziol.perksystem.perk.PosiadaczPerkow;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.IOException;

import static badziol.perksystem.perk.PerkStale.*;

public class ListenerMenu implements Listener {
    private final PerkSystem plugin;

    public ListenerMenu(PerkSystem plugin){
        // Jak mnie wkurza to zawijanie do pojedynczej linii
        this.plugin = plugin;
    }

    /**
     * Obsługa menu głównego. Możliwość przeczytania instrukcji na temat perków
     * Wybór perka, który ma być zamieniony.
     * @param event -glowny event InventoryClickEvent
     */
    public void obslugaMenuGlowne(InventoryClickEvent event){
        //wykluczenie kliknięcia poza inwentarz
        if (event.getClickedInventory() == null){
            event.setCancelled(true);
            return;
        };
        InventoryType invType =  event.getClickedInventory().getType(); //CHEST  to górne okno , PLAYER dolne okno
        if (invType == InventoryType.PLAYER) event.setCancelled(true);


        ItemStack przedmiot = event.getCurrentItem();
        if (przedmiot == null) return;//Obsługa kliknięcia "pustego" pola.

        if (przedmiot.getType() == Material.PLAYER_HEAD){
            plugin.gui.menuWyborPerka((Player) event.getWhoClicked(),przedmiot);
        }else if( przedmiot.getType() == Material.WRITTEN_BOOK){
            //oszustwo instrukcja-część 2, część 1 - w PerkGui
            //teraz faktycznie tworzymy przedmiot (książkę) , który otworzymy graczowi.
            ItemStack ksiazka = new ItemStack(Material.WRITTEN_BOOK, 1);
            BookMeta ksiazkaMeta = (BookMeta) ksiazka.getItemMeta();
            ksiazkaMeta.setTitle(INSTRUKCJA_TYTUL);
            ksiazkaMeta.setAuthor(INSTRUKCJA_AUTOR);
            ksiazkaMeta.setPages(INSTRUKCJA_TRESC);
            ksiazka.setItemMeta(ksiazkaMeta);
            Player p = (Player) event.getWhoClicked();
            p.openBook(ksiazka);
        }else{
            //System.out.println("Nie obsługiwany przedmiot z menu.");
        }
        event.setCancelled(true);// to sprawia, że nie można przenosić przedmiotu
    }

    /**
     * Zamiana wskazanego perka pozostałe z listy.
     * @param event to InventoryClickEvent
     */
    public void obslugaMenuPerk(InventoryClickEvent event){
        //wykluczenie kliknięcia poza inwentarz
        if (event.getClickedInventory() == null){
            event.setCancelled(true);
            return;
        };
        //wyklucz kliknięcie w inwentarz gracza
        InventoryType invType =  event.getClickedInventory().getType(); //CHEST górne okno , PLAYER dolne okno
        if (invType == InventoryType.PLAYER){
            event.setCancelled(true);
            return;
        }

        ItemStack glowka = event.getCurrentItem();
        if (glowka == null) return;
        if (glowka.getType() != Material.PLAYER_HEAD){
            event.setCancelled(true);
            return;
        }
        //Pozycja tego elementu jest sciśle określona !!!!
        ItemStack zmienianaGlowka = event.getView().getTopInventory().getItem(POZYCJA_ZAMIENIANEJ_GLOWKI);
        // W teorii zamieniana główka nigdy nie powinna być nullem, ale nulli jak ojca nigdy nie mozesz byc pewny.
        if (zmienianaGlowka == null) return;

        ItemStack wybranaGlowka = event.getCurrentItem();
        String wybranaId = NBT.get(wybranaGlowka, nbt -> nbt.getString("nazwaId")); //klucz zaszyty w przedmiocie
        String zmienianaId = NBT.get(zmienianaGlowka, nbt -> nbt.getString("nazwaId"));
        String imie = event.getWhoClicked().getName();
        System.out.println("[LM](zamiana) Dokonano wyboru perka przez : "+imie+
                " , zmieniana : "+zmienianaId+" na "+wybranaId);
        PosiadaczPerkow tenPosiadacz = plugin.perkLista.pobierzPosiadacza(imie);
        if (tenPosiadacz == null){
            System.out.println("[LM](zamiana)  BLAD - nie udalo sie pobrac posiadacza perka o imieniu :"+
                    imie+" BLAD KRYTYCZNY");
            return;
        };
        int result = tenPosiadacz.zamienPerkAktywny(zmienianaId,wybranaId);
        switch (result){
            case 1 -> {
                Perk perkStary = plugin.perkLista.wezPerk(zmienianaId);
                perkStary.dezaktywuj((Player) event.getWhoClicked());
                Perk perkNowy = plugin.perkLista.wezPerk(wybranaId);
                perkNowy.aktywuj((Player) event.getWhoClicked());

                System.out.println("[LM](zamiana) - Zamiana zakonczona sukcesem.");
                try {
                    plugin.perkLista.zapiszPlik();
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("[LM](zamiana) - BLAD - zapisu do pliku");
                }
            }
            case -1-> System.out.println("[LM](zamiana) BLAD - Nie znaleziono perka bazowego");
            case -2-> System.out.println("[LM](zamiana) BLAD - Perk zamieniany juz wystepuje na liscie.");
        };
        event.setCancelled(true);
        plugin.gui.menuGlowne((Player) event.getWhoClicked());

    }

    /**
     * Rozpoznajemy po tytule okna, które kliknięto i przekazujemy event do odpowiedniej funkcji
     * @param event
     */
    @EventHandler
    public void  onMenuClick(InventoryClickEvent event){
        //Glowne menu z aktywnymi perkami
        if(event.getView().getTitle().equalsIgnoreCase(plugin.gui.menuGlowneNaglowek)){
            obslugaMenuGlowne(event);
        }
        //Wybor perka
        else if (event.getView().getTitle().equalsIgnoreCase(plugin.gui.menuWyborPerkaNaglowek)) {
            obslugaMenuPerk(event);
        }
    }

/*
    //Na tym etapie ten handler jest juz zbedny , niech narazie zostanie
    @EventHandler
    public void onMenuClose(InventoryCloseEvent event){
        if(event.getView().getTitle().equalsIgnoreCase(plugin.gui.menuGlowneNaglowek)) {
            System.out.println("[LM](close) - zamknieto menu glowne");
        }else if(event.getView().getTitle().equalsIgnoreCase(plugin.gui.menuWyborPerkaNaglowek)) {
            System.out.println("[LM](close) - zamknieto wybor perka");
        }
    }
 */

}
