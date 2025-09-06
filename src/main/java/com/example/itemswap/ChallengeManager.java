package com.example.itemswap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ChallengeManager {

    private final ItemSwapPlugin plugin;
    private Player player1;
    private Player player2;
    private Player currentTurn;
    private ItemStack requiredItem;
    private long startTime;

    public ChallengeManager(ItemSwapPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean startChallenge(String name1, String name2) {
        player1 = Bukkit.getPlayerExact(name1);
        player2 = Bukkit.getPlayerExact(name2);

        if (player1 == null || player2 == null) {
            return false;
        }

        currentTurn = player1;
        requiredItem = null;
        startTime = System.currentTimeMillis();

        Bukkit.broadcastMessage(ChatColor.YELLOW + plugin.getConfig().getString("start-message"));

        // Timer starten
        Bukkit.getScheduler().runTaskTimer(plugin, new TimerTask(this), 20L, 20L);

        updateTitles();
        return true;
    }

    public void handlePickup(Player picker, ItemStack item) {
        if (picker != currentTurn) {
            return;
        }

        if (requiredItem == null) {
            // Erstes Item darf frei gewählt werden
            requiredItem = new ItemStack(item.getType(), item.getAmount());
            switchTurn();
            Bukkit.broadcastMessage(ChatColor.GREEN + plugin.getConfig().getString("success-message"));
        } else {
            // Muss genau das geforderte Item sein
            if (item.getType() == requiredItem.getType() && item.getAmount() == requiredItem.getAmount()) {
                Bukkit.broadcastMessage(ChatColor.GREEN + plugin.getConfig().getString("success-message"));
                requiredItem = null;
                switchTurn();
            } else {
                // Falsches Item → beide sterben und Reset
                player1.setHealth(0.0);
                player2.setHealth(0.0);
                Bukkit.broadcastMessage(ChatColor.RED + plugin.getConfig().getString("death-message"));
                resetWorlds();
            }
        }
    }

    private void switchTurn() {
        currentTurn = (currentTurn == player1) ? player2 : player1;
        updateTitles();
    }

    private void updateTitles() {
        Player active = currentTurn;
        Player waiting = (currentTurn == player1) ? player2 : player1;

        if (requiredItem == null) {
            // Aktiver Spieler darf neues Item einsammeln
            active.sendTitle("§aDU BIST DRAN", "§7Beliebiges Item einsammeln", 10, 60, 10);
        } else {
            // Aktiver Spieler muss ein bestimmtes Item sammeln
            active.sendTitle("§aDU BIST DRAN", "§7" + requiredItem.getAmount() + "x " + requiredItem.getType().toString(), 10, 60, 10);
        }

        if (requiredItem == null) {
            waiting.sendTitle("§cDU BIST NICHT DRAN", "§7Warte bis " + active.getName() + " ein Item einsammelt", 10, 60, 10);
        } else {
            waiting.sendTitle("§cDU BIST NICHT DRAN", "§7Warte bis " + active.getName() + " " + requiredItem.getAmount() + "x " + requiredItem.getType().toString() + " einsammelt", 10, 60, 10);
        }
    }

    private void resetWorlds() {
        // Hier könntest du ein Welt-Reset-Plugin oder ein Script antriggern
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "Die Welt muss manuell resettet werden!");
    }

    public long getElapsedSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}
