package me.Fireflasher.WorldguardIntegration;

import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
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


/*
                Flag poststelle = true;

                if (WorldGuard.getInstance().getPlatform().getRegionContainer().get().getApplicableRegions().getRegions().)
                for(ProtectedRegion regions : applicableRegionSet){
                        if ( regions.contains(vector3.toBlockPoint())){
                                if (regions.a){
                                        return true;
                                }
                        }
                }
                return false;
                */
                return false;
        }

}//end Flagacces class