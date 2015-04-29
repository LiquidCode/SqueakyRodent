package net.liquidcode.mc.squeakyrodent;

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
    
    public static void main( String[] args )
    {
        System.out.println(
            "I think you may have forgotten that this is a CraftBukkit Plugin."
        );
    }
    
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new ChatEventDecour(), this);
        saveJob.runTaskTimer(this, 15*60*20, 15*60*20);
    }
    
    @Override
    public void onDisable() {
        // @TODO: Do something
    }
}
