import me.Fireflasher.Configs.DefaultConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PlayerInformationold {


    private Player online_player;
    private OfflinePlayer offline_player;
    private File configFile;
    private FileConfiguration config;

    private static PlayerInformationold PlayerInformationold_on;
    private static PlayerInformationold PlayerInformationold_of;

    public PlayerInformationold() {}

    public PlayerInformationold(Player player) throws IOException, InvalidConfigurationException {
        PlayerInformationold_on = this;
        online_player = player;
        configFile = new File("plugins/Falkenturm/Player", online_player.getUniqueId() + ".yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        if(!configFile.exists()) {
            config.options().header("Spielerspezifische Daten");
            config.options().copyDefaults(true);
            save(config, online_player.getUniqueId().toString());
            if(!configFile.exists()){
                try {
                    loadConfig();
                }
                catch (IOException | InvalidConfigurationException ignored){

                }
            }
        }
        config.load(configFile);
    }
    public PlayerInformationold(String player_id) throws IOException, InvalidConfigurationException {
        PlayerInformationold_of = this;
        offline_player = Bukkit.getPlayer(UUID.fromString(player_id));
        configFile = new File("plugins/Falkenturm/Player", player_id + ".yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        if(!configFile.exists()) {
            config.options().header("Spielerspezifische Daten");
            config.options().copyDefaults(true);
            save(config, player_id);
        }
        config.load(configFile);
    }

    public void loadConfig() throws IOException, InvalidConfigurationException {
        File[] file = new File("plugins/Falkenturm/Player").listFiles();
        if (file == null) {
            System.out.println(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.GREEN + " PlayerInformationolden Es existieren noch keine Playerdateien");
        }
        else {
            for (File value : file) {
                String player_id = value.getName();
                player_id = player_id.substring(0, 36);
                Player player = Bukkit.getPlayer(UUID.fromString(player_id));
                if (player != null) {
                    online_player = player;
                    new PlayerInformationold(online_player);
                }
                else {
                    new PlayerInformationold(player_id);

                    if (DefaultConfig.getConfig().getBoolean("Falkenturm.Chest.Delete")) {
                        deleteConfig(player_id);
                    }
                }
            }
            System.out.println(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.GREEN + " PlayerInformationolden erfolgreich geladen");
        }
    }

    public void save(FileConfiguration con, String player_id) throws IOException {
        con.save(player_id+".yml");
    }

    public FileConfiguration getConfig(Player player) throws IOException, InvalidConfigurationException {
        online_player = player;
        if(PlayerInformationold_on.config != null){
            return PlayerInformationold_on.config;
        }
        return new PlayerInformationold(online_player).config;
    }

    public FileConfiguration getConfig(String player_id) throws IOException, InvalidConfigurationException {
        if(PlayerInformationold_of.config != null){
            return PlayerInformationold_of.config;
        }
        return new PlayerInformationold(player_id).config;
    }

    protected void deleteConfig(String player_id) throws IOException, InvalidConfigurationException {
        PlayerInformationold_of = new PlayerInformationold(player_id);
        LocalDate now = LocalDate.now();
        LocalDate lastlogout = LocalDate.parse(Objects.requireNonNull(PlayerInformationold_of.config.getString("Name.Event.LastLogout")), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        // System.out.println(lastlogout.getYear());
        int jahreszahl = now.getYear() - lastlogout.getYear();
        if (jahreszahl == 0) {
            int difference = now.getDayOfYear() - lastlogout.getDayOfYear();
            if (difference > DefaultConfig.getConfig().getInt("Falkenturm.Chest.Delete.Time")) {

                PlayerInformationold_of.config.set("Event.Chest.location", null);

                String name = PlayerInformationold_of.config.getString("Event.Name." + PlayerInformationold_of.config.getInt("Event.Name.active"));
                System.out.println(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.GREEN + " Kistenstandort von " + ChatColor.GREEN + player_id + ": " + name + " gelöscht");
            }
        }
        else {
            int abwesend = 365 - lastlogout.getDayOfYear();
            if (abwesend + now.getDayOfYear() > DefaultConfig.getConfig().getInt("Falkenturm.Chest.Delete.Time")){

                PlayerInformationold_of.config.set("Event.Chest.location", null);

                String name = config.getString("Event.Name." + PlayerInformationold_of.config.getInt("Event.Name.active"));
                System.out.println(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.GREEN + " Kistenstandort von " + ChatColor.GREEN + player_id + ": " + name + " gelöscht");
            }
        }
        save(PlayerInformationold_of.config, player_id);
    }

    public void deleteConfig(Player player) throws IOException, InvalidConfigurationException, InterruptedException {
        PlayerInformationold_on = new PlayerInformationold(player);

        PlayerInformationold_on.online_player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RED + "Bist du dir sicher das du deinen Briefkasten löschen willst?");
        PlayerInformationold_on.online_player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RED + "Bestätige mit /Briefkasten delete verify");
        boolean timer = true;
        boolean success = false;
        LocalDateTime start = LocalDateTime.now();
        while (timer){
            if(start.plusMinutes(1).isBefore(LocalDateTime.now())) {
                if (PlayerInformationold_on.online_player.performCommand("/Briefkasten delete verify")) {
                    PlayerInformationold_on.config.set("Event.Chest.location", null);
                    save(PlayerInformationold_on.config,PlayerInformationold_on.online_player.getUniqueId().toString());
                    success = true;
                    timer = false;
                }
                else wait(5000);
            }
            else timer = false;
        }
        if(!success) PlayerInformationold_on.online_player.sendMessage(ChatColor.RED + "[Falkenturm] " + "Anfrage abgebrochen");

    }

    private void nameChange(Player player, int i) throws IOException, InvalidConfigurationException {
        PlayerInformationold_on = new PlayerInformationold(player);
        if(PlayerInformationold_on.config.getString("Event.Names." + i) == null){
            PlayerInformationold_on.config.set("Event.Names.active_Name", i);
            PlayerInformationold_on.config.set("Event.Names." + i, online_player.getName());
            save(PlayerInformationold_on.config, PlayerInformationold_on.online_player.getUniqueId().toString());
        }
        else {
            if (Objects.equals(PlayerInformationold_on.config.getString("Event.Names." + i), PlayerInformationold_on.online_player.getName())) {
                if(!Objects.equals(PlayerInformationold_on.config.getInt("Event.Names.active_Name"),i)){
                    PlayerInformationold_on.config.set("Event.Names.active_Name", i);
                    save(PlayerInformationold_on.config, PlayerInformationold_on.online_player.getUniqueId().toString());
                }
            }
            else nameChange(PlayerInformationold_on.online_player,i+1);
        }
    }

    public void setJoinConfig(Player player, String jointime) throws IOException, InvalidConfigurationException {
        PlayerInformationold_on = new PlayerInformationold(player);
        if(PlayerInformationold_on.config.getString("Event.Firstlogin") == null){
            PlayerInformationold_on.config.set("Event.Firstlogin", jointime);
        }
        PlayerInformationold_on.config.set("Event.LastLogin", jointime);
        if (PlayerInformationold_on.config.getString("Event.LastLogout") == null) PlayerInformationold_on.config.set("Event.LastLogout", 0);
        if (PlayerInformationold_on.config.getString("Event.Names.Info.Banned") == null) PlayerInformationold_on.config.set("Event.Names.Info.Banned", false);
        else if(PlayerInformationold_on.config.getBoolean("Event.Names.Info.Banned")) PlayerInformationold_on.config.set("Event.Names.Info.Banned", false);
        nameChange(PlayerInformationold_on.online_player,1);
        if (PlayerInformationold_on.config.getString("Event.Chest.set") == null) PlayerInformationold_on.config.set("Event.Chest.set", false);
        save(PlayerInformationold_on.config, PlayerInformationold_on.online_player.getUniqueId().toString());
    }

    public void setQuitConfig(Player player, String leavetime) throws IOException, InvalidConfigurationException {
        PlayerInformationold_on = new PlayerInformationold(player);
        PlayerInformationold_on.config.set("Event.LastLogout", leavetime);
        save(PlayerInformationold_on.config, PlayerInformationold_on.online_player.getUniqueId().toString());
    }

    public void setBanConfig(Player player, String leavetime) throws IOException, InvalidConfigurationException {
        PlayerInformationold_on = new PlayerInformationold(player);
        PlayerInformationold_on.config.set("Event.LastLogout", leavetime);
        PlayerInformationold_on.config.set("Event.Names.Info.Banned", true);
        save(PlayerInformationold_on.config, PlayerInformationold_on.online_player.getUniqueId().toString());
    }

    public static void chestUse(Player player) throws IOException, InvalidConfigurationException, InterruptedException {
        //TODO Kisteninteractevent funktioniert nicht
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = now;
        time = time.plusMinutes(1);
        while (LocalDateTime.now().isBefore(time)) {

        }
    }

    public void setChestLocation(Player player, Location chestlocation) throws IOException, InvalidConfigurationException {
        PlayerInformationold_on = new PlayerInformationold(player);

        PlayerInformationold_on.config.set("Event.Chest.set",true);
        PlayerInformationold_on.config.set("Event.Chest.location",chestlocation);
        save(PlayerInformationold_on.config, PlayerInformationold_on.online_player.getUniqueId().toString());
    }

    public Location getChestLocation(Player player) throws IOException, InvalidConfigurationException {
        PlayerInformationold_on = new PlayerInformationold(player);
        HashMap<Player, Location> chestlocation = new HashMap<>();
        if(PlayerInformationold_on.config.getBoolean("Event.Chest.set")) {
            Location standord = PlayerInformationold_on.config.getLocation("Event.Chest.location");
            chestlocation.put(PlayerInformationold_on.online_player, standord);
            return standord;
        }
        else {
            return null;
        }
    }
}
