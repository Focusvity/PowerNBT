package me.focusvity.powernbt.exception;

import net.minecraft.server.v1_16_R3.NBTBase;

public class NBTTagNotFound extends QueryException
{

    private final NBTBase tag;

    public NBTTagNotFound(NBTBase tag, Object object)
    {
        super("Tag " + object + " not found");
        this.tag = tag;
    }

    public NBTBase getTag()
    {
        return tag;
    }
}
