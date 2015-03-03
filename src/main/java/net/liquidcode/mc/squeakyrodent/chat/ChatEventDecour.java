package net.liquidcode.mc.squeakyrodent.chat;

import org.apache.commons.lang.WordUtils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ChatEventDecour implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(String.format(ChatColor.DARK_GREEN + " » " + ChatColor.DARK_GRAY+ "%s", event.getPlayer().getDisplayName()));
    }
    
    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        if (event.getQuitMessage().length() > 0 && !event.getQuitMessage().endsWith("left the game.")) {
            event.setQuitMessage(String.format(ChatColor.DARK_RED + " « " + ChatColor.DARK_GRAY+ "%s, %s", event.getPlayer().getDisplayName(), event.getQuitMessage()));
        } else {
            event.setQuitMessage(String.format(ChatColor.DARK_RED + " « " + ChatColor.DARK_GRAY+ "%s", event.getPlayer().getDisplayName()));
        }
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
        worldChangeMessage(event.getPlayer(), event.getTo());
    }
    
    @EventHandler
    public void playerTeleport(PlayerTeleportEvent event) {
        Environment source = event.getFrom().getWorld().getEnvironment();
        Environment destination = event.getTo().getWorld().getEnvironment();
        if (source == destination) {
            return;
        }
        worldChangeMessage(event.getPlayer(), event.getTo());
    }
    
    private void worldChangeMessage(Player player, Location destination) {
        StringBuilder msg = new StringBuilder();
        Environment env = destination.getWorld().getEnvironment();
        msg.append(ChatColor.DARK_GRAY);
        msg.append(player.getDisplayName());
        msg.append(" > ");
        msg.append(ChatColor.GRAY);
        msg.append(WordUtils.capitalizeFully(env.toString()));
        player.getServer().broadcastMessage(msg.toString());
    }
}
