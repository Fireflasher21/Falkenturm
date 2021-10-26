package me.Fireflasher.HashMaps;

import me.Fireflasher.Configs.DefaultConfig;
import me.Fireflasher.Configs.PlayerInformation;
import me.Fireflasher.Configs.ResponseConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class ChestLocation {

    private final boolean verify = new DefaultConfig().getConfig().getBoolean("Falkenturm.verify");

    private static ChestLocation chestLocation;
    private final HashMap<Player, Location> maplocation;
    private final HashMap<Player, LocalDateTime> chesttimer;

    public ChestLocation(){
        chestLocation = this;
        maplocation = new HashMap<Player, Location>();
        chesttimer = new HashMap<Player, LocalDateTime>();
    }

    public static ChestLocation getInstance(){
        if (chestLocation != null){
            return chestLocation;
        }
        return new ChestLocation();
    }

    public void setLocation(Player player, Location location) throws InterruptedException, IOException, InvalidConfigurationException {
        chestLocation.maplocation.put(player, location);
        final String verify_add = new ResponseConfig().getConfig().getString("Response.Messages.Help.verify_add");
        if( new DefaultConfig().getConfig().getBoolean("Falkenturm.verify")) {

            if (player.hasPermission("Briefkasten.verify")) {

                chestLocation.chesttimer.put(player,LocalDateTime.now());
                player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET +  "Du hast nun eine Stunde Zeit um deinen Briefkasten in der Poststelle zu registrieren");
                player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET +  "Du kannst Ihn mit " + ChatColor.BLUE + "/bk " + ChatColor.GREEN + " verify " + ChatColor.RESET + "in der Postelle registrieren");

            }
            else player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RED + "Du bist nicht berechtigt eine Kiste zu registrieren. Wende dich an einen Admin");
        }
        else {
            new PlayerInformation().setChestLocation(player, location);
        }
    }

    public void delLocation(Player player) {
        if (!chestLocation.maplocation.isEmpty()) chestLocation.maplocation.remove(player, chestLocation.maplocation.get(player));

        if (!chestLocation.chesttimer.isEmpty() ) chestLocation.chesttimer.remove(player, chestLocation.chesttimer.get(player));
    }

    public Location getLocation(Player player){
        if (!chestLocation.maplocation.isEmpty()) {
            return chestLocation.maplocation.get(player);
        }
        return null;
    }

    public boolean getTime(Player player) {
        LocalDateTime now = LocalDateTime.now();
        if (!chestLocation.chesttimer.isEmpty()) {
            return chestLocation.chesttimer.get(player).plusHours(1).isAfter(now);
        }
        return false;
    }

    public boolean isChest(String player_id) throws IOException, InvalidConfigurationException {
        Location chest = new PlayerInformation().getChestLocation(player_id);
        Block briefkasten = chest.getBlock();
        return briefkasten.getBlockData().getMaterial().equals(Material.CHEST);
    }
}

