package me.focusvity.powernbt.container;

import me.focusvity.powernbt.exception.NBTTagNotFound;
import me.focusvity.powernbt.exception.QueryException;
import me.focusvity.powernbt.util.Query;
import net.minecraft.server.v1_16_R3.NBTBase;

import java.util.ArrayList;
import java.util.List;

public class ContainerComplex extends Container<Container>
{

    private final Container container;
    private final Query query;

    public ContainerComplex(Container container, Query query)
    {
        this.container = container;
        this.query = query;
    }

    public Query getQuery()
    {
        return query;
    }

    @Override
    public Container getObject()
    {
        return container;
    }

    @Override
    protected NBTBase readTag()
    {
        try
        {
            return query.get(container.getTag());
        }
        catch (NBTTagNotFound nbtTagNotFound)
        {
            return null;
        }
    }

    @Override
    public NBTBase readCustomTag()
    {
        try
        {
            return query.get(container.getCustomTag());
        }
        catch (NBTTagNotFound nbtTagNotFound)
        {
            throw new RuntimeException(nbtTagNotFound);
        }
    }

    @Override
    protected void writeTag(NBTBase base)
    {
        try
        {
            NBTBase value = query.set(container.getTag(), base);
            container.writeTag(value);
        }
        catch (QueryException exception)
        {
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    @Override
    public void writeCustomTag(NBTBase base)
    {
        try
        {
            NBTBase value = query.set(container.getCustomTag(), base);
            container.writeCustomTag(value);
        }
        catch (QueryException exception)
        {
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    @Override
    protected void eraseTag()
    {
        try
        {
            NBTBase value = query.remove(container.getTag());
            container.setTag(value);
        }
        catch (NBTTagNotFound nbtTagNotFound)
        {
            throw new RuntimeException(nbtTagNotFound.getMessage(), nbtTagNotFound);
        }
    }

    @Override
    public void eraseCustomTag()
    {
        try
        {
            NBTBase value = query.remove(container.getCustomTag());
            container.setCustomTag(value);
        }
        catch (NBTTagNotFound nbtTagNotFound)
        {
            throw new RuntimeException(nbtTagNotFound.getMessage(), nbtTagNotFound);
        }
    }

    @Override
    protected Class<Container> getContainerClass()
    {
        return Container.class;
    }

    @Override
    public List<String> getTypes()
    {
        return new ArrayList<>();
    }

    @Override
    public String toString()
    {
        if (query == null || query.toString().isEmpty())
        {
            return "<" + container.toString() + ">";
        }
        return "<" + container.toString() + " " + query.toString() + ">";
    }
}
