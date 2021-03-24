package me.focusvity.powernbt.container;

import me.focusvity.powernbt.nms.NMSBlock;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.List;

public class ContainerBlock extends Container<Block>
{

    private Block block;

    public ContainerBlock(Block block)
    {
        this.block = block;
    }

    @Override
    public Block getObject()
    {
        return block;
    }

    @Override
    protected void writeTag(NBTBase base)
    {
        if (!(base instanceof NBTTagCompound))
        {
            return;
        }
        NMSBlock.setTag(block, (NBTTagCompound) base);
        NMSBlock.update(block);
    }

    @Override
    public NBTTagCompound readCustomTag()
    {
        NBTTagCompound compound = readTag();
        if (compound != null)
        {
            removeIgnored(compound);
        }
        return compound;
    }

    public NBTTagCompound readTag()
    {
        NBTTagCompound compound = new NBTTagCompound();
        NMSBlock.readTag(block, compound);
        return compound;
    }

    @Override
    public void writeCustomTag(NBTBase base)
    {
        if (!(base instanceof NBTTagCompound))
        {
            return;
        }
        NBTTagCompound compound = (NBTTagCompound) base.clone();
        removeIgnored(compound);
        NBTTagCompound original = readTag();
        if (compound.get("x") == null)
        {
            compound.set("x", original.get("x"));
        }
        if (compound.get("y") == null)
        {
            compound.set("y", original.get("y"));
        }
        if (compound.get("z") == null)
        {
            compound.set("z", original.get("z"));
        }
        writeTag(compound);
    }

    @Override
    protected Class<Block> getContainerClass()
    {
        return Block.class;
    }

    @Override
    public List<String> getTypes()
    {
        return Arrays.asList("block", "block_" + block.getType().toString());
    }
}
