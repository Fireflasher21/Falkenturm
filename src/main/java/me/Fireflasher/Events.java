package me.Fireflasher;

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

import me.Fireflasher.Configs.PlayerInformation;
import me.Fireflasher.Configs.ResponseConfig;
import me.Fireflasher.HashMaps.AddTimer;
import me.Fireflasher.HashMaps.ChestLocation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Events implements Listener {

    public static Plugin plugin;
    private static final ResponseConfig RESPONSECONFIG = Main.getInstance().responseConfig;

    public Events(Plugin plugin){
        Events.plugin = plugin;
    }

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) throws IOException, InvalidConfigurationException {
        Player player = (Player) event.getPlayer();
        String jointime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy " + "kk:mm"));
        new PlayerInformation(player).setJoinConfig(player, jointime);
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event) throws IOException, InvalidConfigurationException {
        Player player = (Player) event.getPlayer();
        String leavetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy " + "kk:mm"));
        new PlayerInformation(player).setQuitConfig(player, leavetime);
    }

    @EventHandler
    public static void onKick(PlayerKickEvent event) throws IOException, InvalidConfigurationException {
        Player player = (Player) event.getPlayer();
        String leavetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy " + "kk:mm"));
        if(player.isBanned()) new PlayerInformation(player).setBanConfig(player, leavetime);
        else new PlayerInformation(player).setQuitConfig(player, leavetime);
    }

    @EventHandler
    public static void onPlayerInteractEvent(PlayerInteractEvent event) throws IOException, InterruptedException, InvalidConfigurationException {
        String addChest = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.add");

        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
            if(AddTimer.getInstance().getTime(player)) {
                if (event.getClickedBlock().getType() == Material.CHEST) ChestLocation.getInstance().setLocation(player, event.getClickedBlock().getLocation());
                else {
                    player.sendMessage(addChest);
                    AddTimer.getInstance().delTime(player);
                }
            }
        }
    }

}
