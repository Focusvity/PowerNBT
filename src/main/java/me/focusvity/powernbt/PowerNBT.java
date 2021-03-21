package me.focusvity.powernbt;

import org.bukkit.plugin.java.JavaPlugin;

public class PowerNBT extends JavaPlugin
{

    private static PowerNBT plugin;

    @Override
    public void onEnable()
    {
        this.plugin = this;
    }

    @Override
    public void onDisable()
    {
        this.plugin = null;
    }

    public static PowerNBT getInstance()
    {
        return plugin;
    }
}
