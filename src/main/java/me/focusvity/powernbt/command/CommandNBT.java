package me.focusvity.powernbt.command;

import me.focusvity.powernbt.command.action.*;
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

        if (action == null)
        {
            checkArgs(argsBefore, 3, true);
            Action a = new ActionView(caller, argsBefore.poll(), argsBefore.poll(), argsBefore.poll());
            a.execute();
        }
        else if (action.equals("view") || action.equals("?"))
        {
            checkArgs(argsBefore, 3, true);
            checkArgs(argsBefore, 1, false);
            checkArgs(argsAfter, 0, true);
            Action a = new ActionView(caller, argsBefore.poll(), argsBefore.poll(), argsBefore.poll());
            a.execute();
        }
        else if (action.equals("paste"))
        {
            validate(argsBefore);
            checkArgs(argsAfter, 1, true);
            Action a = new ActionEdit(caller, argsBefore.poll(), argsBefore.poll(), "buffer", argsAfter.poll());
            a.execute();
        }
        else if (action.equals("cancel"))
        {
            checkArgs(argsBefore, 0, true);
            checkArgs(argsAfter, 0, true);
            Action a = new ActionCancel(caller);
            a.execute();
        }
        else if (action.equals("=") || action.equals("<"))
        {
            validate(argsBefore);
            validate(argsAfter);
            Action a = new ActionEdit(caller, argsBefore.poll(), argsBefore.poll(), argsAfter.poll(), argsAfter.poll());
            a.execute();
        }
        else if (action.equals(">"))
        {
            validate(argsBefore);
            validate(argsAfter);
            Action a = new ActionEditLast(caller, argsBefore.poll(), argsBefore.poll(), argsAfter.poll(), argsAfter.poll());
            a.execute();
        }
        else if (action.equals(">>"))
        {
            validate(argsBefore);
            validate(argsAfter);
            Action a = new ActionMove(caller, argsBefore.poll(), argsBefore.poll(), argsAfter.poll(), argsAfter.poll());
            a.execute();
        }
        else if (action.equals("<<"))
        {
            validate(argsBefore);
            validate(argsAfter);
            Action a = new ActionMoveLast(caller, argsBefore.poll(), argsBefore.poll(), argsAfter.poll(), argsAfter.poll());
            a.execute();
        }
        else if (action.equals("cut"))
        {
            validate(argsBefore);
            checkArgs(argsAfter, 0, true);
            Action a = new ActionCut(caller, argsBefore.poll(), argsBefore.poll());
            a.execute();
        }
        else if (action.equals("rm") || action.equals("rem") || action.equals("remove"))
        {
            validate(argsBefore);
            checkArgs(argsAfter, 0, true);
            Action a = new ActionRemove(caller, argsBefore.poll(), argsBefore.poll());
            a.execute();
        }
        else if (action.equals("ren") || action.equals("rename"))
        {
            checkArgs(argsBefore, 2, true);
            checkArgs(argsBefore, 2, false);
            if (argsAfter.size() != 1)
            {
                throw new RuntimeException("Too many arguments");
            }
            Action a = new ActionRename(caller, argsBefore.poll(), argsBefore.poll(), argsAfter.poll());
            a.execute();
        }
        else if (action.equals("copy"))
        {
            validate(argsBefore);
            checkArgs(argsAfter, 0, true);
            Action a = new ActionCopy(caller, argsBefore.poll(), argsBefore.poll());
            a.execute();
        }
        else if (action.equals("select") || action.equals("set"))
        {
            checkArgs(argsBefore, 1, true);
            validate(argsAfter);
            Action a = new ActionSet(caller, argsBefore.poll(), argsAfter.poll(), argsAfter.poll());
            a.execute();
        }
        else if (action.equals("as"))
        {
            validate(argsBefore);
            checkArgs(argsAfter, 1, true);
            Action a = new ActionSet(caller, argsAfter.poll(), argsBefore.poll(), argsBefore.poll());
            a.execute();
        }
        else if (action.equals("swap") || action.equals("<>"))
        {
            validate(argsBefore);
            validate(argsAfter);
            Action a = new ActionSwap(caller, argsBefore.poll(), argsBefore.poll(), argsAfter.poll(), argsAfter.poll());
            a.execute();
        }
        else if (action.equals("add") || action.equals("+="))
        {
            validate(argsBefore);
            validate(argsAfter);
            Action a = new ActionAddAll(caller, argsBefore.poll(), argsBefore.poll(), argsAfter.poll(), argsAfter.poll());
            a.execute();
        }
        return true;
    }

    private void checkArgs(List<?> list, int size, boolean more)
    {
        if (more && list.size() > size)
        {
            throw new RuntimeException("Too many arguments");
        }
        else if (list.size() < size)
        {
            throw new RuntimeException("Not enough arguments");
        }
    }

    private void validate(List<?> list)
    {
        checkArgs(list, 2, true);
        checkArgs(list, 1, false);
    }
}
