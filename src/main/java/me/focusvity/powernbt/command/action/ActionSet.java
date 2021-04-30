package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.container.ContainerComplex;
import me.focusvity.powernbt.container.ContainerVariable;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;

public class ActionSet extends Action
{

    private final Caller caller;
    private final Argument firstArg;
    private final Argument secondArg;

    public ActionSet(Caller caller, String firstObject, String secondObject, String paramater)
    {
        this.caller = caller;
        this.firstArg = new Argument(caller, firstObject, null);
        this.secondArg = new Argument(caller, secondObject, paramater);
    }

    @Override
    public void execute() throws Exception
    {
        if (firstArg.needPrepare())
        {
            firstArg.prepare(this, null, null);
            return;
        }
        Container container = firstArg.getContainer();
        if (!(container instanceof ContainerVariable))
        {
            throw new RuntimeException("Variable Required");
        }
        ContainerVariable variable = (ContainerVariable) container;
        if (secondArg.needPrepare())
        {
            secondArg.prepare(this, container, null);
            return;
        }
        Container secondContainer = secondArg.getContainer();
        Query query = secondArg.getQuery();
        if (query != null && !query.isEmpty())
        {
            container = new ContainerComplex(container, query);
        }
        variable.setContainer(container);
        caller.send(container + " selected as " + variable.getVariableName());
    }
}
