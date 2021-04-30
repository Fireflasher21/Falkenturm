package me.Fireflasher.HashMaps;

import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AddTimer {

    private static AddTimer addtimer;
    private final HashMap<Player, LocalDateTime> times;

    public AddTimer(){
        addtimer = this;
        times = new HashMap<Player, LocalDateTime>();
    }

    public static AddTimer getInstance(){
        if (addtimer != null){
            return addtimer;
        }
        return new AddTimer();
    }

    public void setTime(Player player){
        delTime();
        LocalDateTime now = LocalDateTime.now();
        addtimer.times.put(player, now);
    }

    private void delTime() {
        LocalDateTime now = LocalDateTime.now();
        if (!addtimer.times.isEmpty()) {
            for (Map.Entry<Player, LocalDateTime> entry : addtimer.times.entrySet()) {
                if (entry.getValue().plusMinutes(1).isBefore(now)) {
                    addtimer.times.remove(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void delTime(Player player){
        addtimer.times.remove(player, addtimer.times.get(player));

    }

    public boolean getTime(Player player){
        delTime();
        LocalDateTime now = LocalDateTime.now();
        if (!addtimer.times.isEmpty()) {
            return addtimer.times.get(player).plusMinutes(1).isAfter(now);
        }
        return false;
    }

    public boolean getnull(Player player){
        delTime();
        return addtimer.times.isEmpty();
    }
}
