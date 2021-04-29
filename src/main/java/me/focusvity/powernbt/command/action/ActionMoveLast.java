package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;
import me.focusvity.powernbt.util.Viewer;
import net.minecraft.server.v1_16_R3.NBTBase;

public class ActionMoveLast extends Action
{

    private final Caller caller;
    private final Argument firstArg;
    private final Argument secondArg;

    public ActionMoveLast(Caller caller, String firstObject, String firstParam, String secondObject, String secondParam)
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
        NBTBase base = firstQuery.get(firstContainer.getCustomTag());
        if (base == null)
        {
            throw new RuntimeException("No Value");
        }
        try
        {
            secondQuery.set(secondContainer.getCustomTag(), base);
            firstQuery.remove(firstContainer.getCustomTag());
            caller.send("Tag moved: " + Viewer.getShortValueWithPrefix(base, false, false));
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Cannot move tag from " + secondQuery.toString(), ex);
        }
    }
}
