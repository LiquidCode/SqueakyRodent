package net.liquidcode.mc.squeakyrodent.chat;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

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
            message.append(ChatColor.GREEN);
            message.append(" ++");
        } else {
            message.append(ChatColor.RED);
            message.append(" --");
        }
        if (Math.abs(levelDiff) > 1) {
            message.append(Math.abs(levelDiff));
        }
        message.append(ChatColor.GRAY);
        message.append(" ");
        message.append(event.getPlayer().getDisplayName());
        event.getPlayer().getServer().broadcastMessage(message.toString());
    }
}
