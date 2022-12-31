package badziol.perksystem.perk.Treser;

import badziol.perksystem.PerkSystem;
import badziol.perksystem.perk.Perk;
import badziol.perksystem.perk.PerkStale;

//https://minecraft-heads.com/custom-heads/animals/59115-blue-doge
// "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFkMjZjNWM4Y2NmNTgzYWM5ZWI0ZDBmN2ZhZjQ1ZjVlNGE5YTE2NzAwODYzMmZkM2I5NzkxYWMyNGVhOGI1NyJ9fX0="
public class PerkTreser extends Perk {

    public PerkTreser(PerkSystem plugin) {
        super(plugin);
        nazwaId= PerkStale.PERK_TRESER;
        wyswietlanie="Treser";
        opis.add("Możesz przyzwac psa");
        opis.add("wspomagajacego cie w walce.");
        //https://minecraft-heads.com/custom-heads/animals/59115-blue-doge
        textura="eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cm" +
                "UvZGFkMjZjNWM4Y2NmNTgzYWM5ZWI0ZDBmN2ZhZjQ1ZjVlNGE5YTE2NzAwODYzMmZkM2I5NzkxYWMyNGVhOGI1NyJ9fX0";
        efektWidoczny = true;
        inicjujGlowke();
    }
}
