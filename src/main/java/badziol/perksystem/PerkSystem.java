//07.01.2023 - LAPTOP
//12.01 - duza zmiana to uskrzydlenie ikara
//Bukkit Tutorial | Complex Particle Effects #1
//https://www.youtube.com/watch?v=BJaD2AgXF_U
//https://www.youtube.com/watch?v=XMeck_nTdVA

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

//28.12.2022 - dokonczony perk krwawienie
package badziol.perksystem;

import badziol.perksystem.komendy.TabPerk;
import badziol.perksystem.komendy.KomendaPG;
import badziol.perksystem.komendy.KomendaPerk;
import badziol.perksystem.komendyTestowe.*;
import badziol.perksystem.listeners.ListenerJoin;
import badziol.perksystem.listeners.ListenerMenu;
import badziol.perksystem.listeners.ListenerQuit;
import badziol.perksystem.perk.CiezkaLapa.PerkCiezkaLapa;
import badziol.perksystem.perk.Ikar.ZadanieIkar;
import badziol.perksystem.perk.Ikar.KomendaIkar;
import badziol.perksystem.perk.Kevlar.PerkKevlar;
import badziol.perksystem.perk.KrwawiaceRany.PerkKrwawiaceRany;
import badziol.perksystem.perk.KrwawiaceRany.ZadanieKrwawiaceRany;
import badziol.perksystem.perk.Lowca.KomendaLowca;
import badziol.perksystem.perk.Lowca.PerkLowca;
import badziol.perksystem.perk.Niejadek.PerkNiejadek;
import badziol.perksystem.perk.PerkLista;
import badziol.perksystem.perk.Treser.PerkTreser;
import badziol.perksystem.perk.Treser.ZadanieTreser;
import badziol.perksystem.perk.Wampir.PerkWampir;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;


public final class PerkSystem extends JavaPlugin {
    private final static String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace("org.bukkit.craftbukkit", "").replace(".", "");
    private static PerkSystem plugin;
    public PerkLista perkLista = new PerkLista(this);
    public PerkGui gui;

    //public PosiadaczeKompasow posiadaczeKompasow = new PosiadaczeKompasow(this);


    public static Plugin getPlugin() {
        return plugin;
    }

    public static PerkSystem getInstance() {
        return plugin;
    }

    public String getServerVersion() {
        return VERSION;
    }
    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("------------------------------------------");
        System.out.println("(PerkSystem) wersja : 1.57");
        System.out.println("      --- Komendy wlasciwe ---");
        System.out.println("/perk - wstepny zarys / idea ");
        System.out.println("/pg - graficzny interfejs PerkSystem");
        System.out.println("/ikar - urucham 'aktywny' perk pozwalajacy latac");
        System.out.println("/lowca [daj] - utworz przedmiot , [gracz] ustawi cel na gracza , [smierc] - ustawi na miejsce zgonu");
        System.out.println();
        System.out.println("--- Komendy pomocnicze ---");
        System.out.println("/ser64 - serializowanie obiektu (lewa reka) metoda Base64 ");
        System.out.println("/serjson - serializowanie obiektu (lewa eka) gsonem");
        System.out.println("------------------------------------------");
        Objects.requireNonNull(getCommand("perk")).setExecutor(new KomendaPerk(this));
        Objects.requireNonNull(getCommand("perk")).setTabCompleter(new TabPerk(this));

        Objects.requireNonNull(getCommand("pg")).setExecutor(new KomendaPG(this));
        Objects.requireNonNull(getCommand("ikar")).setExecutor(new KomendaIkar(this));
        Objects.requireNonNull(getCommand("lowca")).setExecutor(new KomendaLowca(this));

        Objects.requireNonNull(getCommand("ser64")).setExecutor(new KomendaSerializeBase64());
        Objects.requireNonNull(getCommand("serjson")).setExecutor(new KomendaSerializeJson());


        try {
            boolean wczytanie;
            boolean kontrola = false;
            wczytanie = perkLista.wczytajPlik();
            if (wczytanie) kontrola = perkLista.kontrolaIntegralnosciDanych();
            if (kontrola){
                System.out.println("(PerkSystem) - nie wykryto problemow z integralnoscia danych.");
            }else{
                System.out.println("(PerkSystem) - wykonano kroki niezbedne do przywrocenia integralnosci danych." );
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("[PerkSystem] - plik pewnie nie istnieje.");
        }
        gui = new PerkGui(this);

        //LISTENERY
        getServer().getPluginManager().registerEvents(new ListenerMenu(this), this);
        getServer().getPluginManager().registerEvents(new PerkNiejadek(this),this);
        getServer().getPluginManager().registerEvents(new PerkCiezkaLapa(this),this);
        getServer().getPluginManager().registerEvents(new PerkWampir(this),this);
        getServer().getPluginManager().registerEvents(new PerkKevlar(this),this);
        getServer().getPluginManager().registerEvents(new PerkKrwawiaceRany(this), this);
        getServer().getPluginManager().registerEvents(new PerkTreser(this),this);
        getServer().getPluginManager().registerEvents(new PerkLowca(this),this);


        //ZADANIA
        //Ikar
        ZadanieIkar zadanieIkar = new ZadanieIkar(this);
        zadanieIkar.runTaskTimer(this,0L,20L); //co 40 = 2 sek
        //Krwawiace rany
        ZadanieKrwawiaceRany zadanieKrwawiaceRany = new ZadanieKrwawiaceRany(this);
        zadanieKrwawiaceRany.runTaskTimer(this,0L,20L); //co 40 = 2 sek
        //Treser
        ZadanieTreser zadanieTreser = new ZadanieTreser(this);
        zadanieTreser.runTaskTimer(this,0L,20L);

        //TESTY TESTY

        //TO MUSI BYC NA SAMYM KONCU
        getServer().getPluginManager().registerEvents(new ListenerJoin(this),this);
        getServer().getPluginManager().registerEvents(new ListenerQuit(this),this);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
