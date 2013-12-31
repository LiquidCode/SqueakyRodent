package net.liquidcode.mc.squeakyrodent;

import org.bukkit.plugin.java.JavaPlugin;

import net.liquidcode.mc.squeakyrodent.chat.ChatEventDecour;

/**
 * Hello world!
 *
 */
public class SqueakyRodent extends JavaPlugin
{
    public static void main( String[] args )
    {
        System.out.println(
            "I think you may have forgotten that this is a CraftBukkit Plugin."
        );
    }
    
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new ChatEventDecour(), this);
    }
    
    @Override
    public void onDisable() {
        // @TODO: Do something
    }
}
