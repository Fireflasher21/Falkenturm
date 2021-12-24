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

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import me.Fireflasher.Commands.FalkenturmCommandExecuter;
import me.Fireflasher.Commands.LetterChestCommandExecuter;
import me.Fireflasher.Configs.DefaultConfig;
import me.Fireflasher.Configs.PlayerInformation;
import me.Fireflasher.Configs.ResponseConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Flag FALKENTURM;
    public DefaultConfig defaultConfig;
    public ResponseConfig responseConfig;
    private static Main instance;


    @Override
    public void onEnable() {
        Player player = null;
        instance = this;
        //Config
        try {
            this.defaultConfig = new DefaultConfig();
            this.responseConfig = new ResponseConfig();
            new PlayerInformation().loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        saveConfig();
        //CommandExecuter
        this.getCommand("Falkenturm").setExecutor(new FalkenturmCommandExecuter());
        this.getCommand("Briefkasten").setExecutor(new LetterChestCommandExecuter());
        Bukkit.getPluginManager().registerEvents(new Events(instance), instance);
        this.saveDefaultConfig();

        getLogger().info("Falkenturm ist aktiv");
    }

    @Override
    public void onLoad(){
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "falkenturm", defaulting to false
            StateFlag flag = new StateFlag("falkenturm", false);
            registry.register(flag);
            FALKENTURM = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("falkenturm");
            if (existing instanceof StateFlag) {
                FALKENTURM = (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.WARNING,"Falkenturm ist nicht aktiv");
    }

    public static Main getInstance(){
        return instance;
    }


}//end main class

