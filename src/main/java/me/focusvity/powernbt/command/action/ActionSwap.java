package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;
import net.minecraft.server.v1_16_R3.NBTBase;

public class ActionSwap extends Action
{

    private final Caller caller;
    private final Argument firstArg;
    private final Argument secondArg;

    public ActionSwap(Caller caller, String firstObject, String firstParam, String secondObject, String secondParam)
    {
        this.caller = caller;
        this.firstArg = new Argument(caller, firstObject, firstParam);
        this.secondArg = new Argument(caller, secondObject, secondParam);
    }

    @Override
    public void execute() throws Exception
    {
        if (firstArg.needPrepare())
        {
            firstArg.prepare(this, null, null);
            return;
        }
        Container firstContainer = firstArg.getContainer();
        Query firstQuery = firstArg.getQuery();
        if (secondArg.needPrepare())
        {
            secondArg.prepare(this, firstContainer, firstQuery);
            return;
        }
        Container secondContainer = secondArg.getContainer();
        Query secondQuery = secondArg.getQuery();
        NBTBase firstBase = firstQuery.get(firstContainer.getCustomTag());
        NBTBase secondBase = secondQuery.get(secondContainer.getCustomTag());
        if (firstBase == null && secondBase == null)
        {
            throw new RuntimeException("No elements to swap");
        }
        if (secondBase == null)
        {
            firstQuery.remove(firstContainer.getCustomTag());
        }
        else
        {
            firstQuery.set(firstContainer.getCustomTag(), secondBase);
        }
        if (firstBase == null)
        {
            secondQuery.remove(secondContainer.getCustomTag());
        }
        else
        {
            secondQuery.set(secondContainer.getCustomTag(), firstBase);
        }
        caller.send("Tags swapped");
    }
}
