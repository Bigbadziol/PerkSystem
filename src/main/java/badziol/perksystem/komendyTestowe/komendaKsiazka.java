package badziol.perksystem.komendyTestowe;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class komendaKsiazka  implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ItemStack ksiazka = new ItemStack(Material.WRITTEN_BOOK, 1);
            BookMeta ksiazkaMeta = (BookMeta) ksiazka.getItemMeta();
            ksiazkaMeta.setTitle("Perki");
            ksiazkaMeta.setAuthor("Filipos");
            ksiazkaMeta.addPage("To jest przykladowa strona zapisana \n w ksizace.");
            ksiazkaMeta.addPage("Kolejny\n test\n strony\n.");
            ksiazkaMeta.addPage("abc\n ffff");
            //ArrayList<String> strony = new ArrayList<>();
            //strony.add("Strona 1");
            //strony.add("Strona 2");
            //strony.add("Strona 3");
            //ksiazkaMeta.setPages(strony);
            ksiazka.setItemMeta(ksiazkaMeta);

            p.getInventory().addItem(ksiazka);
        }

        return true;
    }
}
