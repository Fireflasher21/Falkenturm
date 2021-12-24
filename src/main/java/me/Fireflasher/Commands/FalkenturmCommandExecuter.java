package me.Fireflasher.Commands;

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
import me.Fireflasher.WorldguardIntegration.CreateRegion;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;

public class FalkenturmCommandExecuter implements CommandExecutor {

    public FalkenturmCommandExecuter() {}

    //Config
    private final boolean verify = new DefaultConfig().getConfig().getBoolean("Falkenturm.verify");
    private final String verify_blocked = new ResponseConfig().getConfig().getString("Response.Messages.Help.verify_blocked");
    private final String perm_help = new ResponseConfig().getConfig().getString("Response.Messages.No_Permission.help");
    private final String perm_default = new ResponseConfig().getConfig().getString("Response.Messages.No_Permission.default");

    @Override
    public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args) {
        //Falkenturm Commandreihe
        if (cmd.getName().equalsIgnoreCase("Falkenturm")) {
            if(sender.hasPermission("Falkenturm")) {
                if (args.length == 1) {
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
                        case "create":
                            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Bitte benutze " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "create region " + ChatColor.RED + "REGIONSNAME " + ChatColor.AQUA + "Radius");
                            return true;
                        case "setFlag":
                            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Benutze " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "addFlag" + ChatColor.RED + " Regionsname " + ChatColor.AQUA + "true/false " + ChatColor.RESET + "um die Region für eine Poststelle zu aktivieren") ;
                            return true;
                        default:
                            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Benutze bitte " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "help " + "für die Befehle");
                            return false;
                    }
                }else if(args.length >= 2) {
                    switch (args[0]) {
                        case "create":
                            if (sender instanceof Player player) {
                                if (verify) return CreateRegion.onCommand(player, args);
                                else return ResponseConfig.nullExecuter(player, verify_blocked);
                            } else {
                                sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Dieser Command kann nur von einem Spieler ausgeführt werden");
                                return true;
                            }
                        case "setFlag":
                            if (sender instanceof Player player) {
                                if (verify) return CreateRegion.setFlag(player, args);
                                else return ResponseConfig.nullExecuter(player, verify_blocked);
                            } else {
                                sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Dieser Command kann nur von einem Spieler ausgeführt werden");
                                return true;
                            }
                        default:
                            sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + "Benutze bitte " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "help " + "für die Befehle");
                            return true;
                    }
                }else{
                    sender.sendMessage("Für genauere Informationen benutze /Falkenturm help");
                    return true;
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
            if(sender.hasPermission("Falkenturm.region")){
                sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "create");
            }
            if(sender.hasPermission("Falkenturm.setFlag")){
                sender.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.BLUE + "/Falkenturm " + ChatColor.GREEN + "setFlag");
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