package badziol.perksystem.listeners;


import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ListenerCzastki  implements Listener {

    @EventHandler
    public  void onRuchGracza(PlayerMoveEvent event){
        Player gracz = event.getPlayer();
        //czastki kierunkowe  dzialaja inaczej , lista czastek :
        //BUBBLE_COLUMN_UP      BUBBLE_POP      CAMPFIRE_COZY_SMOKE     CAMPFIRE_SIGNAL_SMOKE
        //CLOUD     CRIT      CRIT_MAGIC    DAMAGE_INDICATOR    DRAGON_BREATH   ELECTRIC_SPARK
        //ENCHANTMENT_TABLE    END_ROD     EXPLOSION_NORMAL    FIREWORKS_SPARK     FLAME
        //NAUTILUS      PORTAL      REVERSE_PORTAL      SCRAPE      SCULK_CHARGE
        //SCULK_CHARGE_POP      SCULK_SOUL      SMALL_FLAME     SMOKE_LARGE     SMOKE_NORMAL
        //SOUL      SOUL_FIRE_FLAME     SPIT    SQUID_INK   TOTEM
        //WATER_BUBBLE      WATER_WAKE      WAX_OFF     WAX_ON

        //ustawic licznik 0(count) , wtedy -> x,y,z zmieniaja swoje znaczenie , nie sa ofsetem a
        // parametrem kierunkowym

        //kierunkowe 1
        //zielony falujacy waz od stop
//        gracz.getWorld().spawnParticle(Particle.TOTEM,gracz.getLocation(),0,2,2,2);

        // kierunkowe 2 - dla sprintera ????
        //Location pozycjaEfektu = gracz.getLocation();
        //pozycjaEfektu.setY(pozycjaEfektu.getY() + 0.5d);
        //gracz.spawnParticle(Particle.BUBBLE_COLUMN_UP,gracz.getLocation(),20,0.5d,1.5d,0.5d);



        //colorized - przeniesione z case 4 - fajne
/*
        Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(255, 165, 0), Color.fromRGB(255,0 , 0), 2.0F);
        gracz.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, gracz.getLocation(), 50, dustTransition);
*/
        //colorized
        //Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255,255,0),1.5f);
        //Particle.DustOptions dust = new Particle.DustOptions(Color.YELLOW,1.5f);
        //gracz.getWorld().spawnParticle(Particle.REDSTONE,gracz.getLocation(),50,dust);


        //double red = 0 / 255D;
        //double green = 127 / 255D;
        //double blue = 255 / 255D;
        //gracz.getWorld().spawnParticle(Particle.SPELL_MOB, gracz.getLocation(), 0, red, green, blue, 1);
/*
        Spawning NOTE particles is very similar to spawning SPELL_MOB particles.
        The difference is that there are only 24 color values to choose from.
        The offsetX value should be a number between 0 and 1 and be in intervals of 1/24.
        The offsetY and offsetZ values should always be 0.
        Make sure the count is set to 0 and the extra value is set to 1.
        Here is an example of how to spawn a red note:


 */
        //double note = 6 / 24D; // 6 is the value of the red note
        //gracz.getWorld().spawnParticle(Particle.NOTE, gracz.getLocation(), 0, note, 0, 0, 1);

        //material
        //Efekty moga byc skojarzone z dwoma zmianami stanu bloku :ITEM_CRACK FALLING_DUST

        //dobranie sie do danych konkretnego bloku
        //mozna wiele efektow nakladac na raz , przyklad :
/*
        ItemStack itemCrackData = new ItemStack(Material.REDSTONE_BLOCK);
        gracz.getWorld().spawnParticle(Particle.ITEM_CRACK, gracz.getLocation(), 3, itemCrackData);

        BlockData fallingDustData = Material.STONE.createBlockData();
        gracz.getWorld().spawnParticle(Particle.FALLING_DUST, gracz.getLocation(), 10, fallingDustData);
*/

        //vibration
        //BlockDestination - wskazuje cel , tu jest troszke bezsensu bo wskazuje na samego siebie
        // pomyslec np: aby wskazywal na przeciwnika
/*
        Player cel = Bukkit.getPlayerExact("majka_maja");
        if (cel == null ) {
            cel = gracz;
        }
        Vibration vibration = new Vibration(gracz.getLocation(), new Vibration.Destination.BlockDestination(cel.getLocation()), 40);
       // gracz.getWorld().spawnParticle(Particle.VIBRATION, gracz.getLocation(), 1, vibration);
        gracz.getWorld().spawnParticle(Particle.SHRIEK, gracz.getLocation(), 1,40);

*/
        //Pomysl na ikara - aktywacja perka lub lot
        //Location pozycjaEfektu = gracz.getLocation();
        //pozycjaEfektu.setY(pozycjaEfektu.getY() + 0.5d);
        //gracz.spawnParticle(Particle.BUBBLE_COLUMN_UP,gracz.getLocation(),20,0.5d,1.5d,0.5d);


        //Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(255, 0, 0), Color.fromRGB(0, 255, 0), 3.0F);
        //gracz.spawnParticle(Particle.DUST_COLOR_TRANSITION, gracz.getLocation(), 50, dustTransition);
    }

}
