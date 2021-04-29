package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;
import me.focusvity.powernbt.util.Viewer;
import net.minecraft.server.v1_16_R3.NBTBase;

import java.util.List;

public class ActionRename extends Action
{

    private final Caller caller;
    private final Argument argument;
    private final String name;
    private final Query secondQuery;

    public ActionRename(Caller caller, String object, String parameter, String name)
    {
        this.caller = caller;
        this.argument = new Argument(caller, object, parameter);
        this.name = name;
        this.secondQuery = Query.fromString(name);
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
        Query firstQuery = argument.getQuery();
        List<Object> v = firstQuery.getParent().getValues();
        v.addAll(secondQuery.getValues());
        Query newQuery = new Query(v);
        NBTBase root = container.getTag();
        NBTBase base = firstQuery.get(root);
        if (base == null)
        {
            throw new RuntimeException("Cannot rename tag");
        }
        else
        {
            root = firstQuery.remove(root);
            root = newQuery.set(root, base);
            container.setCustomTag(root);
            caller.send("Tag renamed to " + name + ": " + Viewer.getShortValueWithPrefix(base, false, false));
        }
    }
}
