package me.focusvity.powernbt.container;

import net.minecraft.server.v1_16_R3.NBTBase;

import java.util.ArrayList;
import java.util.List;

public class ContainerBase extends Container<NBTBase>
{

    private NBTBase base;

    public ContainerBase(NBTBase base)
    {
        this.base = base;
    }

    @Override
    public NBTBase getObject()
    {
        return base;
    }

    @Override
    protected NBTBase readTag()
    {
        return base;
    }

    @Override
    protected void writeTag(NBTBase base)
    {
        this.base = base;
    }

    @Override
    public void eraseTag()
    {
        this.base = null;
    }

    @Override
    protected Class<NBTBase> getContainerClass()
    {
        return NBTBase.class;
    }

    @Override
    public List<String> getTypes()
    {
        return new ArrayList<>();
    }

    @Override
    public String toString()
    {
        return base.asString();
    }
}
