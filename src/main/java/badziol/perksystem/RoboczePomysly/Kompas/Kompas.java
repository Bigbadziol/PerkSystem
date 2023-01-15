package badziol.perksystem.RoboczePomysly.Kompas;

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

public class Kompas  {
    private PerkSystem plugin;
    private final ItemStack kompas;
    private final String nbtNazwaPrzedmiotu="nazwaPrzedmiotu";
    private final String nbtKompasTag="perkkompas";
    private final Player wlasciciel;
    private Player cel;
    private double dystans;

    /**
     *  Utwórz obiekt kompasu.
     * @param plugin
     * @param wlasciciel
     */
    public Kompas(PerkSystem plugin, Player wlasciciel){
        this.plugin = plugin;
        this.wlasciciel = wlasciciel;
        cel = null;
        dystans = 0D;
        kompas = new ItemStack(Material.COMPASS ,1);
        CompassMeta meta = (CompassMeta) kompas.getItemMeta();
        meta.setDisplayName("Perk Kompas");
        ArrayList<String> opis = new ArrayList<>();
        opis.add("Ten kompas pozwala ci wykryć");
        opis.add("najbliższego gracza");
        meta.setLore(opis);
        kompas.setItemMeta(meta);
        NBT.modify(kompas, nbt -> {
            nbt.setString(nbtNazwaPrzedmiotu,nbtKompasTag);
        });
    }


    /**
     *  Najblizszy gracz wzgledem posiadacza kompasu
     * @return - obiekt gracza lub null jesli jest sie jedynym na serwerze.
     */
    public Player najblizszyGracz(){
        Location wLoc = wlasciciel.getLocation();
        cel = null;
        dystans = 0D;
        if (wLoc.getWorld().getEnvironment() != World.Environment.NORMAL)  return  null;

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

    public double wezDystans(){
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


    private void ustawCel(Location nowyCel){
        CompassMeta meta = (CompassMeta) kompas.getItemMeta();
        meta.setLodestone(nowyCel);
        kompas.setItemMeta(meta);
        wlasciciel.setCompassTarget(nowyCel);
    }


    public void celGracz(){
        cel = najblizszyGracz();
        if (cel == null){
            System.out.println("[Kompas] Jestes sam na serwerze.");
        }else{
            System.out.println("[KOMPAS]  dystans  gracz :"+String.format("%.2f",dystans)+ " do "+ cel.getName());
            ustawCel(cel.getLocation());
        }
    }

    public void celSmierc(){
        Location resp = wlasciciel.getLastDeathLocation();
        if (resp == null){
            System.out.println("[Kompas] - ostatnia smierc jest nullem");
        }else{
            System.out.println("[Kompas] - do miejsca zgonu");
            ustawCel(resp);
        }
    }


    public boolean perkowyKompas(ItemStack przedmiot){
        if (przedmiot == null) return false;
        NBTItem nbti = new NBTItem(przedmiot);
        if (nbti.hasTag(nbtNazwaPrzedmiotu)){
            String tag = nbti.getString(nbtNazwaPrzedmiotu);
            return tag.equalsIgnoreCase(nbtKompasTag);
        }
        return false;
    }

    boolean wEkwipunku(){
        ItemStack[] przedmioty  = wlasciciel.getInventory().getContents();
        boolean res;
        for (int i=0 ; i < przedmioty.length; i++){
            res = perkowyKompas(przedmioty[i]);
            if (res) return true;
        }
        return false;
    }

}
