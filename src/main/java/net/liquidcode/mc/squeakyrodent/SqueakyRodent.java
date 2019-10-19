package net.liquidcode.mc.squeakyrodent;

import net.gameshaft.temporals.uberchat.UberChat;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.liquidcode.mc.squeakyrodent.chat.ChatEventDecour;
import net.liquidcode.mc.squeakyrodent.features.WorldSaving;

/**
 * Hello world!
 *
 */
public class SqueakyRodent extends JavaPlugin
{
    private WorldSaving saveJob = new WorldSaving(this);

    private UberChat chat = null;
    
    public static void main( String[] args )
    {
        System.out.println(
            "I think you may have forgotten that this is a CraftBukkit Plugin."
        );
    }
    
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new ChatEventDecour(this), this);
        saveJob.runTaskTimer(this, 15*60*20, 15*60*20);

        Plugin uberChat = this.getServer().getPluginManager().getPlugin("UberChat");
        if (uberChat != null) {
            this.chat = (UberChat) uberChat;

            this.chat.getMessageBroker().registerMessageType("leveling", false, " §2{levelSignPlus}§a{levelChangePlus}§4{levelSignMinus}§c{levelChangeMinus} §8{displayName} -> §a{level}");
            this.chat.getMessageBroker().registerMessageType("deaths", true, "{message}");

            this.getLogger().info("Detected UberChat, will broadcast based on chat channels");
        }
    }
    
    @Override
    public void onDisable() {
        // @TODO: Do something
    }

    public UberChat getChat() {
        return chat;
    }
}
