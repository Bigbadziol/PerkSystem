package badziol.perksystem.perk.Lowca;

import badziol.perksystem.PerkSystem;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.ArrayList;
import java.util.Objects;

public class KompasLowcy {
    private final PerkSystem plugin;
    private final ItemStack kompas;
    private final String nbtNazwaPrzedmiotu="nazwaPrzedmiotu";
    private final String nbtKompasTag="perkkompas";
    private final Player wlasciciel;
    private Player cel;
    private double dystans;

    /**
     *  Utwórz obiekt kompasu.
     * @param plugin - referencja do korzenia
     * @param wlasciciel - właściciel
     */
    public KompasLowcy(PerkSystem plugin, Player wlasciciel){
        this.plugin = plugin;
        this.wlasciciel = wlasciciel;
        cel = null;
        dystans = 0D;
        kompas = new ItemStack(Material.COMPASS ,1);
        CompassMeta meta = (CompassMeta) kompas.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Kompas lowcy");
            ArrayList<String> opis = new ArrayList<>();
            opis.add("Ten kompas pozwala ci wykryć");
            opis.add("najbliższego gracza");
            meta.setLore(opis);
            kompas.setItemMeta(meta);
        }
        //dodaj tag, który pozwoli określić nam w przyszłości czy porównywany przedmiot jest 'perkowym kompasem'
        NBT.modify(kompas, nbt -> {
            nbt.setString(nbtNazwaPrzedmiotu,nbtKompasTag);
        });
    }


    /**
     *  Najbliższy gracz względem posiadacza kompasu
     * @return - obiekt gracza lub null jeśli jest się jedynym na serwerze.
     */
    public Player najblizszyGracz(){
        Location wLoc = wlasciciel.getLocation();
        cel = null;
        dystans = 0D;
        if (Objects.requireNonNull(wLoc.getWorld()).getEnvironment() != World.Environment.NORMAL)  return  null;

        for (Player gracz : plugin.getServer().getOnlinePlayers()) {
            if (gracz.getUniqueId() == wlasciciel.getUniqueId()) continue;

            double wX = wLoc.getX();
            double wY = wLoc.getY();
            double gX = gracz.getLocation().getX();
            double gY = gracz.getLocation().getY();
            double dystansGracze = Math.sqrt( Math.pow(wX-gX,2) + Math.pow(wY - gY,2));

            //System.out.println("[KOMPAS] - Dystans gracze: "+dystansGracze);

            if (cel == null){
                dystans = dystansGracze;
                cel = gracz;
            }else{
                if (dystansGracze < dystans){
                    dystans = dystansGracze;
                    cel = gracz;
                }
            }
        }
        return cel;
    }

    /**
     * Pobierz odległość pomiędzy właścicielem kompasu a najbliższym graczem;
     * @return odległość pomiędzy posiadaczem a najbliższym graczem
     */
    public double wezDystans(){
        najblizszyGracz();
        return  dystans;
    }

    /**
     * Utwórz fizyczny przedmiot i dodaj go do ekwipunku gracza. Sprawdź, czy przedmiot nie występuje już
     * w ekwipunku.
     */
    public void utworzKompas(){
        if (! wlasciciel.getInventory().contains(kompas))  wlasciciel.getInventory().addItem(kompas);
        else{
            System.out.println("[KOMPAS] masz juz kompas.");
        }
    }

    /**
     * Metoda pomocnicza, która ustawia magnetyt kompasu na określoną lokalizację
     * @param nowyCel - lokalizacja naszego wirtualnego magnetytu.
     */
    private void ustawCel(Location nowyCel){
        CompassMeta meta = (CompassMeta) kompas.getItemMeta();
        if (meta != null) {
            meta.setLodestone(nowyCel);
            kompas.setItemMeta(meta);
        }
        wlasciciel.setCompassTarget(nowyCel);
    }

    /**
     *  Kompas jako cel będzie wskazywał najbliższego gracza jeśli, właściciel nie jest sam na serwerze.
     */

    public void celGracz(){
        cel = najblizszyGracz();
        if (cel == null){
            System.out.println("[Kompas] Jestes sam na serwerze.");
        }else{
            System.out.println("[KOMPAS]  dystans  gracz :"+String.format("%.2f",dystans)+ " do "+ cel.getName());
            ustawCel(cel.getLocation());
        }
    }

    /**
     *  Kompas jako cel będzie wskazywał miejsce ostatniej śmierci gracza
     */
    public void celSmierc(){
        Location resp = wlasciciel.getLastDeathLocation();
        if (resp == null){
            System.out.println("[Kompas] - ostatnia smierc jest nullem");
        }else{
            System.out.println("[Kompas] - do miejsca zgonu");
            ustawCel(resp);
        }
    }

    /**
     *  Na podstawie prywatnego nbt tagu, kompas sprawdza czy porównywany przedmiot jest 'perkowym kompasem'
     * @param przedmiot - porównywany przedmiot
     * @return - true, jeśli przedmiot posiada tag
     */
    public boolean perkowyKompas(ItemStack przedmiot){
        if (przedmiot == null) return false;
        NBTItem nbti = new NBTItem(przedmiot);
        if (nbti.hasTag(nbtNazwaPrzedmiotu)){
            String tag = nbti.getString(nbtNazwaPrzedmiotu);
            return tag.equalsIgnoreCase(nbtKompasTag);
        }
        return false;
    }

    /**
     * Metoda pozwalająca stwierdzić czy w ekwipunku właściciela kompasu 'perkowy kompas' występuje.
     * Gracz w każdej chwili może wyrzucić przedmiot.
     * @return - true, jeśli przedmiot nadal występuje w ekwipunku gracza.
     */
    boolean wEkwipunku(){
        ItemStack[] przedmioty  = wlasciciel.getInventory().getContents();
        boolean res;
        for (ItemStack tenPrzedmiot : przedmioty) {
            res = perkowyKompas(tenPrzedmiot);
            if (res) return true;
        }
        return false;
    }
}