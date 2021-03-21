package me.focusvity.powernbt.nms;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;
import java.util.Optional;

public class NMSEntity
{

    public static net.minecraft.server.v1_16_R3.Entity getEntityHandle(Entity entity)
    {
        return ((CraftEntity) entity).getHandle();
    }

    public static void readEntity(Entity entity, NBTTagCompound compound)
    {
        net.minecraft.server.v1_16_R3.Entity nms = getEntityHandle(entity);
        nms.load(compound);
    }

    public static void writeEntity(Entity entity, NBTTagCompound compound)
    {
        getEntityHandle(entity).save(compound);
    }

    public static Entity spawnEntity(NBTTagCompound compound, World world)
    {
        compound = compound.clone();
        CraftWorld cWorld = (CraftWorld) world;
        WorldServer nmsWorld = cWorld.getHandle();
        net.minecraft.server.v1_16_R3.Entity nmsEntity = EntityTypes.a(compound, nmsWorld).get();
        if (nmsEntity == null)
        {
            return null;
        }
        nmsEntity.load(compound);
        nmsWorld.addEntity(nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        Entity entity = nmsEntity.getBukkitEntity();
        Location location = entity.getLocation();
        NBTTagList posTag = new NBTTagList();
        posTag.add(NBTTagDouble.a(location.getX()));
        posTag.add(NBTTagDouble.a(location.getY()));
        posTag.add(NBTTagDouble.a(location.getZ()));
        Entity currentEntity = entity;
        while (true)
        {
            NBTTagList passengerTag = compound.getList("Passengers", 6);
            if (passengerTag == null)
            {
                break;
            }
            passengerTag.add(posTag.clone());
            compound.set("Passengers", passengerTag);
            net.minecraft.server.v1_16_R3.Entity nmsPassenger = EntityTypes.a(compound, nmsWorld).get();
            if (nmsPassenger == null)
            {
                break;
            }
            nmsWorld.addEntity(nmsEntity);
            Entity passenger = (Entity) nmsEntity;
            currentEntity.addPassenger(passenger);
            currentEntity = passenger;
        }
        return entity;
    }
}
