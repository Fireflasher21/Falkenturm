package me.Fireflasher.HashMaps;

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
        addtimer.times.clear();

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
        delTime(player);
        return addtimer.times.isEmpty();
    }
}
