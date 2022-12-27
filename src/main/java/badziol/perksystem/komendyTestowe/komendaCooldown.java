package badziol.perksystem.komendyTestowe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class komendaCooldown  implements CommandExecutor {
    //The key is the Player, and the long is the epoch time of the last time they ran the command
    private final HashMap<UUID, Long> mapaCd;
    private final long czas = 5000L;

    public komendaCooldown() {
        this.mapaCd = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            System.out.println("z komendy /cd uuid: "+uuid);
            //czy gracz na mapieCd, jesli nie to dodaj go.
            //Jesli na mapie sprawdz czy uplynal "wiekszy czas"

            if (!mapaCd.containsKey(player.getUniqueId()) || System.currentTimeMillis() - mapaCd.get(player.getUniqueId()) > czas) {
                mapaCd.put(player.getUniqueId(), System.currentTimeMillis());
                player.sendMessage("Perk odpalony....");
            } else {
                player.sendMessage("Poczekaj   " + (czas - (System.currentTimeMillis() - mapaCd.get(player.getUniqueId()))) + " ms");
            }
        }
        return true;
    }
}

