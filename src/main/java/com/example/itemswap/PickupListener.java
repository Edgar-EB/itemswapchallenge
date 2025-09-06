package com.example.itemswap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PickupListener implements Listener {

    private final ChallengeManager challengeManager;

    public PickupListener(ChallengeManager challengeManager) {
        this.challengeManager = challengeManager;
    }

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        challengeManager.handlePickup(event.getPlayer(), item);
    }
}
