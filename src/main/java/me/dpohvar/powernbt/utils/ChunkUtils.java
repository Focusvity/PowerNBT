package me.dpohvar.powernbt.utils;

import me.dpohvar.powernbt.PowerNBT;
import me.dpohvar.powernbt.api.NBTCompound;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static me.dpohvar.powernbt.utils.NBTUtils.nbtUtils;
import static me.dpohvar.powernbt.utils.ReflectionUtils.*;

public class ChunkUtils
{

    public static ChunkUtils chunkUtils = new ChunkUtils();
    //Map<Object, Object> chunkLoaderMap = new WeakHashMap<Object, Object>();
    private RefClass cChunkCoordIntPair;
    private RefClass cIChunkAccess;
    private RefField fChunkProvider;
    private RefField fChunks;
    private RefMethod mGetChunkHandle;
    private RefMethod mGetWorldHandle;
    private RefMethod mSaveChunk;
    private RefMethod mLoadChunk;
    private RefMethod mLoadEntities;
    //private RefMethod mPutToMap;
    private RefMethod mAddEntities;
    private RefMethod mDefinedStructureManager;
    private RefMethod mVillagePlace;


    private ChunkUtils()
    {
        try
        {
            RefClass cCraftChunk = getRefClass("{cb}.CraftChunk, {CraftChunk}");
            RefClass cChunk = getRefClass("{nms}.Chunk, {nm}.world.chunk.Chunk, {Chunk}");
            mGetChunkHandle = cCraftChunk.findMethodByReturnType(cChunk);
            RefClass cCraftWorld = getRefClass("{cb}.CraftWorld, {CraftWorld}");
            RefClass cWorldServer = getRefClass("{nms}.WorldServer, {nm}.world.WorldServer, {WorldServer}");
            mGetWorldHandle = cCraftWorld.findMethodByReturnType(cWorldServer);
            //RefClass iChunkProvider = getRefClass("{nms}.IChunkProvider, {nm}.world.chunk.IChunkProvider, {IChunkProvider}");
            RefClass cChunkProviderServer = getRefClass("{nms}.ChunkProviderServer, {nm}.world.gen.ChunkProviderServer, {ChunkProviderServer}");
            //RefClass iChunkLoader = getRefClass("{nms}.IChunkLoader, {nm}.world.chunk.storage.IChunkLoader, {IChunkLoader}");
            fChunkProvider = cWorldServer.findField(cChunkProviderServer);
            RefClass cChunkRegionLoader = getRefClass("{nms}.ChunkRegionLoader, {nm}.world.chunk.storage.AnvilChunkLoader, {ChunkRegionLoader}");
            //RefClass cWorld = getRefClass("{nms}.World, {nm}.world.World, {World}");
            //RefClass cNBTTagCompound = getRefClass("{nms}.NBTTagCompound, {nm}.nbt.NBTTagCompound, {NBTTagCompound}");
            cChunkCoordIntPair = getRefClass("{nms}.ChunkCoordIntPair, {nm}.world.chunk.ChunkCoordIntPair, {ChunkCoordIntPair}");
            cIChunkAccess = getRefClass("{nms}.IChunkAccess, {nm}.world.chunk.IChunkAccess, {ChunkCoordIntPair}");
            mSaveChunk = cChunkRegionLoader.findMethodByName("saveChunk");
            //RefClass cLongObjectHashMap;
            /*try
            {
                cLongObjectHashMap = getRefClass("{cb}.util.LongObjectHashMap, {LongObjectHashMap}");
                fChunks = cChunkProviderServer.findField(cLongObjectHashMap);
            }
            catch (RuntimeException ignored)
            {
                cLongObjectHashMap = getRefClass("{nm}.util.LongHashMap, {LongHashMap}");
                fChunks = cChunkProviderServer.findField(cLongObjectHashMap);
            }*/
            //mLoadChunk = cChunkRegionLoader.findMethodByParams(cWorld, cNBTTagCompound);
            mLoadChunk = cChunkRegionLoader.findMethodByName("loadChunk");
            mLoadEntities = cChunkRegionLoader.findMethodByName("loadEntities");
            //mPutToMap = cLongObjectHashMap.findMethodByParams(long.class, Object.class);
            mAddEntities = cChunk.findMethod(
                    new MethodCondition()
                            .withReturnType(void.class)
                            .withTypes()
                            .withName("addEntities"),
                    new MethodCondition()
                            .withForge(true)
                            .withReturnType(void.class)
                            .withTypes()
                            .withSuffix("c")
            );
            mDefinedStructureManager = cWorldServer.findMethodByName("r_");
            mVillagePlace = cWorldServer.findMethodByName("x");
        }
        catch (Exception e)
        {
            if (PowerNBT.plugin.isDebug())
            {
                PowerNBT.plugin.getLogger().log(Level.WARNING, "Can't load ChunkUtils!", e);
            }
            else
            {
                PowerNBT.plugin.getLogger().log(Level.WARNING, "Can't load ChunkUtils!");
            }
        }
    }

    public static long toLong(int msw, int lsw)
    {
        return ((long) msw << 32) + (long) lsw - -2147483648L;
    }

    /*private Object getChunkLoader(Object nmsWorld)
    {
        Object chunkLoader = chunkLoaderMap.get(nmsWorld);
        if (chunkLoader != null) return chunkLoader;
        Object chunkProvider = fChunkProvider.of(nmsWorld).get();
        chunkLoader = fChunkLoader.of(chunkProvider).get();
        chunkLoaderMap.put(nmsWorld, chunkLoader);
        return chunkLoader;
    }*/

    public NBTCompound readChunk(Chunk chunk)
    {
        Object nmsWorld = mGetWorldHandle.of(chunk.getWorld()).call();
        Object nmsChunk = mGetChunkHandle.of(chunk).call();
        Object nbt = mSaveChunk.of(fChunkProvider).call(nmsWorld, nmsChunk); // saveChunk(WorldServer, IChunkAccess)
        return NBTCompound.forNBTCopy(nbt);
    }

    public void writeChunk(Chunk chunk, Object nbtTagCompound)
    {
        writeChunk(chunk, nbtTagCompound, false);
    }

    public void writeChunkUnsafe(Chunk chunk, Object nbtTagCompound)
    {
        writeChunk(chunk, nbtTagCompound, true);
    }

    private void writeChunk(Chunk chunk, Object nbtTagCompound, boolean unsafe)
    {
        Object nmsWorld = mGetWorldHandle.of(chunk.getWorld()).call();
        int x = chunk.getX();
        int z = chunk.getZ();
        Object chunkCoordIntPair = cChunkCoordIntPair.getConstructor().create(x, z);
        // remove entities
        for (Entity entity : chunk.getEntities())
        {
            if (!(entity instanceof Player)) entity.remove();
        }
        // unload chunk
        chunk.unload();
        // read nbt tag
        nbtTagCompound = nbtUtils.cloneTag(nbtTagCompound);
        if (!unsafe)
        {
            Map<String, Object> handleMap = nbtUtils.getHandleMap(nbtTagCompound);
            // fix x,z coordinates
            handleMap.put("xPos", nbtUtils.createTagInt(x));
            handleMap.put("zPos", nbtUtils.createTagInt(z));
            // fix entities coordinates
            fixEntitiesData(handleMap.get("Entities"), x, z);
            // fix tile entities coordinates
            fixTileEntitiesData(handleMap.get("TileEntities"), x, z);
        }
        // create new chunk
        Object newChunk = mLoadChunk.of(fChunkProvider).call(nmsWorld,
                mDefinedStructureManager.of(nmsWorld),
                mVillagePlace.of(nmsWorld),
                chunkCoordIntPair,
                nbtTagCompound); // loadChunk(WorldServer, DefinedStructureManager, VillagePlace, ChunkCoordIntPair, NBTTagCompound)
        // load entities
        if (mLoadEntities != null) mLoadEntities.of(fChunkProvider).call(nbtTagCompound, newChunk);
        // add entities
        mAddEntities.of(newChunk).call();
        // save chunk to provider map
        //Object chunkMap = fChunks.of(chunkProvider).get();
        long hash = toLong(x, z);
        //mPutToMap.of(chunkMap).call(hash, newChunk);
        // update chunk
        ChunkReloadTask task = new ChunkReloadTask(chunk);
        task.run();
        Bukkit.getScheduler().runTaskLater(PowerNBT.plugin, task, 2);
    }

    private void fixEntitiesData(Object nbtList, int x, int z)
    {
        if (nbtList == null) return;
        List<Object> list = nbtUtils.getHandleList(nbtList);
        for (Object nbtEntity : list)
        {
            while (nbtEntity != null)
            {
                Map<String, Object> entityMap = nbtUtils.getHandleMap(nbtEntity);
                List<Object> posList = nbtUtils.getHandleList(entityMap.get("Pos"));
                double posX = (Double) nbtUtils.getValue(posList.get(0));
                double posZ = (Double) nbtUtils.getValue(posList.get(2));
                posList.set(0, nbtUtils.createTagDouble((x << 4) + (posX % 16)));
                posList.set(2, nbtUtils.createTagDouble((z << 4) + (posZ % 16)));
                nbtEntity = entityMap.get("Riding");
            }

        }
    }

    private void fixTileEntitiesData(Object nbtList, int x, int z)
    {
        if (nbtList == null) return;
        List<Object> list = nbtUtils.getHandleList(nbtList);
        for (Object nbtEntity : list)
        {
            Map<String, Object> entityMap = nbtUtils.getHandleMap(nbtEntity);
            int posX = (Integer) nbtUtils.getValue(entityMap.get("x"));
            int posZ = (Integer) nbtUtils.getValue(entityMap.get("z"));
            entityMap.put("x", nbtUtils.createTagInt((x << 4) | (posX & 0xf)));
            entityMap.put("z", nbtUtils.createTagInt((z << 4) | (posZ & 0xf)));
        }
    }

    class ChunkReloadTask implements Runnable
    {

        private Chunk chunk;

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
