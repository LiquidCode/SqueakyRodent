package net.liquidcode.mc.squeakyrodent.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
        int changeThreshold = 2;

        HashMap<String, String> params = new HashMap<>();
        // Get the essentials
        params.put("level", String.format("%d", event.getNewLevel()));
        params.put("levelDiff", String.format("%d", levelDiff));
        // Then some more for styling opportunities
        params.put("oldLevel", String.format("%d", event.getOldLevel()));
        params.put("levelChange", String.format("%d", Math.abs(levelDiff)));


        params.put("levelChangePlus", "");
        params.put("levelChangeMinus", "");
        params.put("levelSignPlus", "");
        params.put("levelSignMinus", "");

        if (levelDiff >= 0) {
            params.put("levelSign", "+");
            // Then for higher styling flexibility
            params.put("levelSignPlus", "+");
            if (levelDiff >= changeThreshold)
                params.put("levelChangePlus", String.format("%d", Math.abs(levelDiff)));
        } else {
            params.put("levelSign", "-");
            // Then for higher styling flexibility
            params.put("levelSignMinus", "-");
            if (levelDiff >= changeThreshold)
                params.put("levelChangeMinus", String.format("%d", Math.abs(levelDiff)));
        }

        plugin.getChat().getMessageBroker().sendFromPlayer(event.getPlayer(), "leveling", params);
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
        
        HashMap<String, String> fields = new HashMap<>();
        fields.put("message", deathMessage);
        fields.put("namelessMessage", deathMessage);

        // Strip player name if present
        if (deathMessage.startsWith(event.getEntity().getName())) {
            String[] words = deathMessage.split(" ");
            fields.put("namelessMessage", String.join(" ", Arrays.copyOfRange(words, 1, words.length - 1)));
        }

        UberChat.plugin.getMessageBroker().sendFromPlayer(event.getEntity(), "deaths", fields);
    }
}
