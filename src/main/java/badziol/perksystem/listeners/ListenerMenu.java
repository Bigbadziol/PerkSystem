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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

import static badziol.perksystem.perk.PerkStale.POZYCJA_ZAMIENIANEJ_GLOWKI;

public class ListenerMenu implements Listener {
    private final PerkSystem plugin;

    public ListenerMenu(PerkSystem plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void  onMenuClick(InventoryClickEvent event){

        //Glowne menu z aktywnymi perkami
        if(event.getView().getTitle().equalsIgnoreCase(plugin.gui.menuGlowneNaglowek)){
            InventoryType invType =  event.getClickedInventory().getType(); //CHEST gorne okno , PLAYER dolne okno
            if (invType == InventoryType.PLAYER) event.setCancelled(true); //  bylo true

            //Obsluga klikniecia "pustego" pola i wszystkiego co nie jest glowka
            ItemStack glowka = event.getCurrentItem();
            if (glowka == null) return;
            if (glowka.getType() != Material.PLAYER_HEAD) return;

            plugin.gui.menuWyborPerka((Player) event.getWhoClicked(),glowka);
            event.setCancelled(true);// to sprawia , ze nie mozna przenosic przedmiotu
        }
        //Wybor perka
        else if (event.getView().getTitle().equalsIgnoreCase(plugin.gui.menuWyborPerkaNaglowek)) {
            //wyklucz klikniecie w inwentarz gracza
            InventoryType invType =  event.getClickedInventory().getType(); //CHEST gorne okno , PLAYER dolne okno
            if (invType == InventoryType.PLAYER) event.setCancelled(true);

            ItemStack glowka = event.getCurrentItem();
            if (glowka == null) return;
            if (glowka.getType() != Material.PLAYER_HEAD) return;

            //Pozycja tego elementu jest scisle okreslona !!!!
            ItemStack zmienianaGlowka = event.getView().getTopInventory().getItem(POZYCJA_ZAMIENIANEJ_GLOWKI);
            // W teorii zamieniana glowka nigdy nie powinna byc nullem, ale nulli jak ojca nigdy nie mozesz byc pewny.
            if (zmienianaGlowka == null) return;



            System.out.println("slot hit : "+event.getSlot());
            if ((event.getSlot()>=9) || (event.getSlot()<=17)){
                System.out.println("pasek hit");
            }
            if (event.getSlot()<=17 || event.getCurrentItem() == null ){
                System.out.println("nie tu");
                event.setCancelled(true);
            }else{
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
                System.out.println("--------------------------------");
                plugin.gui.menuGlowne((Player) event.getWhoClicked());
            }
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event){
        if(event.getView().getTitle().equalsIgnoreCase(plugin.gui.menuGlowneNaglowek)) {
            System.out.println("[LM](close) - zamknieto menu glowne");
        }else if(event.getView().getTitle().equalsIgnoreCase(plugin.gui.menuWyborPerkaNaglowek)) {
            System.out.println("[LM](close) - zamknieto wybor perka");
        }
    }
}
