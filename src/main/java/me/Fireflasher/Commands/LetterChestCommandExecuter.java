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
import me.Fireflasher.HashMaps.AddTimer;
import me.Fireflasher.HashMaps.ChestLocation;
import me.Fireflasher.HashMaps.DeleteTimer;
import me.Fireflasher.Main;
import me.Fireflasher.WorldguardIntegration.Flagaccess;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LetterChestCommandExecuter implements CommandExecutor {

    private Player player;
    private final ResponseConfig RESPONSECONFIG = Main.getInstance().responseConfig;
    private final DefaultConfig DEFAULTCONFIG = Main.getInstance().defaultConfig;

    //config
    private final boolean verify = DEFAULTCONFIG.getConfig().getBoolean("Falkenturm.verify");
    private final String perm_help = RESPONSECONFIG.getConfig().getString("Response.Messages.No_Permission.help");
    private final String perm_add = RESPONSECONFIG.getConfig().getString("Response.Messages.No_Permission.add");
    private final String perm_delete = RESPONSECONFIG.getConfig().getString("Response.Messages.No_Permission.delete");
    private final String perm_verify = RESPONSECONFIG.getConfig().getString("Response.Messages.No_Permission.verify");
    private final String perm_send = RESPONSECONFIG.getConfig().getString("Response.Messages.No_Permission.send");
    private final String perm_default = RESPONSECONFIG.getConfig().getString("Response.Messages.No_Permission.default");
    private final String verify_blocked = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.verify_blocked");

    public LetterChestCommandExecuter() {}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String default_string = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.default");
        //Commandreihe
        if (cmd.getName().equalsIgnoreCase("Briefkasten") ) {

            if (sender instanceof Player player) {
                if (player.hasPermission("Briefkasten")) {
                    switch (args.length) {
                        case 0:
                            return ResponseConfig.nullExecuter(player,default_string);

                        case 1:
                            if(args[0].equalsIgnoreCase("help")) {
                                return command_help(player);
                            }
                            else if(args[0].equalsIgnoreCase("add")) {
                                try {
                                    return command_add(player);
                                } catch (IOException | InvalidConfigurationException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(args[0].equalsIgnoreCase("delete")) {
                                try {
                                    return command_delete(player);
                                } catch (IOException | InvalidConfigurationException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(args[0].equalsIgnoreCase("verify")) {
                                try {
                                    return command_verify(player);
                                } catch (IOException | InvalidConfigurationException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(args[0].equalsIgnoreCase("send")) {
                                return command_send(player);
                            }
                            else {
                                return ResponseConfig.nullExecuter(player,default_string);
                            }

                        case 2:
                            if(args[0].equals("help")) {
                                return command_help(player, args[1]);
                            }
                            else if(args[0].equals("delete")){
                                String add_nopending = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.add_nopending");
                                if(args[1].equals("verify")) {
                                    if (DeleteTimer.getInstance().getCommand(player)) {
                                        try {
                                            new PlayerInformation().deleteConfig(player);
                                            return true;
                                        } catch (IOException | InvalidConfigurationException | InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        return ResponseConfig.nullExecuter(player, add_nopending);
                                    }
                                }else if(args[1].equals("decline")) {
                                    if (DeleteTimer.getInstance().getCommand(player)) {
                                        DeleteTimer.getInstance().delTime(player);
                                        player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Deine Anfrage wurde gelöscht");
                                        return true;
                                    }else {
                                        return ResponseConfig.nullExecuter(player, add_nopending);
                                    }
                                }
                                else{
                                    String help_default = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.default");
                                    return ResponseConfig.nullExecuter(player,help_default);
                                    }
                            }
                            else if(args[0].equalsIgnoreCase("send")) {
                                try {
                                    return command_send(player,args[1],0);
                                } catch (IOException | InvalidConfigurationException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                return ResponseConfig.nullExecuter(player,default_string);
                            }

                        default:
                            return ResponseConfig.nullExecuter(player,default_string);
                    }//switch args.length

                }//Briefkasten Befehl Berechtigung
                else{
                    return ResponseConfig.nullExecuter(player,perm_default);
                }

            }//Ausführung durch User
            else {
                String onlyplayer = RESPONSECONFIG.getConfig().getString("Response.Messages.No_Permission.onlyplayer");
                return ResponseConfig.nullExecuter(player, onlyplayer);
            }

        }//end Commandreihe
        else return false;
    }//end public method OnCommand

    private boolean command_help(Player player) {
        this.player = player;
        if (player.hasPermission("Briefkasten.help")) {
            player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.DARK_RED + "Befehle:");


            player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.BLUE + "/Briefkasten " + ChatColor.GREEN + "help " + ChatColor.DARK_RED + "Befehl" );
            if (player.hasPermission("Briefkasten.add")) {
                player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.BLUE + "/Briefkasten " + ChatColor.GREEN + "add");
            }
            if (player.hasPermission("Briefkasten.delete")) {
                player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.BLUE + "/Briefkasten " + ChatColor.GREEN + "delete");
            }
            if (player.hasPermission("Briefkasten.verify")) {
                if (verify) {
                    player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.BLUE + "/Briefkasten " + ChatColor.GREEN + "verify");
                }
            }
            if (player.hasPermission("Briefkasten.send")) {
                player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.BLUE + "/Briefkasten " + ChatColor.GREEN + "send");
            }
            player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Du kannst auch: " + ChatColor.BLUE +"/Bk " + ChatColor.RESET + "benutzen");
            return true;
        }
        else {
            return ResponseConfig.nullExecuter(player, perm_help);
        }
    }//end private method command_help

    private boolean command_help(Player player, String command) {
        this.player = player;
        String help_add = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.add");
        String help_verify_add = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.verify_add");
        String help_delete = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.delete");
        String help_verify = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.verify");
        String help_verify_send = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.verify_send");
        String help_send = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.send");
        String help_default = RESPONSECONFIG.getConfig().getString("Response.Messages.Help.default");

        switch (command) {
            case "add" -> {
                if (player.hasPermission("Briefkasten.add")) {
                    if (verify) {
                        return ResponseConfig.nullExecuter(player,help_verify_add);
                    }
                    else{
                        return ResponseConfig.nullExecuter(player,help_add);
                    }
                }
                else {
                    return ResponseConfig.nullExecuter(player,perm_add);
                }
            }
            case "delete" -> {
                if (player.hasPermission("Briefkasten.delete")) {
                    return ResponseConfig.nullExecuter(player,help_delete);
                }
                else {
                    return ResponseConfig.nullExecuter(player,perm_delete);
                }
            }
            case "verify" -> {
                if (player.hasPermission("Briefkasten.verify")) {
                    if (verify) {
                        return ResponseConfig.nullExecuter(player,help_verify);
                    }
                    else {
                        return ResponseConfig.nullExecuter(player,verify_blocked);
                    }
                }
                else {
                    return ResponseConfig.nullExecuter(player,perm_verify);
                }
            }
            case "send" -> {
                if (player.hasPermission("Briefkasten.send")) {
                    if (verify) {
                        return ResponseConfig.nullExecuter(player,help_verify_send);
                    }
                    else {
                        return ResponseConfig.nullExecuter(player,help_send);
                    }

                }
                else {
                    return ResponseConfig.nullExecuter(player,perm_send);
                }
            }
            default -> {
                return ResponseConfig.nullExecuter(player,help_default);
            }
        }//switch args[1]

    }//end private method command_help_sub

    private boolean command_add(Player player) throws IOException, InvalidConfigurationException, InterruptedException {
        this.player = player;
        String add = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.add");
        String add_false = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.add_false");
        String add_pending = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.add_pending");

        if (player.hasPermission("Briefkasten.add")) {
            if (AddTimer.getInstance().getnull(player)) {
                if(!ChestLocation.getInstance().getTime(player)) {
                    AddTimer.getInstance().setTime(player);
                    String nullexecuter = RESPONSECONFIG.getConfig().getString("Response.Messages.No_Permission.null");
                    if (new PlayerInformation(player).getConfig(player).getBoolean("Event.Chest.set")) {
                        AddTimer.getInstance().delTime(player);
                        return ResponseConfig.nullExecuter(player, add_false);
                    } else {
                        ResponseConfig.nullExecuter_void(player, add);
                        return true;
                    }
                }
            }
            return ResponseConfig.nullExecuter(player, add_pending);
        }
        else return ResponseConfig.nullExecuter(player, perm_add);

    }//end private method command_add

    private boolean command_delete(Player player) throws IOException, InvalidConfigurationException, InterruptedException {
        this.player = player;
        String delete_verify1 = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.delete_verify1");

        if (player.hasPermission("Briefkasten.delete")) {
            if(!new PlayerInformation().getConfig(player).getBoolean("Event.Chest.set")){
                player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Du hast keinen Briefkasten registriert");
            }
            else {
                ResponseConfig.nullExecuter_void(player,delete_verify1);
                DeleteTimer.getInstance().setTime(player);
                DeleteTimer.getInstance().setCommand(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&4[Falkenturm]&r Bestätige mit &9/Briefkasten &adelete verify"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&4[Falkenturm]&r Breche mit &9/Briefkasten &adelete decline &rab"));
                return true;
            }
            return true;
        } else {
            return ResponseConfig.nullExecuter(player,perm_delete);
        }
    }//end private method command_delete

    private boolean command_verify(Player player) throws IOException, InvalidConfigurationException {
        this.player = player;
        String verify_false = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.verify_false");

        if(player.hasPermission("Briefkasten.verify")) {
            if (verify) {

                if (Flagaccess.inRegion(player)) {
                    if (ChestLocation.getInstance().getTime(player)) {
                        new PlayerInformation().setChestLocation(player, ChestLocation.getInstance().getLocation(player));
                        return true;
                    }
                } else {
                    return ResponseConfig.nullExecuter(player, verify_false);
                }
                return true;
            }//Configabfrage ob Regionprotect aktiv oder nicht
            else {
                return ResponseConfig.nullExecuter(player, verify_blocked);
            }
        }
        else {
            return ResponseConfig.nullExecuter(player,perm_verify);
        }
    }//end private method command_verify

    private boolean command_send(Player player){
        String send_false_np = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.send_false_np");
        this.player = player;
        if(player.hasPermission("Briefkasten.send")) {
                return ResponseConfig.nullExecuter(player, send_false_np);
        }
        else{
            return ResponseConfig.nullExecuter(player,perm_send);
        }
    }

    private boolean command_send(Player player, String playerstring, int online) throws IOException, InvalidConfigurationException, InterruptedException {
        String send_false_pl = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.send_false_pl");
        String send_no_chest = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.send_no_chest");



        this.player = player;
        if(player.hasPermission("Briefkasten.send")) {
            boolean setChest = false;
            boolean chest_exist = false;
            switch (online){//Construktor für online
                case 1:
                    Player player2 = Bukkit.getPlayer(playerstring);
                    setChest = new PlayerInformation().getConfig(player2).getBoolean("Event.Chest.set"); // Abfrage ob Kiste vorhanden
                    if(setChest) chest_exist = ChestLocation.getInstance().isChest(player2.getUniqueId().toString()); // Abfrage ob Kiste noch existiert
                    break;
                case 2:
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerstring);
                    String offlineplayer_id = offlinePlayer.getUniqueId().toString();
                    setChest = new PlayerInformation().getConfig(offlineplayer_id).getBoolean("Event.Chest.set"); // Abfrage ob Kiste vorhanden
                    if(setChest) chest_exist = ChestLocation.getInstance().isChest(offlinePlayer.getUniqueId().toString()); // Abfrage ob Kiste noch existiert
                    break;
                default:
                    if (Bukkit.getPlayer(playerstring) != null) {
                        return command_send(player, playerstring, 1);
                    }
                    else {
                        OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(playerstring);
                        File playeruuid = new File("plugins/Falkenturm/Player", offlineplayer.getUniqueId()+ ".yml" );
                        if (playeruuid.exists()) return command_send(player, playerstring, 2);
                        else {
                            return ResponseConfig.nullExecuter(player,send_false_pl);
                        }
                    }
            }
            if(setChest){
                if(chest_exist){
                    if(!DEFAULTCONFIG.getConfig().getBoolean("Falkenturm.Letter.to_worlds")) {
                        if(new PlayerInformation().getChestLocation(Bukkit.getOfflinePlayer(playerstring).getUniqueId().toString()).getWorld() != player.getWorld()){
                            player.sendMessage(ChatColor.DARK_RED + "[Falkenturm] " + ChatColor.RESET + "Der Briefkasten ist außer Reichweite");
                            return true;
                        }
                    }
                    return send_brief(player, playerstring);
                }
                else {
                    if( online == 1) new PlayerInformation().deleteConfig(Bukkit.getPlayer(playerstring),1);
                    if( online == 2) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerstring);
                        FileConfiguration con = new PlayerInformation().getConfig(offlinePlayer.getUniqueId().toString());
                        con.set("Event.Chest.location", null);
                        con.set("Event.Chest.set", false);
                        con.save(offlinePlayer.getUniqueId().toString());
                    }
                    return ResponseConfig.nullExecuter(player,send_no_chest);
                }
            }
            else {
                return ResponseConfig.nullExecuter(player,send_no_chest);
            }
        }
        else{
            return ResponseConfig.nullExecuter(player,perm_send);
        }
    }//end private method command_send

    private boolean send_brief(Player player, String playerstring) throws IOException, InvalidConfigurationException {
        String send_true = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.send_true");
        String send_false = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.send_false");
        String send_chest_space = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.send_chest_space");
        String send_no_book = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.send_no_book");
        String send_empty_book = RESPONSECONFIG.getConfig().getString("Response.Messages.Ausgabe.send_empty_book");

        int brief = 0;
        if (player.getInventory().getItemInMainHand().getType().equals(Material.WRITTEN_BOOK))  brief = 1;
        if (player.getInventory().getItemInMainHand().getType().equals(Material.WRITABLE_BOOK))  brief = 2;
        if ( brief == 0) return ResponseConfig.nullExecuter(player, send_no_book);

        Block chest_block = new PlayerInformation().getChestLocation(Bukkit.getOfflinePlayer(playerstring).getUniqueId().toString()).getBlock();
        InventoryHolder chest_inventory = (InventoryHolder)chest_block.getState();
        int slot = chest_inventory.getInventory().firstEmpty();
        if(slot >= 0) {

            ItemStack brief_sender = player.getInventory().getItemInMainHand();
            BookMeta brief_e_meta = (BookMeta) brief_sender.getItemMeta();
            if(!verify || Flagaccess.inRegion(player)) {
                if (brief == 1) {
                    if (DEFAULTCONFIG.getConfig().getBoolean("Falkenturm.Letter.change_author")) {
                        brief_e_meta.setAuthor("???");
                    }
                } else if (brief == 2) {
                    if (DEFAULTCONFIG.getConfig().getBoolean("Falkenturm.Letter.close")) {
                        List<String> pages = brief_e_meta.getPages();
                        if (pages.isEmpty()) {
                            return ResponseConfig.nullExecuter(player, send_empty_book);
                        }
                        brief_sender.setType(Material.WRITTEN_BOOK);
                        brief_e_meta.setPages(pages);
                        if (brief_e_meta.getTitle() == null || brief_e_meta.getTitle().isEmpty()) {
                            brief_e_meta.setTitle("Brief");
                        }
                        if (DEFAULTCONFIG.getConfig().getBoolean("Falkenturm.Letter.change_author")) {
                            brief_e_meta.setAuthor("???");
                        } else brief_e_meta.setAuthor(player.getName());
                    }
                }
                player.getInventory().remove(brief_sender);
                brief_sender.setItemMeta(brief_e_meta);
                chest_inventory.getInventory().addItem(brief_sender);
                Main.getInstance().getLogger().info("[Falkenturm] Spieler: " + player.getName() + " hat einen Brief an " + playerstring + " gesendet");
                return ResponseConfig.nullExecuter(player, send_true);
            }//end Worldregiontest verify
            else return ResponseConfig.nullExecuter(player, send_false);

            /*
              if (Flagaccess.inRegion(player)) {
                    player.getInventory().remove(brief_sender);
                    brief_sender.setItemMeta(brief_e_meta);
                    chest_inventory.getInventory().addItem(brief_sender);
                    Main.getInstance().getLogger().info("[Falkenturm] Spieler: " + player.getName() + " hat einen Brief an " + playerstring + " gesendet");
                    return ResponseConfig.nullExecuter(player, send_true);
                }else {
                player.getInventory().remove(brief_sender);
                brief_sender.setItemMeta(brief_e_meta);
                chest_inventory.getInventory().addItem(brief_sender);
                Main.getInstance().getLogger().info("[Falkenturm] Spieler: " + player.getName() + " hat einen Brief an " + playerstring + " gesendet");
                return ResponseConfig.nullExecuter(player, send_true);
                }*/
        }else return ResponseConfig.nullExecuter(player, send_chest_space);
    }



}//end of class