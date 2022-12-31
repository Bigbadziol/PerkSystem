package badziol.perksystem.komendyTestowe;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import static badziol.perksystem.perk.PerkStale.INSTRUKCJA_TRESC;

public class KomendaKsiazka implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ItemStack ksiazka = new ItemStack(Material.WRITTEN_BOOK, 1);
            BookMeta ksiazkaMeta = (BookMeta) ksiazka.getItemMeta();
            ksiazkaMeta.setTitle("Jakis tytul");
            ksiazkaMeta.setAuthor("Country Fox");
            //mozna tak:
                //ksiazkaMeta.addPage("To jest przykladowa strona zapisana \n w ksizace.");
                //ksiazkaMeta.addPage("Kolejny\n test\n strony\n.");
                //ksiazkaMeta.addPage("abc\n ffff");
            //lub jako arraylist:
                //ArrayList<String> strony = new ArrayList<>();
                //strony.add("Strona 1");
                //strony.add("Strona 2");
                //strony.add("Strona 3");
                //ksiazkaMeta.setPages(strony);
            //albo:
            ksiazkaMeta.setPages(INSTRUKCJA_TRESC);
            ksiazka.setItemMeta(ksiazkaMeta);

            System.out.println("Rozmiar ksiazki : " + INSTRUKCJA_TRESC.length);
            //p.getInventory().addItem(ksiazka);
            //p.closeInventory();
            p.openBook(ksiazka);


        }

        return true;
    }
}
