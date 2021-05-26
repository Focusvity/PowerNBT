package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;
import me.focusvity.powernbt.util.Viewer;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTNumber;

public class ActionBitInverse extends Action
{

    private final Caller caller;
    private final Argument firstArg;

    public ActionBitInverse(Caller caller, String firstObject, String firstParam)
    {
        this.caller = caller;
        this.firstArg = new Argument(caller, firstObject, firstParam);
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
        NBTBase firstBase = firstQuery.get(firstContainer.getCustomTag());
        if (!(firstBase instanceof NBTNumber))
        {
            throw new RuntimeException("No value");
        }
        long baseValue = ((NBTNumber) firstBase).k().longValue();
        firstBase = setValue((NBTNumber) firstBase, ~baseValue);
        firstQuery.set(firstContainer.getCustomTag(), firstBase);
        caller.send("New value set: " + Viewer.getShortValueWithPrefix(firstBase, false, false));
    }
}
