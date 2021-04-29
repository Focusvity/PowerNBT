package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.util.Caller;

public class ActionCancel extends Action
{

    private final Caller caller;

    public ActionCancel(Caller caller)
    {
        this.caller = caller;
    }

    @Override
    public void execute()
    {
        caller.hold(null, null);
        caller.send("Selection Cancelled");
    }
}
