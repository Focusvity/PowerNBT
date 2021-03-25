package me.focusvity.powernbt.container;

import me.focusvity.powernbt.nms.NMSChunk;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Chunk;

import java.util.Arrays;
import java.util.List;

public class ContainerChunk extends Container<Chunk>
{

    private Chunk chunk;

    public ContainerChunk(Chunk chunk)
    {
        this.chunk = chunk;
    }

    @Override
    public Chunk getObject()
    {
        return chunk;
    }

    @Override
    protected NBTBase readTag()
    {
        NBTTagCompound compound = new NBTTagCompound();
        NMSChunk.readChunk(chunk, compound);
        return compound;
    }

    @Override
    protected void writeTag(NBTBase base)
    {
        NMSChunk.writeChunk(chunk, (NBTTagCompound) base.clone());
    }

    @Override
    protected Class<Chunk> getContainerClass()
    {
        return Chunk.class;
    }

    @Override
    public List<String> getTypes()
    {
        return Arrays.asList("s");
    }

    @Override
    public String toString()
    {
        return chunk.toString();
    }
}
