package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;
import me.focusvity.powernbt.util.Viewer;

public class ActionView extends Action
{

    private final Caller caller;
    private final Argument arg;
    private String[] args;

    private ActionView(Caller caller, String object, String query)
    {
        this.caller = caller;
        this.arg = new Argument(caller, object, query);
    }

    public ActionView(Caller caller, String object, String query, String args)
    {
        this(caller, object, query);
        if (args != null)
        {
            this.args = args.split(",|\\.|-");
        }
    }

    @Override
    public void execute() throws Exception
    {
        if (arg.needPrepare())
        {
            arg.prepare(null, null, null);
            return;
        }

        Container container = arg.getContainer();
        Query query = arg.getQuery();
        int start = -1, end = -1;
        boolean hex = false;
        boolean bin = false;
        if (args != null)
        {
            for (String s : args)
            {
                hex = (s.equalsIgnoreCase("hex") || s.equalsIgnoreCase("h"));
                bin = (s.equalsIgnoreCase("bin") || s.equalsIgnoreCase("b"));
                if (s.equalsIgnoreCase("full") || s.equalsIgnoreCase("f")
                        || s.equalsIgnoreCase("all") || s.equalsIgnoreCase("a"))
                {
                    start = 0;
                    end = Integer.MAX_VALUE;
                }
                else if (s.matches("[0-9]+"))
                {
                    if (end == -1)
                    {
                        Integer.parseInt(s);
                    }
                    else
                    {
                        Integer.parseInt(s);
                    }
                }
            }
        }
        if (start == -1)
        {
            start = 0;
        }
        if (end == -1)
        {
            end = 0;
        }
        if (start > end)
        {
            int t = start;
            start = end;
            end = t;
        }
        String answer = Viewer.getFullValue(query.get(container.getCustomTag()), start, end, hex, bin);
        caller.send(answer);
    }
}
