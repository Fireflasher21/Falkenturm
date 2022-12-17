package me.Fireflasher.HashMaps;

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

import me.Fireflasher.Configs.DefaultConfig;
import me.Fireflasher.Configs.PlayerInformation;
import me.Fireflasher.Configs.ResponseConfig;
import me.Fireflasher.Main;
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

    private final DefaultConfig DEFAULTCONFIG = Main.getInstance().defaultConfig;
    private final ResponseConfig RESPONSECONFIG = Main.getInstance().responseConfig;
    private final boolean verify = DEFAULTCONFIG.getConfig().getBoolean("Falkenturm.verify");

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
        final String add_timer = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.add_timer");
        final String verify_add = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.verify_add");
        chestLocation.maplocation.put(player, location);
        if(verify) {

            if (player.hasPermission("Briefkasten.verify")) {

                chestLocation.chesttimer.put(player,LocalDateTime.now());
                player.sendMessage(add_timer);
                player.sendMessage(verify_add);

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

