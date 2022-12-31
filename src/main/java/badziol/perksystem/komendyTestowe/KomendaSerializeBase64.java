package badziol.perksystem.komendyTestowe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class KomendaSerializeBase64 implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();
            String encodedObject;

            try{
                //Serialize the item(turn it into byte stream)
                ByteArrayOutputStream io = new ByteArrayOutputStream();
                BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
                os.writeObject(item);
                os.flush();

                byte[] serializedObject = io.toByteArray();

                //Encode the serialized object into to the base64 format
                encodedObject = new String(Base64.getEncoder().encode(serializedObject));

                player.sendMessage("Zakodowany przedmiot(base 64): " + encodedObject);
                System.out.println("Zakodowany przedmiot(base 64): "+encodedObject);

                //Remove the item from their main hand
                player.getInventory().setItemInMainHand(null);

                //REWERS
                serializedObject = Base64.getDecoder().decode(encodedObject);
                ByteArrayInputStream in = new ByteArrayInputStream(serializedObject);
                BukkitObjectInputStream is = new BukkitObjectInputStream(in);
                ItemStack newItem = (ItemStack) is.readObject();
                player.getInventory().setItemInMainHand(newItem);

            }catch (ClassNotFoundException | IOException ex){
                System.out.println(ex.toString());
            }

        }
        return true;
    }

}