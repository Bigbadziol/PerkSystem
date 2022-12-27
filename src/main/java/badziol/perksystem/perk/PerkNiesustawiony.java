package badziol.perksystem.perk;
//Specjalny perk , kiedy gracz ma limit np. 4 perkow a ustawione 3
//-odpcja na ustawienie , lub wylaczenie dzialania perka


import badziol.perksystem.PerkSystem;

public class PerkNiesustawiony extends Perk{
    public PerkNiesustawiony(PerkSystem plugin){
        super(plugin);
        nazwaId=PerkStale.PERK_NIEUSTAWIONY;
        wyswietlanie="Ustaw mnie.";
        opis.add("Wlaczony perk w istotny sposob ");
        opis.add("wzmacnia twoja postac");
        //https://minecraft-heads.com/custom-heads/decoration/59528-feather
        textura="eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ"+
                "nQubmV0L3RleHR1cmUvZTdhYjg5NTRmZDE5ZWFkMWE5MDQ4MjNlZGMwMGRjYTRjMDBhND"+
                "dkYzJiOTAwMWU4OWQ0MmUxYjIyZWQxNmY2ZSJ9fX0=";

        inicjujGlowke();
    }
}
