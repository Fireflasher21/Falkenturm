package me.Fireflasher.HashMaps;

import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.HashMap;

public class DeleteTimer {

    private static DeleteTimer deletetimer;
    private final HashMap<Player, LocalDateTime> times;
    private final HashMap<Player, Boolean> command_verify;

    public DeleteTimer(){
        deletetimer = this;
        times = new HashMap<Player, LocalDateTime>();
        command_verify = new HashMap<Player, Boolean>();
    }

    public static DeleteTimer getInstance(){
        if (deletetimer != null){
            return deletetimer;
        }
        return new DeleteTimer();
    }

    public void setTime(Player player){
        LocalDateTime now = LocalDateTime.now();
        deletetimer.times.put(player, now);
    }

    public void setCommand(Player player){
        deletetimer.command_verify.put(player, true);
    }

    public void delTime(Player player) {
            if (!deletetimer.times.isEmpty()) deletetimer.times.remove(player, deletetimer.times.get(player));
            if (!deletetimer.command_verify.isEmpty()) deletetimer.command_verify.remove(player, true);
    }

    public boolean getTime(Player player){
        LocalDateTime now = LocalDateTime.now();
        if (!deletetimer.times.isEmpty()) {
            return deletetimer.times.get(player).plusMinutes(1).isAfter(now);
        }
        return false;
    }

    public boolean getCommand(Player player){
        if (!deletetimer.command_verify.isEmpty()) {
            return deletetimer.command_verify.get(player);
        }
        return false;
    }
}
