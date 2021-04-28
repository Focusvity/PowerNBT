package me.focusvity.powernbt.command;

import me.focusvity.powernbt.command.action.Action;
import me.focusvity.powernbt.command.action.ActionView;
import me.focusvity.powernbt.util.Caller;

import java.util.*;

public class CommandNBT extends Command
{

    public final Set<String> specialTokens = new HashSet<String>(
            Arrays.asList(
                    "=", "<",
                    "rm", "rem", "remove",
                    "ren", "rename",
                    "copy",
                    "&=", // a = a & b
                    "|=", // a = a | b
                    "^=", // a = a ^ b
                    "*=", // a = a * b
                    "~",
                    "paste",
                    "add", "+=",
                    "cut",
                    "set", "select",
                    "as",
                    "view", "?",
                    "debug",
                    "cancel",
                    "swap", "<>",
                    ">",
                    ">>",
                    "<<",
                    "insert", "ins",
                    "spawn"
            )
    );

    public CommandNBT()
    {
        super(false);
    }

    public CommandNBT(boolean silent)
    {
        super(silent);
    }

    @Override
    protected boolean command(Caller caller, List<String> words) throws Throwable
    {
        if (words.isEmpty())
        {
            return false;
        }

        LinkedList<String> argsBefore = new LinkedList<>();
        LinkedList<String> argsAfter = new LinkedList<>();
        String action = null;
        for (String t : words)
        {
            if (specialTokens.contains(t))
            {
                action = t;
            }
            else
            {
                if (action == null)
                {
                    argsBefore.add(t);
                }
                else
                {
                    argsAfter.add(t);
                }
            }
        }

        RuntimeException tooManyException = new RuntimeException("Too many arguments");
        RuntimeException notEnoughException = new RuntimeException("Not enough arguments");

        if (action == null)
        {
            if (argsBefore.size() > 3)
            {
                throw tooManyException;
            }
            Action a = new ActionView(caller, argsBefore.poll(), argsBefore.poll(), argsBefore.poll());
            a.execute();
        }
        return false;
    }
}
