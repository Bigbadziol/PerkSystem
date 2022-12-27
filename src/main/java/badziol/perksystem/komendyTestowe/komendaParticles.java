package badziol.perksystem.komendyTestowe;

import badziol.perksystem.PerkSystem;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class komendaParticles implements CommandExecutor {
    private final PerkSystem plugin;

    public komendaParticles(PerkSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length == 0){
                System.out.println("[Czastki] - nie wiem co robic");

            }else if (args.length == 1){
                String parametr = args[0];
                int wybor = -1;
                boolean bladKonwersji = false;
                try {
                    wybor = Integer.parseInt(parametr);
                }catch(Exception e){
                    bladKonwersji = true;
                }
                if (bladKonwersji){
                    System.out.println("[Czastki] - nie udalo sie konwertowac parametru na liczbe");
                    return true;
                }
                switch(wybor){
                    case 1->{
                        System.out.println("[Czastki] - wybor 1 - typ - colored particles");
                        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 127, 255), 1.0F);
                        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 50, dustOptions);
                    }
                    case 2->{
                        System.out.println("[Czastki] - wybor 2 - typ - colored particles");
                        double red = 0 / 255D;
                        double green = 127 / 255D;
                        double blue = 255 / 255D;
                        player.getWorld().spawnParticle(Particle.SPELL_MOB, player.getLocation(), 0, red, green, blue, 10);
                    }
                    case 3->{
                        System.out.println("[Czastki] - wybor 3 - colored particles");
                        double note = 6 / 24D; // 6 is the value of the red note
                        player.getWorld().spawnParticle(Particle.NOTE, player.getLocation(), 0, note, 0, 0, 10);
                    }
                    case 4->{
                        System.out.println("[Czastki] - wybor 4 - colored particles");
                        Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(255, 0, 0), Color.fromRGB(255, 255, 255), 1.0F);
                        player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, player.getLocation(), 50, dustTransition);
                    }
                    case 5->{
                        System.out.println("[Czastki] - wybor 5 wysokosc oka");
                        player.getWorld().spawnParticle(Particle.DRIP_LAVA, player.getEyeLocation().add(0,1,0),25);
                    }
                    case 6->{
                        System.out.println("[Czastki] - wybor 6 offset");
                        //player.getWorld().spawnParticle(Particle.FALLING_LAVA,50,0,10,25);
                        player.spawnParticle(Particle.FALLING_LAVA, player.getLocation(),25,0,10,0);
                    }
                    case 10->{
                        Location loc = player.getLocation();
                        int rad = 2;
                        final double[] y = {0};
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                double x = rad * Math.cos(y[0]);
                                double z = rad * Math.sin(y[0]);
                                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255,255,0),1.5f);
                                player.getWorld().spawnParticle(Particle.REDSTONE,loc.add(x/2, y[0]/3,z/2),35,dust);
                                y[0] += 0.05d;
                            }
                        }.runTaskTimerAsynchronously(plugin,0,5);

                    }
                    default -> {
                        System.out.println("[Czastki] - nie obslugiwana wartosc");
                    }
                }//switch

            }
        }

        return true;
    }
}
