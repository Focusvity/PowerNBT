package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;
import me.focusvity.powernbt.util.Viewer;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTNumber;

public class ActionMultiply extends Action
{

    private final Caller caller;
    private final Argument firstArg;
    private final Argument secondArg;

    public ActionMultiply(Caller caller, String firstObject, String firstParam, String secondObject, String secondParam)
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
        NBTBase firstBase = firstQuery.get(firstContainer.getCustomTag());
        Container secondContainer = secondArg.getContainer();
        Query secondQuery = secondArg.getQuery();
        NBTBase secondBase = secondQuery.get(secondContainer.getCustomTag());
        if (!(secondBase instanceof NBTNumber))
        {
            throw new RuntimeException("No value");
        }
        if (firstBase == null)
        {
            firstBase = secondBase.clone();
            firstBase = setValue((NBTNumber) firstBase, 0);
        }
        Number result;
        Number firstNumber = ((NBTNumber) firstBase).k();
        Number secondNumber = ((NBTNumber) secondBase).k();
        if (firstNumber instanceof Float || firstNumber instanceof Double || secondNumber instanceof Float || secondNumber instanceof Double)
        {
            result = firstNumber.doubleValue() * secondNumber.doubleValue();
        }
        else
        {
            result = firstNumber.longValue() * secondNumber.longValue();
        }
        firstBase = setValue((NBTNumber) firstBase, result.longValue());
        firstQuery.set(firstContainer.getCustomTag(), firstBase);
        caller.send("New value set: " + Viewer.getShortValueWithPrefix(firstBase, false, false));
    }
}
