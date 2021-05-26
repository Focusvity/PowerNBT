package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;
import me.focusvity.powernbt.util.Viewer;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTList;
import net.minecraft.server.v1_16_R3.NBTNumber;

public class ActionInsert extends Action
{

    private final Caller caller;
    private final Argument firstArg;
    private final Argument secondArg;
    private final int pos;

    public ActionInsert(Caller caller, String firstObject, String firstParam, String pos, String secondObject, String secondParam)
    {
        this.caller = caller;
        this.pos = Integer.parseInt(pos);
        if (this.pos < 0)
        {
            caller.send("Invalid Index: " + this.pos);
        }
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
        NBTBase firstBase = firstQuery.get(firstContainer.getCustomTag());
        Container secondContainer = secondArg.getContainer();
        Query secondQuery = secondArg.getQuery();
        NBTBase secondBase = secondQuery.get(secondContainer.getCustomTag());
        if (firstBase instanceof NBTList)
        {
            NBTList tag = (NBTList) firstBase;
            tag.add(pos, secondBase);
            firstQuery.set(firstContainer.getCustomTag(), tag);
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondBase, false, false));
        }
        else
        {
            caller.send("Cannot insert tags");
        }
    }
}
