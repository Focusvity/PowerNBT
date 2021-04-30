package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;
import me.focusvity.powernbt.util.Viewer;
import net.minecraft.server.v1_16_R3.NBTBase;

public class ActionCopy extends Action
{

    private final Caller caller;
    private final Argument argument;

    public ActionCopy(Caller caller, String object, String parameter)
    {
        this.caller = caller;
        this.argument = new Argument(caller, object, parameter);
    }

    @Override
    public void execute() throws Exception
    {
        if (argument.needPrepare())
        {
            argument.prepare(this, null, null);
            return;
        }
        Container container = argument.getContainer();
        Query query = argument.getQuery();
        NBTBase base = query.get(container.getCustomTag());
        caller.setCustomTag(base);
        caller.send("Copied to buffer: " + Viewer.getShortValueWithPrefix(base, false, false));
    }
}
