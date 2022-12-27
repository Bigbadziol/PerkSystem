package badziol.perksystem.komendyTestowe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class komendaSerializeJson implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            String ret = gson.toJson(item.serialize());
            player.sendMessage("Zakodowany przedmiot(json) : "+ ret);
            System.out.println("Zakodowany przedmiot(json) : "+ ret);

            // Deserialize
            Map<String, Object> map = gson.fromJson(ret, new TypeToken<Map<String, Object>>(){}.getType());
            ItemStack is2 = ItemStack.deserialize(map);
            player.getInventory().setItem(8,is2);

        }
        return true;
    }
}