package me.Fireflasher.WorldguardIntegration;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.Fireflasher.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CreateRegion {

    public static boolean onCommand(Player player, String[] args){
        if(args.length == 4) {
            if (args[1].equalsIgnoreCase("region")) {
                //da Radius ungerade Zahlen
                final int MIN = 3;
                final int MAX = 9;
                if (parseInt(args[3]) && Integer.parseInt(args[3]) >= MIN && Integer.parseInt(args[3]) <= MAX ){
                    String regionname = args[2];
                    int radius = Integer.parseInt(args[3]);

                    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                    Location location = localPlayer.getLocation();
                    int[] minint = new int[2];
                    int[] maxint = new int[2];
                    minint[0] = location.getBlockX() - radius;
                    minint[1] = location.getBlockZ() - radius;
                    maxint[0] = location.getBlockX() + radius;
                    maxint[1] = location.getBlockZ() + radius;
                    BlockVector3 min = BlockVector3.at(minint[0], 0, minint[1]);
                    BlockVector3 max = BlockVector3.at(maxint[0], 256, maxint[1]);
                    ProtectedRegion region = new ProtectedCuboidRegion(regionname, min, max);
                    region.setFlag(Main.FALKENTURM, StateFlag.State.ALLOW);

                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionManager regionManager = container.get(localPlayer.getWorld());
                    if(region == null){
                        player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Die Region konnte nicht erstellt werden");
                        return  true;
                    }else{
                        regionManager.addRegion(region);
                        player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Die Region " + regionname + " wurde an mit einem Radius von " + radius + " Blöcken um X: " + location.getBlockX() + " und Y: " + location.getBlockZ() + " erstellt");
                        return  true;
                    }

                }
                else player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Radius muss eine Zahl zwischen " + MIN + " und "+ MAX + " sein");
            }
        }else {
            player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Bitte benutze " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "create region " + ChatColor.RED + "REGIONSNAME " + ChatColor.AQUA + "Radius");
            return true;
        }
        return false;
    }

    public static boolean setFlag(Player player, String[] args) {
        if (args.length == 3) {
            String regionname = args[1];

            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regionManager = container.get(localPlayer.getWorld());
            if(regionManager.hasRegion(regionname)){
                ProtectedRegion region = regionManager.getRegion(regionname);
                String value = args[2];
                if(parseboolean(value)){
                    if(parseboolean(value)){
                        region.setFlag(Main.FALKENTURM, StateFlag.State.ALLOW);
                        player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Poststelle in der Region " + ChatColor.GREEN + regionname + ChatColor.RESET + " wurde deaktiviert");
                        player.getServer().getLogger().log(Level.INFO, "[Falkenturm] Poststelle in der Region " + regionname + " wurde deaktiviert");
                    }
                    else {
                        region.setFlag(Main.FALKENTURM, StateFlag.State.DENY);
                        player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Poststelle in der Region " + ChatColor.GREEN + regionname + ChatColor.RESET + " wurde aktiviert");
                        player.getServer().getLogger().log(Level.INFO, "[Falkenturm] Poststelle in der Region " + regionname + " wurde aktiviert");
                    }
                    return true;
                }
                else {
                    player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Gib als Wert bitte " + ChatColor.RED + "true/false " +ChatColor.RESET + "an");
                    return true;
                }
            }else {
                player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Region konnten nicht gefunden werden");
                return true;
            }
        }
        player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Bitte benutze " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "setFlag" + ChatColor.RED + "REGIONSNAME " + ChatColor.AQUA + "true/false");
        return true;
    }

    private static boolean parseInt(String integer){
        try {
            Integer.parseInt(integer);
            return true;
        }catch (NumberFormatException nfe){
            return false;
        }
    }
    private static boolean parseboolean(String value){
        try {
            Boolean.parseBoolean( value);
            return true;
        }catch (ClassCastException cce){
            return false;
        }
    }

}
