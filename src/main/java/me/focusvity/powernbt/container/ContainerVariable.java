package me.focusvity.powernbt.container;

import me.focusvity.powernbt.util.Caller;
import net.minecraft.server.v1_16_R3.NBTBase;

import java.util.ArrayList;
import java.util.List;

public class ContainerVariable extends Container<Caller>
{

    private Caller caller;
    private String name;

    public ContainerVariable(Caller caller, String name)
    {
        this.caller = caller;
        this.name = name;
    }

    @Override
    public Caller getObject()
    {
        return caller;
    }

    @Override
    protected NBTBase readTag()
    {
        Container c = getContainer();
        if (c != null)
        {
            return c.readTag();
        }
        return null;
    }

    @Override
    protected void writeTag(NBTBase base)
    {
        Container c = getContainer();
        if (c != null)
        {
            c.writeTag(base);
        }
    }

    @Override
    protected void eraseTag()
    {
        Container c = getContainer();
        if (c != null)
        {
            c.eraseTag();
        }
    }

    @Override
    public NBTBase readCustomTag()
    {
        Container c = getContainer();
        if (c != null)
        {
            return c.readCustomTag();
        }
        return null;
    }

    @Override
    public void writeCustomTag(NBTBase base)
    {
        Container c = getContainer();
        if (c != null)
        {
            c.writeCustomTag(base);
        }
    }

    @Override
    public void eraseCustomTag()
    {
        Container c = getContainer();
        if (c != null)
        {
            c.eraseCustomTag();
        }
    }

    @Override
    protected Class<Caller> getContainerClass()
    {
        return Caller.class;
    }

    @Override
    public List<String> getTypes()
    {
        Container c = getContainer();
        if (c == null)
        {
            return new ArrayList<>();
        }
        return c.getTypes();
    }

    public Container getContainer()
    {
        return caller.getVariable(name);
    }

    public String getVariableName()
    {
        return name;
    }

    public void setContainer(Container c)
    {
        caller.setVariable(name, c);
    }

    public void removeContainer()
    {
        caller.removeVariable(name);
    }

    @Override
    public String toString()
    {
        return "%" + name;
    }
}
