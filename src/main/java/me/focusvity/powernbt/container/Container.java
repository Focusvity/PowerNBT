package me.focusvity.powernbt.container;

import me.focusvity.powernbt.PowerNBT;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;

import java.util.List;

public abstract class Container<T>
{

    public abstract T getObject();

    protected abstract NBTBase readTag();

    protected abstract void writeTag(NBTBase base);

    protected void eraseTag()
    {
        writeTag(new NBTTagCompound());
    }

    public NBTBase readCustomTag()
    {
        return readTag();
    }

    public void writeCustomTag(NBTBase base)
    {
        writeTag(base.clone());
    }

    public void eraseCustomTag()
    {
        eraseTag();
    }

    protected abstract Class<T> getContainerClass();

    public final String getName()
    {
        return getContainerClass().getSimpleName();
    }

    public abstract List<String> getTypes();

    public final NBTBase getTag()
    {
        return readTag();
    }

    public final void setTag(NBTBase base)
    {
        if (base == null)
        {
            eraseTag();
            return;
        }
        writeTag(base.clone());
    }

    public final void setCustomTag(NBTBase base)
    {
        if (base == null)
        {
            eraseCustomTag();
            return;
        }
        base = base.clone();
        if (base instanceof NBTTagCompound)
        {
            NBTTagCompound compound = (NBTTagCompound) base;
            removeIgnored(compound);
        }
        writeCustomTag(base);
    }

    public final NBTBase getCustomTag()
    {
        NBTBase base = readTag();
        if (base instanceof NBTTagCompound)
        {
            NBTTagCompound compound = (NBTTagCompound) base;
            removeIgnored(compound);
        }
        return base;
    }

    public final void removeTag()
    {
        eraseTag();
    }

    public final void removeCustomTag()
    {
        eraseCustomTag();
    }

    public void removeIgnored(NBTTagCompound compound)
    {
        PowerNBT.getInstance().getConfig().getStringList("ignore." + getName()).forEach(ignore ->
                compound.remove(ignore));
    }
}
