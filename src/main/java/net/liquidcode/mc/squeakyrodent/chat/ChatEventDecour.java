package net.liquidcode.mc.squeakyrodent.chat;

import org.apache.commons.lang.WordUtils;

import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class ChatEventDecour implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(String.format(ChatColor.DARK_GREEN + " » " + ChatColor.DARK_GRAY+ "%s", event.getPlayer().getDisplayName()));
    }
    
    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(String.format(ChatColor.DARK_RED + " « " + ChatColor.DARK_GRAY+ "%s", event.getPlayer().getDisplayName()));
    }
    
    @EventHandler
    public void playerKick(PlayerKickEvent event) {
        if (event.getReason().toLowerCase().contains("banned")) {
            event.getPlayer().getServer().broadcastMessage(String.format(ChatColor.RED + " ⦸ " + ChatColor.DARK_GRAY + "%s" + ChatColor.GRAY + ", %s", event.getPlayer().getDisplayName(), event.getReason()));
        } else {
            event.getPlayer().getServer().broadcastMessage(String.format(ChatColor.RED + " - " + ChatColor.DARK_GRAY + "%s" + ChatColor.GRAY + ", %s", event.getPlayer().getDisplayName(), event.getReason()));
        }
    }
    
    @EventHandler
    public void playerLevelChange(PlayerLevelChangeEvent event) {
        int levelDiff = event.getNewLevel() - event.getOldLevel();
        StringBuilder message = new StringBuilder();
        if (levelDiff >= 0) {
            message.append(ChatColor.DARK_GREEN);
            message.append(" +");
            message.append(ChatColor.GREEN);
        } else {
            message.append(ChatColor.DARK_RED);
            message.append(" -");
            message.append(ChatColor.RED);
        }
        if (Math.abs(levelDiff) > 1) {
            message.append(Math.abs(levelDiff));
        }
        message.append(ChatColor.DARK_GRAY);
        message.append(" ");
        message.append(event.getPlayer().getDisplayName());
        message.append(" -> ");
        message.append(ChatColor.GREEN);
        message.append(event.getPlayer().getLevel());
        event.getPlayer().getServer().broadcastMessage(message.toString());
    }
    
    @EventHandler
    public void playerPortal(PlayerPortalEvent event) {
        Environment destination = event.getTo().getWorld().getEnvironment();
        StringBuilder msg = new StringBuilder();
        msg.append(ChatColor.DARK_GRAY);
        msg.append(event.getPlayer().getDisplayName());
        msg.append(" > ");
        msg.append(ChatColor.GRAY);
        msg.append(WordUtils.capitalizeFully(destination.toString()));
        event.getPlayer().getServer().broadcastMessage(msg.toString());
    }
}
