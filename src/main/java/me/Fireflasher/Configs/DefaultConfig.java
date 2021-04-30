package me.Fireflasher.Configs;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DefaultConfig {


    private static final File ConfigFile = new File("plugins/Falkenturm","config.yml");
    //private static File ConfigFile = new File(Bukkit.getServer().getPluginManager().getPlugin("Falkenturm").getDataFolder("plugin.yml"), "config.yml");
    private static FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);

    public static void setup() throws IOException, InvalidConfigurationException {
        if(ConfigFile.exists()){
            Config = YamlConfiguration.loadConfiguration(ConfigFile);
            System.out.println("[Falkenturm] Defaultconfig erfolgreich geladen");
        }
        else {
                loadConfig();
                if(ConfigFile.exists()) {
                    Config = YamlConfiguration.loadConfiguration(ConfigFile);
                    System.out.println("[Falkenturm] Defaultconfig erfolgreich geladen");
                }
                else {
                    System.out.println("[Falkenturm] Defaultconfig nicht geladen");
                }
                System.out.println("[Falkenturm] Defaultconfig nicht erstellt");
        }
    }

    public static void loadConfig() throws IOException, InvalidConfigurationException {
        getConfig().options().header("DefaultConfig");
        /*
        getConfig().addDefault("Falkenturm.verify", false);

         */
        getConfig().addDefault("Falkenturm.Chest.delete", false);
        getConfig().addDefault("Falkenturm.Chest.time", 30);
        getConfig().addDefault("Falkenturm.Letter.to_worlds", true);
        getConfig().addDefault("Falkenturm.Letter.close", true);
        getConfig().addDefault("Falkenturm.Letter.change_author", false);

        getConfig().options().copyDefaults(true);
        save();
        getConfig().load(ConfigFile);
    }

    public static void save() throws IOException{
        getConfig().save(ConfigFile);
    }

    public static FileConfiguration getConfig(){
        return Config;
    }
}
