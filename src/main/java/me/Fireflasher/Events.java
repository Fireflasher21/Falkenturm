package me.Fireflasher;

import me.Fireflasher.Configs.PlayerInformation;
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
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
            if(AddTimer.getInstance().getTime(player)) {
                if (event.getClickedBlock().getType() == Material.CHEST) ChestLocation.getInstance().setLocation(player, event.getClickedBlock().getLocation());
                else {
                    player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " +ChatColor.RESET + "Bitte wähle eine Kiste als Briefkasten");
                    AddTimer.getInstance().delTime(player);
                }
            }
        }
    }



}
