package me.Fireflasher.Commands;


import me.Fireflasher.Configs.DefaultConfig;
import me.Fireflasher.Configs.PlayerInformation;
import me.Fireflasher.Configs.ResponseConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class FalkenturmCommandExecuter implements CommandExecutor {

    public FalkenturmCommandExecuter() {}

    //Config
    private final String perm_help = new ResponseConfig().getConfig().getString("Response.Messages.No_Permission.help");
    private final String perm_default = new ResponseConfig().getConfig().getString("Response.Messages.No_Permission.default");

    @Override
    public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args) {
        //Falkenturm Commandreihe
        if (cmd.getName().equalsIgnoreCase("Falkenturm")) {
            if(sender.hasPermission("Falkenturm")) {
                if (args.length == 0) {
                    sender.sendMessage("Für genauere Informationen benutze /Falkenturm help ");
                    return true;
                }//Command
                else if (args.length == 1) {
                    switch (args[0]) {

                        case "help":
                            return command_help(sender);

                        case "version":
                            return command_version(sender);

                        case "info":
                            return command_info(sender);

                        case "reload":
                            try {
                                return command_reload(sender);
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                            }

                        default:
                            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + "Benutze bitte " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "help " + "für die Befehle");
                            return false;
                    }
                }
                else {
                    sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + "Benutze bitte " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "help " + "für die Befehle");
                    return false;
                }
            }//Falkenturm Berechtigung
            else {
                assert perm_default != null;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', perm_default));
                return false;
            }
        }//end Falkenturm Commandreihe
        else {
            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + " Benutze einen gültigen Command");
            return true;
        }
    }//end onCommand

    private boolean command_help(CommandSender sender) {
        if (sender.hasPermission("Falkemturm.help")) {
            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.DARK_RED + "Befehle:");

            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "help");
            if (sender.hasPermission("Falkenturm.version")) {
                sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "version");
            }
            if (sender.hasPermission("Falkenturm.info")) {
                sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "info");
            }
            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.BLUE + "/Briefkasten " + ChatColor.GREEN + "help");
            return true;
        }
        else {
            assert perm_help != null;
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', perm_help));
            return false;
        }
    }//end private method command_help

    private boolean command_version(CommandSender sender) {
        if (sender.hasPermission("Falkenturm.version")) {
            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.GREEN + "Version 1.0 Snapshot");
            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED + "Du hast keine Berechtigung für " + ChatColor.GREEN + "version");
            return false;
        }
    }//end private method command_version

    private boolean command_info(CommandSender sender) {
        if (sender.hasPermission("Falkenturm.info")) {
            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.GREEN + "Falkenturm ist ein Briefplugin welches dir Briefe von anderen an einen zuvor festgelten Briefkasten sendet");
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Du hast keine Berechtigung für " + ChatColor.GREEN + "info");
            return false;
        }
    }//end private method command_info

    private boolean command_reload(CommandSender sender) throws IOException, InvalidConfigurationException {
        if (sender.hasPermission("Falkenturm.reload")) {
            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.GREEN + "Wurde erfolgreich reloaded");
            new DefaultConfig().reloadConfig();
            new ResponseConfig().reloadConfig();
            new PlayerInformation().loadConfig();

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Du hast keine Berechtigung für " + ChatColor.GREEN + "reload");
            return false;
        }
    }//end private method command_reload

}//end of class