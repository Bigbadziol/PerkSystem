package badziol.perksystem.komendyTestowe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class komendaSpeed implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            PotionEffect potion = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false);
            //potion.apply(player);
            player.sendMessage("Speed siorbniety");
            System.out.println("[speed]");
            //Player#isSprinting();
            //PlayerToggleSprintEvent
            //player.setWalkSpeed(0.2f); // dla 1 - zapierdziela jak lopez
        }
        return true;
    }
}

