package com.example.itemswap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerTask extends BukkitRunnable {

    private final ChallengeManager challengeManager;

    public TimerTask(ChallengeManager challengeManager) {
        this.challengeManager = challengeManager;
    }

    @Override
    public void run() {
        long seconds = challengeManager.getElapsedSeconds();
        long minutes = seconds / 60;
        long secs = seconds % 60;
        String timeString = String.format("%02d:%02d", minutes, secs);

        Player p1 = Bukkit.getPlayer(challengeManager.getPlayer1().getName());
        Player p2 = Bukkit.getPlayer(challengeManager.getPlayer2().getName());

        if (p1 != null) {
            p1.sendActionBar(org.bukkit.ChatColor.YELLOW + "⏱ Challenge: " + timeString);
        }
        if (p2 != null) {
            p2.sendActionBar(org.bukkit.ChatColor.YELLOW + "⏱ Challenge: " + timeString);
        }
    }
}
