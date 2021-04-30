package me.Fireflasher.Configs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class ResponseConfig {


    private static final File ConfigFile = new File("plugins/Falkenturm","CustomResponse.yml");
    //private static File ConfigFile = new File(Bukkit.getServer().getPluginManager().getPlugin("Falkenturm").getDataFolder("plugins/Falkenturm"), "CustomResponse.yml");
    private static FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);


    public static void setup() throws IOException, InvalidConfigurationException {
        if(ConfigFile.exists()) {
            Config = YamlConfiguration.loadConfiguration(ConfigFile);
            System.out.println("[Falkenturm] Responseconfig erfolgreich geladen");
        } else {
                loadConfig();
                if(ConfigFile.exists()) {
                    Config = YamlConfiguration.loadConfiguration(ConfigFile);
                    System.out.println("[Falkenturm] Responseconfig erfolgreich geladen");
                }
                else {
                    System.out.println("[Falkenturm] Responseconfig nicht erstellt");
                }
                System.out.println("[Falkenturm] Responseconfig nicht erstellt");

        }
    }

    public static void loadConfig() throws IOException, InvalidConfigurationException {
        FileConfiguration con = getConfig();
        con.options().header("Wähle deine Antworten");
        //Help Messages
        con.addDefault("Response.Messages.Help.add", "&4[Falkenturm]&r Damit kannst du eine Kiste auswählen welche ab sofort deinen Briefkasten darstellt");
        con.addDefault("Response.Messages.Help.verify_add", "&4[Falkenturm]&r Damit die ausgewählte Kiste gespeichert wird musst du sie in der Poststelle mit &9/Briefkasten &averify&r registrieren um Briefe zu bekommen");
        con.addDefault("Response.Messages.Help.delete", "&4[Falkenturm]&r Damit kannst du deinen bisherigen Briefkasten aus der Config löschen um einen neuen hinzufügen");
        con.addDefault("Response.Messages.Help.verify", "&4[Falkenturm]&r Mit diesem Befehl meldest du der Postelle deinen Briefkasten und speicherst Ihn in der Config. Dazu musst du in der Poststelle stehen");
        con.addDefault("Response.Messages.Help.verify_blocked", "&4[Falkenturm]&r Diese Funktion wurde deaktiviert");
        con.addDefault("Response.Messages.Help.send", "&4[Falkenturm]&r Mit diesem Command kannst du einen Brief an einen Spieler senden welcher einen Briefkasten besitzt");
        con.addDefault("Response.Messages.Help.send_verify", "&4[Falkenturm]&r Mit diesem Command kannst du einen Brief an einen Spieler senden welcher einen Briefkasten besitzt und du in der Poststelle bist");
        con.addDefault("Response.Messages.Help.default", "&4[Falkenturm]&r Benutze bitte einen gültigen Befehl von &9/Briefkasten &ahelp");
        //Permission Meldungen
        con.addDefault("Response.Messages.No_Permission.help", "&4Du hast keine Berechtigung für den Command &ahelp");
        con.addDefault("Response.Messages.No_Permission.add", "&4Du hast keine Berechtigung für den Command &aadd");
        con.addDefault("Response.Messages.No_Permission.delete", "&4Du hast keine Berechtigung für den Command &adelete");
        con.addDefault("Response.Messages.No_Permission.verify", "&4Du hast keine Berechtigung für den Command &averify");
        con.addDefault("Response.Messages.No_Permission.send", "&4Du hast keine Berechtigung für den Command &asend");
        con.addDefault("Response.Messages.No_Permission.default", "&4Du hast keine Berechtigung für diesen Command");
        con.addDefault("Response.Messages.No_Permission.null", "&4Bitte achte auf deine ResponseConfig Einstellungen");
        con.addDefault("Response.Messages.No_Permission.onlyplayer", "&4Diesen Befehl kann nur ein Spieler ausführen");
        //Ausgabe
        con.addDefault("Response.Messages.Ausgabe.add", "&4[Falkenturm]&r Bitte wähle deinen Briefkasten");
        con.addDefault("Response.Messages.Ausgabe.add_pending", "&4[Falkenturm]&r Du hast noch eine Anfrage laufen");
        con.addDefault("Response.Messages.Ausgabe.add_nopending", "&4[Falkenturm]&r Du hast keine austehende Anfrage");
        con.addDefault("Response.Messages.Ausgabe.add_false", "&4[Falkenturm]&r Du besitzt noch einen aktiven Briefkasten. Lösche diesen zuerst");
        con.addDefault("Response.Messages.Ausgabe.delete", "&4[Falkenturm]&r Briefkasten entfernt");
        con.addDefault("Response.Messages.Ausgabe.delete_false", "&4[Falkenturm]&r Briefkasten nicht entfernt, da die Anfrage zu weit zurückliegt");
        con.addDefault("Response.Messages.Ausgabe.delete_verify1", "&4[Falkenturm]&r Bist du dir sicher das du deinen Briefkasten löschen willst?");
        con.addDefault("Response.Messages.Ausgabe.verify_true", "&4[Falkenturm]&r Die Poststelle hat deinen Briefkasten nun registriert");
        con.addDefault("Response.Messages.Ausgabe.verify_false", "&4[Falkenturm]&r Bitte finde dich in der Poststelle ein um deinen Briefkasten anzumelden");
        con.addDefault("Response.Messages.Ausgabe.send_true", "&4[Falkenturm]&r Dein Brief wurde an den Briefkasten der Person zugestellt");
        con.addDefault("Response.Messages.Ausgabe.send_false", "&4[Falkenturm]&r Du musst dich in der Poststelle befinden um den Brief abzusenden");
        con.addDefault("Response.Messages.Ausgabe.send_false_np", "&4[Falkenturm]&r Gib einen Spieler an");
        con.addDefault("Response.Messages.Ausgabe.send_false_pl", "&4[Falkenturm]&r Diesen Spieler gibt es nicht");
        con.addDefault("Response.Messages.Ausgabe.send_no_chest", "&4[Falkenturm]&r Der Spieler besitzt keinen Briefkasten");
        con.addDefault("Response.Messages.Ausgabe.send_chest_space", "&4[Falkenturm]&r Der Briefkasten ist voll");
        con.addDefault("Response.Messages.Ausgabe.send_no_book", "&4[Falkenturm]&r Du brauchst einen gültigen Brief");

        con.options().copyDefaults(false);
        save();
        con.load(ConfigFile);
    }

    public static FileConfiguration getConfig(){
        return Config;
    }

    public static void save() throws IOException {
        Config.save(ConfigFile);
    }

    public static boolean nullExecuter(Player player, String message){
        String nullexecuter = ResponseConfig.getConfig().getString("Response.Messages.No_Permission.null");
        if (message == null) {
            if(nullexecuter == null)player.sendMessage(ChatColor.DARK_RED + "[Falkenturm]" + ChatColor.RED + "Bitte achte auf deine ResponseConfig Einstellungen");
            else player.sendMessage(ChatColor.translateAlternateColorCodes('&', nullexecuter));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        return true;
    }

    public static void nullExecuter_void(Player player, String message){
        String nullexecuter = ResponseConfig.getConfig().getString("Response.Messages.No_Permission.null");
        if (message == null) {
            if(nullexecuter == null)player.sendMessage(ChatColor.DARK_RED + "[Falkenturm]" + ChatColor.RED + "Bitte achte auf deine ResponseConfig Einstellungen");
            else player.sendMessage(ChatColor.translateAlternateColorCodes('&', nullexecuter));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    /*
    public static void test(Player player){
        Config = YamlConfiguration.loadConfiguration(ConfigFile);
        player.sendMessage(Config.getString("Response.Messages.Ausgabe.send_true"));
    }
    */
}
