package me.Fireflasher.Configs;

import me.Fireflasher.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ResponseConfig {

    private Main plugin;
    private File ConfigFile = null;
    private FileConfiguration Config = null;

    public ResponseConfig(){
        this.plugin = Main.getInstance();
        saveDefaults();
    }

    public void reloadConfig(){
        if(this.ConfigFile == null){
            this.ConfigFile = new File(this.plugin.getDataFolder(), "CustomResponse.yml");
        }
        this.Config = YamlConfiguration.loadConfiguration(this.ConfigFile);

        InputStream defaulStream = this.plugin.getResource("CustomResponse.yml");
        if(defaulStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaulStream));
            this.Config.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig(){
        if (this.Config == null){
            reloadConfig();
        }
        return this.Config;
    }

    public void saveConfig() throws IOException {
        if (this.Config == null || this.ConfigFile == null){
            return;
        }
        try {
            this.getConfig().save(this.ConfigFile);
        }catch (IOException ioe){
            plugin.getLogger().log(Level.SEVERE, "ResponseConfig konnte nicht geladen werden" + this.ConfigFile, ioe );
        }
    }

    public void saveDefaults(){
        if (this.ConfigFile == null){
            this.ConfigFile = new File(this.plugin.getDataFolder(), "CustomResponse.yml");
        }
        if(!this.ConfigFile.exists()){
            this.plugin.saveResource("CustomResponse.yml", false);
        }
    }

    public void reload(Main plugin) throws IOException, InvalidConfigurationException {
        ConfigFile = new File("plugins/Falkenturm","CustomResponse.yml");
        Config = YamlConfiguration.loadConfiguration(ConfigFile);

        if(ConfigFile.exists()) {
            Config = YamlConfiguration.loadConfiguration(ConfigFile);
            System.getLogger("[Falkenturm] Responseconfig erfolgreich geladen");
        } else {
            ConfigFile = new File( plugin.getDataFolder(), "CustomResponse.yml" );
            Config = YamlConfiguration.loadConfiguration(ConfigFile);

            InputStream defaultstream = plugin.getResource("CustomResponse.yml");
            if (defaultstream != null){
                YamlConfiguration conf = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultstream));
                Config.setDefaults(conf);
            }
            saveConfig();
            if(ConfigFile.exists()) {
                Config = YamlConfiguration.loadConfiguration(ConfigFile);
                System.getLogger("[Falkenturm] Responseconfig erfolgreich geladen");
            }
            else {
                System.getLogger("[Falkenturm] Responseconfig nicht erstellt");
            }
            System.getLogger("[Falkenturm] Responseconfig nicht erstellt");

        }
    }

    public static boolean nullExecuter(Player player, String message){
        String nullexecuter = new ResponseConfig().getConfig().getString("Response.Messages.No_Permission.null");
        if (message == null) {
            if(nullexecuter == null)player.sendMessage(ChatColor.DARK_RED + "[Falkenturm]" + ChatColor.RED + "Bitte achte auf deine ResponseConfig Einstellungen");
            else player.sendMessage(ChatColor.translateAlternateColorCodes('&', nullexecuter));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        return true;
    }

    public static void nullExecuter_void(Player player, String message){
        String nullexecuter = new ResponseConfig().getConfig().getString("Response.Messages.No_Permission.null");
        if (message == null) {
            if(nullexecuter == null)player.sendMessage(ChatColor.DARK_RED + "[Falkenturm]" + ChatColor.RED + "Bitte achte auf deine ResponseConfig Einstellungen");
            else player.sendMessage(ChatColor.translateAlternateColorCodes('&', nullexecuter));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

}
