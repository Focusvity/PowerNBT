package me.focusvity.powernbt;

import me.focusvity.powernbt.command.CommandNBT;
import me.focusvity.powernbt.listener.SelectListener;
import me.focusvity.powernbt.util.Caller;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class PowerNBT extends JavaPlugin
{

    private static PowerNBT plugin;
    private static String prefix = ChatColor.GOLD + "[" + ChatColor.YELLOW + "PowerNBT" + ChatColor.GOLD + "] " + ChatColor.RESET;
    private static String errorPrefix = ChatColor.DARK_RED + "[" + ChatColor.RED + "PowerNBT" + ChatColor.DARK_RED + "] " + ChatColor.RESET;
    private static HashMap<String, Caller> callers = new HashMap<>();

    public static PowerNBT getInstance()
    {
        return plugin;
    }

    public static String getPrefix()
    {
        return prefix;
    }

    public static String getErrorPrefix()
    {
        return errorPrefix;
    }

    public static Caller getCaller(CommandSender sender)
    {
        Caller c = callers.get(sender.getName());
        if (c != null)
        {
            c.setSender(sender);
            return c;
        }
        c = new Caller(sender);
        callers.put(sender.getName(), c);
        return c;
    }

    @Override
    public void onEnable()
    {
        this.plugin = this;
        getConfig().options().copyDefaults(true);
        getServer().getPluginManager().registerEvents(new SelectListener(), this);
        getCommand("powernbt").setExecutor(new CommandNBT());
        //getCommand("powernbt.").setExecutor(new CommandNBT(true));
    }

    @Override
    public void onDisable()
    {
        this.plugin = null;
    }
}
