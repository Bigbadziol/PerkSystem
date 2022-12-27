
/*
//dodane - DOSTEP DO : https://www.spigotmc.org/resources/nbt-api.7939/
<repository>
<id>codemc-repo</id>
<url>https://repo.codemc.org/repository/maven-public/</url>
<layout>default</layout>
</repository>

    //to dodane bo nie chcialo zassac z orginalnego
        <repository>
            <id>egg82-ninja</id>
            <url>https://www.myget.org/F/egg82-java/maven/</url>
        </repository>

<dependency>
  <groupId>de.tr7zw</groupId>
  <artifactId>item-nbt-api-plugin</artifactId>
  <version>2.11.1</version>
  <scope>provided</scope>
</dependency>

plugin.yml -> depend: [NBTAPI]

versja aktualna - https://modrinth.com/plugin/nbtapi/versions


//dodane - WGRYWANIE PLUGINU BEZPOSREDNIO DO SERWERA
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>2.3.1</version>
                <configuration>
                    <outputDirectory>D:\BuildToolSpigot\plugins</outputDirectory>
                </configuration>
            </plugin>

//dodane - spigot i spigot-api to nie to samo - UZYSKUJEMY DOSTEP DO : GameProfile
        <dependency>
            <groupId>org.spigotmc</groupId>
            <artifactId>spigot-api</artifactId>
            <version>1.19.2-R0.1-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>
 */

package badziol.perksystem;

import badziol.perksystem.komendy.TabPerk;
import badziol.perksystem.komendy.komendaPG;
import badziol.perksystem.komendy.komendaPerk;
import badziol.perksystem.komendyTestowe.*;
import badziol.perksystem.listeners.ListenerCzastki;
import badziol.perksystem.listeners.ListenerMenu;
import badziol.perksystem.perk.CienzkaLapa.PerkCienzkaLapa;
import badziol.perksystem.perk.Ikar.ZadanieIkar;
import badziol.perksystem.perk.Ikar.komendaIkar;
import badziol.perksystem.perk.Kevlar.PerkKevlar;
import badziol.perksystem.perk.KrwawiaceRany.PerkKrwawiaceRany;
import badziol.perksystem.perk.KrwawiaceRany.ZadanieKrwawiaceRany;
import badziol.perksystem.perk.Niejadek.PerkNiejadek;
import badziol.perksystem.perk.PerkLista;
import badziol.perksystem.perk.Wampir.PerkWampir;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;


public final class PerkSystem extends JavaPlugin {
    private static PerkSystem plugin;
    public PerkLista perkLista = new PerkLista(this);
    public PerkGui gui;

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("------------------------------------------");
        System.out.println("(PerkSystem) wersja : 1.21");
        System.out.println("      --- Komendy wlasciwe ---");
        System.out.println("/perk - wstepny zarys / idea ");
        System.out.println("/pg - graficzny interfejs PerkSystem");
        System.out.println("/ikar - urucham 'aktywny' perk pozwalajacy latac");
        System.out.println();
        System.out.println("--- Komendy pomocnicze ---");
        System.out.println("/ser64 - serializowanie obiektu (lewa reka) metoda Base64 ");
        System.out.println("/serjson - serializowanie obiektu (lewa eka) gsonem");
        System.out.println("/cd - test  odpalenia czegos na cooldown, domyslnie 5 sek");
        System.out.println("/lot - test przekazania referencji do pluginu w inny sposob.");
        System.out.println("/cooldown - blokowanie samej z siebie komendy na okreslony czas");
        System.out.println("/par [liczba] 1...n - testowanie roznych typow czastek");
        System.out.println("------------------------------------------");
        getCommand("perk").setExecutor(new komendaPerk(this));
        getCommand("perk").setTabCompleter(new TabPerk(this));
        getCommand("pg").setExecutor(new komendaPG(this));
        getCommand("ikar").setExecutor(new komendaIkar(this));


        getCommand("ser64").setExecutor(new komendaSerializeBase64());
        getCommand("serjson").setExecutor(new komendaSerializeJson());
        getCommand("cd").setExecutor(new komendaCooldown());
        getCommand("lot").setExecutor(new komendaLot(this));
        getCommand("speed").setExecutor(new komendaSpeed());
        getCommand("despeed").setExecutor(new komendaDespeed());
        getCommand("ksiazka").setExecutor(new komendaKsiazka());
        getCommand("par").setExecutor(new komendaParticles(this));
        try {
            perkLista.wczytajPlik();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("[PerkSystem] - plik pewnie nie istnieje.");
        }
        gui = new PerkGui(this);

        //LISTENERY
        getServer().getPluginManager().registerEvents(new ListenerMenu(this), this);
        getServer().getPluginManager().registerEvents(new PerkNiejadek(this),this);
        getServer().getPluginManager().registerEvents(new PerkCienzkaLapa(this),this);
        getServer().getPluginManager().registerEvents(new PerkWampir(this),this);
        getServer().getPluginManager().registerEvents(new PerkKevlar(this),this);
        //...
        getServer().getPluginManager().registerEvents(new PerkKrwawiaceRany(this),this);
        //zabawa czastkami
        getServer().getPluginManager().registerEvents(new ListenerCzastki(),this);
        //ZADANIA
        //Ikar
        ZadanieIkar zadanieIkar = new ZadanieIkar(this);
        zadanieIkar.runTaskTimer(this,0L,40L); //co 40 = 2 sek
        //Krwawiace rany
        ZadanieKrwawiaceRany zadanieKrwawiaceRany = new ZadanieKrwawiaceRany(this);
        zadanieKrwawiaceRany.runTaskTimer(this,0L,10L); //co 40 = 2 sek
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
