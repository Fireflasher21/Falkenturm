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

import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.Fireflasher.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Flagaccess extends JavaPlugin {


        private WorldGuardPlugin getWorldGuard() {
                Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

                // WorldGuard may not be loaded
                if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
                        return null; // Maybe you want throw an exception instead
                }

                return (WorldGuardPlugin) plugin;
        }

        public static boolean inRegion(Player player) {

                //
                LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                Location location = localPlayer.getLocation();
                Vector3 vector3 = location.toVector();
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager regionManager = container.get(localPlayer.getWorld());
                ApplicableRegionSet applicableRegionSet = regionManager.getApplicableRegions(vector3.toBlockPoint());


                Flag flag = null;
                StateFlag.State allow = StateFlag.State.ALLOW;

                //if (WorldGuard.getInstance().getPlatform().getRegionContainer().get((World) player.getWorld()).getApplicableRegions()) {
                        for (ProtectedRegion regions : applicableRegionSet) {
                                if (regions.getFlag(Main.FALKENTURM).equals(allow)) {
                                        return true;
                                }

                        }
                //}
                return false;
        }

}//end Flagacces class