package com.example.itemswap;

import org.bukkit.plugin.java.JavaPlugin;

public class ItemSwapPlugin extends JavaPlugin {

    private ChallengeManager challengeManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        challengeManager = new ChallengeManager(this);

        // Event-Listener registrieren
        getServer().getPluginManager().registerEvents(new PickupListener(challengeManager), this);

        // Command registrieren
        getCommand("startchallenge").setExecutor((sender, command, label, args) -> {
            if (!sender.hasPermission("itemswap.admin")) {
                sender.sendMessage("§cKeine Berechtigung!");
                return true;
            }

            if (args.length != 2) {
                sender.sendMessage("§cBenutzung: /startchallenge <Spieler1> <Spieler2>");
                return true;
            }

            boolean started = challengeManager.startChallenge(args[0], args[1]);
            if (started) {
                sender.sendMessage("§aChallenge gestartet!");
            } else {
                sender.sendMessage("§cFehler: Spieler nicht gefunden.");
            }
            return true;
        });

        getLogger().info("ItemSwapChallenge aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ItemSwapChallenge deaktiviert!");
    }

    public ChallengeManager getChallengeManager() {
        return challengeManager;
    }
}
