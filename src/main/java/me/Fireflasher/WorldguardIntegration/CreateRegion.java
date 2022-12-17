package me.Fireflasher.WorldguardIntegration;

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
import me.Fireflasher.Configs.ResponseConfig;
import me.Fireflasher.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CreateRegion {


    private static final ResponseConfig RESPONSECONFIG = Main.getInstance().responseConfig;

    public static boolean onCommand(Player player, String[] args){
        final String create_false = RESPONSECONFIG.getConfig().getString("Response.Messages.Region.create_false");
        final String create_true = RESPONSECONFIG.getConfig().getString("Response.Messages.Region.create_true.message");
        final String create_true_name = RESPONSECONFIG.getConfig().getString("Response.Messages.Region.create_true.message.name");
        final String create_true_cords = RESPONSECONFIG.getConfig().getString("Response.Messages.Region.create_true.message.coordinates");
        final String create_true_radius = RESPONSECONFIG.getConfig().getString("Response.Messages.Region.create_true.message.radius");
        final String create_error = RESPONSECONFIG.getConfig().getString("Response.Messages.Region.create_error");
        final String help_region = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.region");

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
                        player.sendMessage(create_false);
                        return  true;
                    }else{
                        regionManager.addRegion(region);
                        player.sendMessage(create_true + "\n" + create_true_name + " " + regionname + "\n" + create_true_radius + " " + radius + "\n" + create_true_cords + " " + location.getBlockX() + "," + location.getBlockZ());
                        return  true;
                    }

                }
                else player.sendMessage(create_error + " " + MIN + " - " + MAX)    ;
            }
        }else {
            player.sendMessage(help_region);
            return true;
        }
        return false;
    }

    public static boolean setFlag(Player player, String[] args) {
        final String activate = RESPONSECONFIG.getConfig().getString("Response.Messages.Region.setflag_true");
        final String deactivate = RESPONSECONFIG.getConfig().getString("Response.Messages.Region.setflag_false");
        final String help_values = RESPONSECONFIG.getConfig().getString("Response.Messages.Region.setflag_values");
        final String setflag_error = RESPONSECONFIG.getConfig().getString("Response.Messages.Region.setflag_error");
        final String help_setflag = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.setflag");
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
                        player.sendMessage( activate + " " + regionname );
                        player.getServer().getLogger().log(Level.INFO, activate + " " + regionname);
                    }
                    else {
                        region.setFlag(Main.FALKENTURM, StateFlag.State.DENY);
                        player.sendMessage(deactivate + " " + regionname);
                        player.getServer().getLogger().log(Level.INFO, deactivate + " " + regionname);
                    }
                    return true;
                }
                else {
                    player.sendMessage(help_values);
                    return true;
                }
            }else {
                player.sendMessage(setflag_error);
                return true;
            }
        }
        player.sendMessage(help_setflag);
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
