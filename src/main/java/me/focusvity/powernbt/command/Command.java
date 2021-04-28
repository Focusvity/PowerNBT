package me.focusvity.powernbt.command;

import me.focusvity.powernbt.PowerNBT;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Tokenizer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command implements CommandExecutor
{

    private final Tokenizer tokenizer = new Tokenizer(null, null, null,
            Arrays.asList('\"'), null, Arrays.asList(' '));
    private final boolean silent;

    public Command(boolean silent)
    {
        this.silent = silent;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
    {
        Caller caller = PowerNBT.getCaller(sender);
        caller.setSilent(silent);
        try
        {
            List<String> words = new ArrayList<>();
            for (String s : tokenizer.tokenize(StringUtils.join(args, ' ')).values())
            {
                words.add(s);
            }
            return command(caller, words);
        }
        catch (Throwable t)
        {
            caller.handleException(t);
            return true;
        }
    }

    protected abstract boolean command(Caller caller, List<String> words) throws Throwable;
}
