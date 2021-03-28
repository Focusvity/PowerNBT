package me.focusvity.powernbt.exception;

import net.minecraft.server.v1_16_R3.NBTBase;

public class NBTTagUnexpectedType extends QueryException
{

    private final NBTBase tag;

    public NBTTagUnexpectedType(NBTBase tag, Class<? extends NBTBase> expected)
    {
        super("Tag has wrong type " + tag.getClass().getSimpleName() + " but expected " + expected.getSimpleName());
        this.tag = tag;
    }

    public NBTBase getTag()
    {
        return tag;
    }
}
