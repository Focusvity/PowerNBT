package me.focusvity.powernbt;

import me.focusvity.powernbt.command.Action;
import me.focusvity.powernbt.command.Argument;
import me.focusvity.powernbt.container.Container;
import net.minecraft.server.v1_16_R3.NBTBase;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Caller extends Container<Caller>
{

    private final HashMap<String, Container> variables = new HashMap<>();
    private CommandSender sender;
    private boolean silent;
    private NBTBase base;
    private Argument argument;
    private Action action;

    public Caller(CommandSender sender)
    {
        this.sender = sender;
    }

    public boolean isSilent()
    {
        return silent;
    }

    public void setSilent(boolean silent)
    {
        this.silent = silent;
    }

    public CommandSender getSender()
    {
        return sender;
    }

    public void setSender(CommandSender sender)
    {
        this.sender = sender;
    }

    public Argument getArgument()
    {
        return argument;
    }

    public Action getAction()
    {
        return action;
    }

    public void hold(Argument argument, Action action)
    {
        this.argument = argument;
        this.action = action;
    }

    public HashMap<String, Container> getVariables()
    {
        return variables;
    }

    public Container getVariable(String name)
    {
        return variables.get(name);
    }

    public void setVariable(String name, Container value)
    {
        variables.put(name, value);
    }

    public void removeVariable(String name)
    {
        variables.remove(name);
    }

    public void send(Object object)
    {
        if (silent)
        {
            return;
        }
        String message = PowerNBT.getPrefix() + object;
        if (message.length() > 32743)
        {
            message = message.substring(0, 32743);
        }
        sender.sendMessage(message);
    }

    public void handleException(Throwable ex)
    {
        String message;
        if (ex.getClass().equals(RuntimeException.class))
        {
            message = PowerNBT.getErrorPrefix() + ex.getMessage();
        }
        else
        {
            message = PowerNBT.getErrorPrefix() + ChatColor.RED + ex.getClass().getSimpleName() + ": "
                    + ChatColor.RESET + ex.getMessage();
        }
        if (message.length() > 32743)
        {
            message = message.substring(0, 32743);
        }
        sender.sendMessage(message);
    }

    @Override
    public Caller getObject()
    {
        return this;
    }

    @Override
    protected NBTBase readTag()
    {
        return base;
    }

    @Override
    protected void writeTag(NBTBase base)
    {
        this.base = base;
    }

    @Override
    protected Class<Caller> getContainerClass()
    {
        return Caller.class;
    }

    @Override
    public List<String> getTypes()
    {
        return Arrays.asList("entity", "living", "entity_player");
    }

    @Override
    public void eraseTag()
    {
        this.base = null;
    }
}
