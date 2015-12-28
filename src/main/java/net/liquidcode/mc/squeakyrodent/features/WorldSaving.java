/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liquidcode.mc.squeakyrodent.features;

import net.liquidcode.mc.squeakyrodent.SqueakyRodent;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author sl
 */
public class WorldSaving extends BukkitRunnable {
    private SqueakyRodent plugin;

    public WorldSaving(SqueakyRodent plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        announce("@a", "Saving...");
        this.plugin.getServer().savePlayers();
        for (World world : this.plugin.getServer().getWorlds()) {
            world.save();
        }
        announce("@a", "Saved!");
    }
    
    public void announce(String player, String message) {
        String cmd = "title " + player + " ";
        this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), cmd + "reset");
        this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), cmd + "subtitle {text:\"" + message + "\"}");
        this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), cmd + "times 0 20 20");
        this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), cmd + "title {text:\"\"}");
    }
}
