package net.liquidcode.mc.squeakyrodent.chat;

import java.util.ArrayList;
import net.gameshaft.temporals.uberchat.UberChat;
import net.gameshaft.temporals.uberchat.chat.ChatChannel;
import net.gameshaft.temporals.uberchat.chat.LocalChatChannel;
import net.gameshaft.temporals.uberchat.chat.TeamChatChannel;
import net.liquidcode.mc.squeakyrodent.SqueakyRodent;
import org.apache.commons.lang.WordUtils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

public class ChatEventDecour implements Listener {
    private final SqueakyRodent plugin;

    public ChatEventDecour(SqueakyRodent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(String.format(ChatColor.DARK_GREEN + " » " + ChatColor.DARK_GRAY+ "%s", event.getPlayer().getDisplayName()));
    }
    
    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        if (event.getQuitMessage().length() > 0 && !event.getQuitMessage().endsWith("left the game")) {
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

        sendLocal(message.toString(), event.getPlayer());
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
    
    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        String deathMessage = event.getDeathMessage();
        event.setDeathMessage("");
        
        ArrayList<ChatChannel> channels = new ArrayList<ChatChannel>();
        
        for (ChatChannel channel : UberChat.plugin.getState().getChannels()) {
            if (channel instanceof LocalChatChannel)
                channels.add(channel);
            
            if (channel instanceof TeamChatChannel)
                channels.add(channel);
        }
        
        UberChat.plugin.getState().sendRawMessage(event.getEntity(), deathMessage, channels);
    }

    private void sendLocal(String msg, Player player) {
        if (plugin.getChat() == null) {
            plugin.getServer().broadcastMessage(msg);
            return;
        }

        for (ChatChannel channel : plugin.getChat().getState().getChannels())
            if (channel instanceof LocalChatChannel)
                for (Player recipient : channel.getRecipients(player))
                    plugin.getLogger().info(recipient.toString());
        
        player.sendMessage(msg);
    }
}
