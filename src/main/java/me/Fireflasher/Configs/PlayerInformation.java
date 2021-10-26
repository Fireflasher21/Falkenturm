package me.Fireflasher.Configs;

import me.Fireflasher.HashMaps.AddTimer;
import me.Fireflasher.HashMaps.ChestLocation;
import me.Fireflasher.HashMaps.DeleteTimer;
import me.Fireflasher.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class PlayerInformation {

    private Player player;
    private File ConfigFile;
    private FileConfiguration Config;

    public PlayerInformation(){
    }

    public PlayerInformation(Player player) throws IOException, InvalidConfigurationException {
        this.player = player;
        this.ConfigFile = new File("plugins/Falkenturm/Player", this.player.getUniqueId() + ".yml");
        this.Config = YamlConfiguration.loadConfiguration(ConfigFile);

        if(!ConfigFile.exists()) {
            Config.options().header("Spielerspezifische Daten");
            Config.options().copyDefaults(true);
            save(player);
            Main.getInstance().getLogger().info("[Falkenturm] Config erfolgreich erstellt für " + player.getName());
        }
        Config.load(ConfigFile);
    }
        // ConfigFile.createNewFile();


    public PlayerInformation(String player_id) throws IOException, InvalidConfigurationException {
        this.ConfigFile = new File("plugins/Falkenturm/Player",player_id + ".yml");
        this.Config = YamlConfiguration.loadConfiguration(ConfigFile);


        save(player_id);
        Config.load(ConfigFile);
    }

    public void loadConfig() throws IOException, InvalidConfigurationException {
        File[] file = new File("plugins/Falkenturm/Player").listFiles();
        if (file == null) {
            Main.getInstance().getLogger().info("[Falkenturm] Es existieren noch keine Playerdateien");
        }
        else {
            for (File value : file) {
                String player_id = value.getName();
                player_id = player_id.substring(0, 36);
                player = Bukkit.getPlayer(UUID.fromString(player_id));
                if (player != null) {
                    Config = new PlayerInformation(player).Config;
                }
                else {
                    Config = new PlayerInformation(player_id).Config;

                    if (new DefaultConfig().getConfig().getBoolean("Falkenturm.Chest.delete")) {
                        deleteConfig(player_id);
                    }
                }
            }
            Main.getInstance().getLogger().info("[Falkenturm] PlayerInformationen erfolgreich geladen");
        }
    }

    public void save(Player player) throws IOException, InvalidConfigurationException {
        this.player = player;
        File ConFile = new File("plugins/Falkenturm/Player", this.player.getUniqueId() + ".yml");
        Config.save(ConFile);
    }

    public void save(String player_id) throws IOException, InvalidConfigurationException {

        // Debug: System.out.println(Config+ " in safe offline");
        File ConFile = new File("plugins/Falkenturm/Player", player_id + ".yml");

        Config.save(ConFile);
    }

    public FileConfiguration getConfig(Player player) throws IOException, InvalidConfigurationException {
        this.player = player;
        return Config = new PlayerInformation(player).Config;
    }

    public FileConfiguration getConfig(String playerid) throws IOException, InvalidConfigurationException {
        return Config = new PlayerInformation(playerid).Config;
    }


    protected void deleteConfig(String player_id) throws IOException, InvalidConfigurationException {
        if(Config.getBoolean("Event.Chest.set")) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastlogout = LocalDateTime.parse(Objects.requireNonNull(Config.getString("Event.LastLogout")), DateTimeFormatter.ofPattern("dd-MM-yyyy " + "kk:mm"));
            int jahreszahl = now.getYear() - lastlogout.getYear();
            if (jahreszahl == 0) {
                int difference = now.getDayOfYear() - lastlogout.getDayOfYear();
                if (difference > (new DefaultConfig().getConfig().getInt("Falkenturm.Chest.time"))) {

                    Config.set("Event.Chest.location", null);
                    Config.set("Event.Chest.set", false);

                    String name = Config.getString("Event.Name." + Config.getString("Event.Name.active"));
                    Main.getInstance().getLogger().info("[Falkenturm] Kistenstandort von " + player_id + ": " + name + " gelöscht");
                }
            } else {
                int abwesend = 365 - lastlogout.getDayOfYear();
                if (abwesend + now.getDayOfYear() > new DefaultConfig().getConfig().getInt("Falkenturm.Chest.Delete.time")) {

                    Config.set("Event.Chest.location", null);
                    Config.set("Event.Chest.set", false);

                    String name = Config.getString("Event.Name." + Config.getString("Event.Name.active"));
                    Main.getInstance().getLogger().info("[Falkenturm] Kistenstandort von " + player_id + ": " + name + " gelöscht");
                }
            }
            save(player_id);
        }
    }

    public void deleteConfig(Player player) throws IOException, InvalidConfigurationException, InterruptedException {
        this.player = player;
        Config = new PlayerInformation(player).Config;
        String delete = new ResponseConfig().getConfig().getString("Response.Messages.Ausgabe.delete");
        String delete_false = new ResponseConfig().getConfig().getString("Response.Messages.Ausgabe.delete_false");

        if(DeleteTimer.getInstance().getTime(player)) {
            Config.set("Event.Chest.location", null);
            Config.set("Event.Chest.set", false);
            save(player);
            Main.getInstance().getLogger().info("[Falkenturm] Kistenstandort von " + player.getUniqueId() + ": " + player.getName() + " gelöscht");
            ResponseConfig.nullExecuter(player,delete);
        }
        else  ResponseConfig.nullExecuter(player,delete_false);
        DeleteTimer.getInstance().delTime(player);

    }

    private void nameChange(Player player, int i) throws IOException, InvalidConfigurationException {
        this.player = player;
        if(Config.getString("Event.Names." + i) == null){
            Config.set("Event.Names.active_Name", i);
            Config.set("Event.Names." + i, player.getName());
            save(player);
        }
        else {
            if (Objects.equals(Config.getString("Event.Names." + i), player.getName())) {
                if(!Objects.equals(Config.getInt("Event.Names.active_Name"),i)) {
                    Config.set("Event.Names.active_Name", i);
                    save(player);
                }
            }
            else nameChange(player,i+1);
        }
    }

    public void setJoinConfig(Player player, String jointime) throws IOException, InvalidConfigurationException {
        this.player = player;
        if(Config.getString("Event.Firstlogin") == null){
            Config.set("Event.Firstlogin", jointime);
        }
        Config.set("Event.LastLogin", jointime);
        if (Config.getString("Event.LastLogout") == null) Config.set("Event.LastLogout", 0);
        if (Config.getString("Event.Names.Info.Banned") == null) Config.set("Event.Names.Info.Banned", false);
        else if(Config.getBoolean("Event.Names.Info.Banned")) Config.set("Event.Names.Info.Banned", false);
        nameChange(player,1);
        if (Config.getString("Event.Chest.set") == null) Config.set("Event.Chest.set", false);
        save(player);
    }

    public void setQuitConfig(Player player, String leavetime) throws IOException, InvalidConfigurationException {
        player = player;
        Config.set("Event.LastLogout", leavetime);
        save(player);
    }

    public void setBanConfig(Player player, String leavetime) throws IOException, InvalidConfigurationException {
        this.player = player;
        Config.set("Event.LastLogout", leavetime);
        Config.set("Event.Names.Info.Banned", true);
        save(player);
    }

    /*
    public static void chestUse(Player player) throws IOException, InvalidConfigurationException, InterruptedException {
        //TODO Kisteninteractevent funktioniert nicht
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = now;
        time = time.plusMinutes(1);
        while (LocalDateTime.now().isBefore(time)) {

        }
    }
     */

    public void setChestLocation(Player player, Location chestlocation) throws IOException, InvalidConfigurationException {
        String verify_true = new ResponseConfig().getConfig().getString("Response.Messages.Ausgabe.verify_true");
        this.player = player;
        Config = new PlayerInformation(player).getConfig(player);

        Config.set("Event.Chest.set",true);
        Config.set("Event.Chest.location",chestlocation);
        this.player.getServer().getLogger().info("[Falkenturm] Briefkasten wurde fuer Spieler: " + player.getName() + " gesetzt");
        save(player);
        AddTimer.getInstance().delTime(player);
        ResponseConfig.nullExecuter_void(player, verify_true);

        ChestLocation.getInstance().delLocation(player);
    }

    public Location getChestLocation(String  player_id) throws IOException, InvalidConfigurationException {
        return (Location) getConfig(player_id).get("Event.Chest.location");

    }
}
