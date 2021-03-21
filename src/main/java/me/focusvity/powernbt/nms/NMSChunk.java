package me.focusvity.powernbt.nms;

import me.focusvity.powernbt.PowerNBT;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_16_R3.CraftChunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NMSChunk
{

    public static void readChunk(Chunk chunk, NBTTagCompound compound)
    {
        net.minecraft.server.v1_16_R3.Chunk nmsChunk = ((CraftChunk) chunk).getHandle();
        WorldServer nmsWorld = nmsChunk.world;
        ChunkRegionLoader.loadChunk(nmsWorld, nmsWorld.n(), nmsWorld.y(), nmsChunk.getPos(), compound);
    }

    public static void writeChunk(Chunk chunk, NBTTagCompound compound)
    {
        writeChunk(chunk, compound, false);
    }

    public static void writeChunkUnsafe(Chunk chunk, NBTTagCompound compound)
    {
        writeChunk(chunk, compound, true);
    }

    private static void writeChunk(Chunk chunk, NBTTagCompound compound, boolean unsafe)
    {
        net.minecraft.server.v1_16_R3.Chunk nmsChunk = ((CraftChunk) chunk).getHandle();
        WorldServer nmsWorld = nmsChunk.world;
        int x = chunk.getX();
        int z = chunk.getZ();
        for (Entity entity : chunk.getEntities())
        {
            if (!(entity instanceof Player))
            {
                entity.remove();
            }
        }
        chunk.unload();
        compound = compound.clone();
        if (!unsafe)
        {
            compound.setInt("xPos", x);
            compound.setInt("zPos", z);
            fixEntitiesData(compound.getList("Entities", 10), x, z);
            fixTileEntitiesData(compound.getList("TileEntities", 3), x, z);
        }
        ChunkRegionLoader.loadChunk(nmsWorld, nmsWorld.n(), nmsWorld.y(), new ChunkCoordIntPair(x, z), compound);
        net.minecraft.server.v1_16_R3.Chunk newChunk = nmsWorld.getChunkIfLoaded(x, z);
        if (newChunk == null)
        {
            return;
        }
        newChunk.addEntities();
        ChunkReloadTask task = new ChunkReloadTask(chunk);
        task.run();
        Bukkit.getScheduler().runTaskLater(PowerNBT.getInstance(), task, 2);
    }

    private static void fixEntitiesData(NBTTagList tagList, int x, int z)
    {
        if (tagList == null)
        {
            return;
        }
        for (NBTBase nbtEntity : tagList)
        {
            if (!(nbtEntity instanceof NBTTagCompound))
            {
                continue;
            }
            NBTTagCompound compound = (NBTTagCompound) nbtEntity.clone();
            NBTTagList posList = compound.getList("Pos", 6);
            NBTTagDouble posXTag = (NBTTagDouble) posList.get(0);
            NBTTagDouble posZTag = (NBTTagDouble) posList.get(2);
            double posX = posXTag.asDouble();
            double posZ = posZTag.asDouble();
            posList.set(0, NBTTagDouble.a((x << 4) + (posX % 16)));
            posList.set(2, NBTTagDouble.a((z << 4) + (posZ % 16)));
            compound.set("Pos", posList.clone());
        }
    }

    public static void fixTileEntitiesData(NBTTagList tagList, int x, int z)
    {
        if (tagList == null)
        {
            return;
        }
        for (NBTBase nbtTileEntity : tagList)
        {
            if (!(nbtTileEntity instanceof NBTTagCompound))
            {
                continue;
            }
            NBTTagCompound compound = (NBTTagCompound) nbtTileEntity.clone();
            int posX = compound.getInt("x");
            int posZ = compound.getInt("z");
            compound.setInt("x", (x << 4) | (posX & 0xf));
            compound.setInt("z", (z << 4) | (posZ & 0xf));
        }
    }

    private static class ChunkReloadTask implements Runnable
    {

        private final Chunk chunk;

        public ChunkReloadTask(Chunk chunk)
        {
            this.chunk = chunk;
        }

        @Override
        public void run()
        {
            chunk.unload();
            chunk.load();
        }
    }
}
