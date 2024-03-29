package io.Sonam.Game.Threads;

import io.Sonam.Game.SkyWars;
import io.Sonam.Game.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

    int count = 10;

    public void run() {
        if(count > 0) {
            count--;
            for(Player player : Bukkit.getOnlinePlayers()) {
                Utils.sendSubTitle(player, count + 1 + "", "red");
            }
            if(SkyWars.debug) Bukkit.broadcastMessage(count + 1 + "");
        } else {
            for(Player player : Bukkit.getOnlinePlayers()) {
                Utils.clearTitle(player);
            }
            SkyWars.getGameManager().startGame();
            if(SkyWars.debug) Bukkit.broadcastMessage(count + 1 + "");
            cancel();
        }




    }
}