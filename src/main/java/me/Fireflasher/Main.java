package me.Fireflasher;

import com.sk89q.worldguard.protection.flags.StateFlag;
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

    public static StateFlag POSTSTELLE;
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

    /*
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "poststelle", defaulting to false
            StateFlag flag = new StateFlag("poststelle", false);
            registry.register(flag);
            POSTSTELLE = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("poststellle");
            if (existing instanceof StateFlag) {
                POSTSTELLE = (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
            }
        }

     */

        getLogger().info("Falkenturm ist aktiv");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.WARNING,"Falkenturm ist nicht aktiv");
    }

    public static Main getInstance(){
        return instance;
    }


}//end main class

