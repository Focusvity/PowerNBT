package me.focusvity.powernbt.nms;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.TileEntity;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;

public class NMSBlock
{

    public static TileEntity getTileEntity(Block block)
    {
        CraftWorld world = (CraftWorld) block.getWorld();
        BlockPosition position = new BlockPosition(block.getX(), block.getY(), block.getZ());
        return world.getHandle().getTileEntity(position);
    }

    public static void setTag(Block block, NBTTagCompound compound)
    {
        compound = compound.clone();
        compound.setInt("x", block.getX());
        compound.setInt("y", block.getY());
        compound.setInt("z", block.getZ());
        setTagUnsafe(block, compound);
    }

    public static void update(Block block)
    {
        if (block == null)
        {
            return;
        }
        TileEntity tile = getTileEntity(block);
        if (tile == null)
        {
            return;
        }
        Packet<?> packet = tile.getUpdatePacket();
        if (packet == null)
        {
            return;
        }
        int maxDistance = Bukkit.getServer().getViewDistance() * 32;
        for (Player player : block.getWorld().getPlayers())
        {
            if (player.getLocation().distance(block.getLocation()) < maxDistance)
            {
                NMSPacket.sendPacket(player, packet);
            }
        }
    }

    public static void setTagUnsafe(Block block, NBTTagCompound compound)
    {
        TileEntity tile = getTileEntity(block);
        if (tile != null)
        {
            tile.b().a(compound);
        }
    }
}
