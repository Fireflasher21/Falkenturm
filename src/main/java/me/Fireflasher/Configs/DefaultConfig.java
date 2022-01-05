package me.Fireflasher.Configs;

/*
 * This file is part of Falkenturm
 *
 * Falkenturm is a privat created Plugin which is not yet published
 *
 * If you copy or use the code below without consulting me first and getting a commitment, you are entering illegal area
 * If I get to know about such cases, I will take legal action
 *
 * You can contact me under my Git Acc:
 * https://github.com/Fireflasher21
 */

import me.Fireflasher.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DefaultConfig {


    private Main plugin;
    private File ConfigFile = null;
    private FileConfiguration Config = null;
    //private static File ConfigFile = new File(Bukkit.getServer().getPluginManager().getPlugin("Falkenturm").getDataFolder("plugin.yml"), "config.yml");

    public DefaultConfig() {
        this.plugin = Main.getInstance();
        saveDefaults();
    }

    public void reloadConfig() {
        if (this.ConfigFile == null) {
            this.ConfigFile = new File(this.plugin.getDataFolder(), "config.yml");
        }
        this.Config = YamlConfiguration.loadConfiguration(this.ConfigFile);

        InputStream defaulStream = this.plugin.getResource("config.yml");
        if (defaulStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaulStream));
            this.Config.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.Config == null) {
            reloadConfig();
        }
        return this.Config;
    }

    public void saveConfig() throws IOException {
        if (this.Config == null || this.ConfigFile == null) {
            return;
        }
        try {
            this.getConfig().save(this.ConfigFile);
        } catch (IOException ioe) {
            plugin.getLogger().log(Level.SEVERE, "DefaultConfig konnte nicht geladen werden" + this.ConfigFile, ioe);
        }
    }

    public void saveDefaults() {
        if (this.ConfigFile == null) {
            this.ConfigFile = new File(this.plugin.getDataFolder(), "config.yml");
        }
        if (!this.ConfigFile.exists()) {
            this.plugin.saveResource("config.yml", false);
        }
    }

    public void setup(Main plugin) throws IOException, InvalidConfigurationException {
        if (ConfigFile.exists()) {
            Config = YamlConfiguration.loadConfiguration(ConfigFile);
            System.out.println("[Falkenturm] Defaultconfig erfolgreich geladen");
        } else {
            ConfigFile = new File(plugin.getDataFolder(), "config.yml");
            Config = YamlConfiguration.loadConfiguration(ConfigFile);

            InputStream defaultstream = plugin.getResource("config.yml");
            if (defaultstream != null) {
                YamlConfiguration conf = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultstream));
                Config.setDefaults(conf);
            }
            saveConfig();
            if (ConfigFile.exists()) {
                Config = YamlConfiguration.loadConfiguration(ConfigFile);
                System.out.println("[Falkenturm] Defaultconfig erfolgreich geladen");
            } else {
                System.out.println("[Falkenturm] Defaultconfig nicht geladen");
            }
            System.out.println("[Falkenturm] Defaultconfig nicht erstellt");
        }
    }

}