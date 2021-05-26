package me.focusvity.powernbt.command.action;

import net.minecraft.server.v1_16_R3.NBTNumber;
import net.minecraft.server.v1_16_R3.NBTTagByte;
import net.minecraft.server.v1_16_R3.NBTTagDouble;
import net.minecraft.server.v1_16_R3.NBTTagFloat;
import net.minecraft.server.v1_16_R3.NBTTagInt;
import net.minecraft.server.v1_16_R3.NBTTagLong;
import net.minecraft.server.v1_16_R3.NBTTagShort;

public abstract class Action
{

    public abstract void execute() throws Exception;

    public NBTNumber setValue(NBTNumber nbt, long number)
    {
        if (nbt instanceof NBTTagByte)
        {
            nbt = NBTTagByte.a((byte) number);
        }
        else if (nbt instanceof NBTTagShort)
        {
            nbt = NBTTagShort.a((short) number);
        }
        else if (nbt instanceof NBTTagInt)
        {
            nbt = NBTTagInt.a((int) number);
        }
        else if (nbt instanceof NBTTagLong)
        {
            nbt = NBTTagLong.a(number);
        }
        else if (nbt instanceof NBTTagFloat)
        {
            nbt = NBTTagFloat.a((float) number);
        }
        else if (nbt instanceof NBTTagDouble)
        {
            nbt = NBTTagDouble.a((double) number);
        }
        return nbt;
    }
}
